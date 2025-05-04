package henacat.webserver;
import java.net.*;
import henacat.servletimpl.*;

public class Main {
    public static void main(String[] args) throws Exception {
        WebApplication app = WebApplication.createInstance("testbbs");
        app.addServlet("/ShowBBS", "ShowBBS");
        app.addServlet("/PostBBS", "PostBBS");
        try ( ServerSocket server = new ServerSocket(8001)){
            for (;;){
                Socket socket = server.accept();
                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }
    } 
 }
