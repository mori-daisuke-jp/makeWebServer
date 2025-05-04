package chapter1;

import java.io.*;
import java.net.*;

public class TcpServer {
    public static void main(String[] args) {
        int port = 8001; // Port number for the server
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            FileOutputStream fos = new FileOutputStream("/home/horikawa/project/makeWebServer/var/server_receive.txt");
            FileInputStream fis = new FileInputStream("/home/horikawa/project/makeWebServer/var/server_send.txt");
            System.out.println("Server is ready to receive files");
            Socket socket = server.accept(); // Accept a client connection
            System.out.println("Client connected");

            int ch;
            // write data which is received from client to file
            InputStream inputStream = socket.getInputStream();
            while ((ch = inputStream.read()) != 0) {
                fos.write(ch); 
            }
            // send data from file to client
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
            socket.close();
            System.out.println("File received and sent successfully");
            
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}