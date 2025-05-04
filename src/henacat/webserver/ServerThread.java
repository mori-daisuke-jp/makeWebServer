package henacat.webserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import henacat.servletimpl.*;
import henacat.util.*;

public class ServerThread implements Runnable {
    private static final String DOCUMENT_ROOT = "/home/horikawa/project/makeWebServer/web";
    private static final String ERROR_DOCUMENT = "/home/horikawa/project/makeWebServer/web";
    private Socket socket;

    ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        OutputStream output = null;
        try {
            InputStream input = socket.getInputStream();

            String line;
            String requestLine = null;
            String method = null;
            Map<String, String> headers = new HashMap<String, String>();
            
            while ((line = Util.readLine(input)) != null) {
                if ("".equals(line)) {
                    break; // End of headers
                }
                if (line.startsWith("GET")) {
                    method = "GET";
                    requestLine = line;
                } else if (line.startsWith("POST")) {
                    method = "POST";
                    requestLine = line;
                } else {
                    addRequestHeader(headers, line);
                }
            }

            if (requestLine == null) {
                return;
            }

            String reqeustURI = MyURLDecoder.decode(requestLine.split(" ")[1], "UTF-8");
            String[] pathAndQuery = reqeustURI.split("?");
            String path = pathAndQuery[0];
            String query = null;
            if (pathAndQuery.length > 1) {
                query = pathAndQuery[1];
            }

            output = new BufferedOutputStream(socket.getOutputStream());

            String appDir = path.substring(1).split("/")[0];
            WebApplication app = WebApplication.searchWebapplication(appDir);
            if (app != null) {
                ServletInfo servletInfo
                    = app.searchServlet(path.substring(appDir.length() + 1));
                if (servletInfo != null) {
                    ServletService.doService(method, query, servletInfo,
                                             headers, input, output);
                    return;
                }
            }

            String ext = null;
            String[] tmp = reqeustURI.split(".");
            ext = tmp[tmp.length - 1];

            if (path.endsWith("/")) {
                path += "index.html";
                ext = "html";
            }

            FileSystem fs = FileSystems.getDefault();
            Path filePath = fs.getPath(DOCUMENT_ROOT + path);
            Path realPath;

            try {
                realPath = filePath.toRealPath();
            } catch (NoSuchFileException e) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            }

            if(!realPath.startsWith(DOCUMENT_ROOT)) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            } else if (Files.isDirectory(realPath)) {
                String host = headers.get("HOST");
                String location = "http://" 
                        + ((host == null) ? Constants.SERVER_NAME : host)
                        + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
               return;
            }
            try (InputStream fis =
                    new BufferedInputStream(Files.newInputStream(realPath))) {
                SendResponse.sendOKResponse(output, fis, ext);
            } catch (FileNotFoundException e) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addRequestHeader(Map<String, String> headers, String line) {
        int index = line.indexOf(':');
        if (index == -1) {
            return;
        }
        String headerName = line.substring(0, index).trim().toUpperCase();
        String headerValue = line.substring(index + 1).trim();
        headers.put(headerName, headerValue);
    }
    
}
