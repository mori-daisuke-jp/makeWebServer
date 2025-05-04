package chapter2;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class ServerThread implements Runnable {
    private static final String DOCUMENT_ROOT = "/home/horikawa/project/makeWebServer/web";
    private static final String ERROR_DOCUMENT = "/home/horikawa/project/makeWebServer/web";
    private static final String SERVER_NAME = "localhost:8002";
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
            String path = null;
            String ext = null;
            String host = null;

            while ((line = Util.readLine(input)) != null) {
                if ("".equals(line)) {
                    break; // End of headers
                }
                if (line.startsWith("GET")) {
                    path = MyURLDecoder.decode(line.split(" ")[1], "UTF-8");
                } else if (line.startsWith("Host:")) {
                    host = line.substring("Host: ".length());
                }
            }

            if (path == null) {
                return;
            }

            if (path.endsWith("/")) {
                path += "index.html";
            }
            ext = path.substring(path.lastIndexOf('.') + 1);

            output = new BufferedOutputStream(socket.getOutputStream());

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
                String location = "http://" 
                        + ((host == null) ? SERVER_NAME : host)
                        + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
               return;
            }
            try (InputStream fis =
                    new BufferedInputStream(Files.newInputStream(realPath))) {
                System.out.println("realPath = " + realPath);
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
    
}
