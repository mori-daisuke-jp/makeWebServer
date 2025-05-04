package chapter2;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
   public static void main(String[] args) throws Exception {
        try ( ServerSocket server = new ServerSocket(8002)){
            for (;;){
                Socket socket = server.accept(); // Accept a client connection
                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }
   } 
}
