package chapter2;

import java.io.*;

class SendResponse {
    static void sendOKResponse(OutputStream output, InputStream fis,
                                        String ext) throws Exception {
        String contentType = Util.getContentType(ext);
        Util.writeLine(output, "HTTP/1.1 200 OK");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Modoki/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-Type: " + contentType);
        Util.writeLine(output, "");
        
        int ch;
        while ((ch = fis.read()) != -1) {
            output.write(ch);
        }
    }       

    static void sendMovePermanentlyResponse(OutputStream output, String location)
                                                 throws Exception {
        Util.writeLine(output, "HTTP/1.1 301 Moved Permanently");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Modoki/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Location: " + location);
        Util.writeLine(output, "");
    }

    static void sendNotFoundResponse(OutputStream output,
                                        String errorDocumtentRoot)
                                        throws Exception {
        Util.writeLine(output, "HTTP/1.1 404 Not Found");
        Util.writeLine(output, "Date: " + Util.getDateStringUtc());
        Util.writeLine(output, "Server: Modoki/0.2");
        Util.writeLine(output, "Connection: close");
        Util.writeLine(output, "Content-Type: text/html");
        Util.writeLine(output, "");
    
        try (InputStream fis = 
                new BufferedInputStream(new FileInputStream(errorDocumtentRoot 
                                                            + "/404.html"))) {
            int ch;
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
        }
    }
}
