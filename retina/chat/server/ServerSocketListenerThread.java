package retina.chat.server;

import java.net.*;
import java.io.*;

public class ServerSocketListenerThread extends Thread{
    ServerSocket server;
    ChatServer leder;

    /** Creates a new instance of ServerSocketListenerThread */
    public ServerSocketListenerThread(ChatServer L, ServerSocket s) {
        server = s;
        leder = L;
    }

    /**
     *  Function: run
     *  Desc: wait for socket requests and grant
     */
    public void run(){
//        System.out.println("Running server socket");
        while(true){
            try{
                Socket s = server.accept();
//                System.out.println("Found a request");
                leder.addSocket(s);
                UserSocketListenerThread user = new UserSocketListenerThread(leder, s);
                user.start();
//                System.out.println("Started user thread");
            }catch(Exception e){
                System.out.println("*****Error happened in Server Listener Thread");
               // e.printStackTrace();
            }
        }
    }
}
