import java.io.*;
import java.net.*;

public class TcpClient {
   public static void main(String[] args) {
        String serverAddress = "localhost"; // Server address
        int port = 8080; // Port number for the server
        try (Socket socket = new Socket(serverAddress, port)) {
             System.out.println("Connected to server at " + serverAddress + ":" + port);
             FileInputStream fis = new FileInputStream("client_send.txt");
             FileOutputStream fos = new FileOutputStream("client_receive.txt");
    
             // send data from file to server
             OutputStream output = socket.getOutputStream();
             int ch;
             while ((ch = fis.read()) != -1) {
                output.write(ch);
             }
             
             output.write(0); // Indicate end of file transfer

             // write data which is received from server to file
             InputStream input = socket.getInputStream();
             while ((ch = input.read()) != -1) {
                fos.write(ch); 
             }
             
             System.out.println("File sent and received successfully");
        } catch (Exception e) {
             e.printStackTrace();
        }
   } 
}
