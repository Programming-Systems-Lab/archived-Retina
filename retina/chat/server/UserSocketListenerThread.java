package retina.chat.server;


import java.net.*;
import java.util.*;
import java.io.*;

import retina.chat.common.Packet;

public class UserSocketListenerThread extends Thread{
       Socket socket;
       String userName;
       ChatServer leder;

    /** Creates a new instance of UserSocketListenerThread */
    public UserSocketListenerThread(ChatServer L, Socket s) {
        socket = s;
        leder = L;
        userName = "";
    }

    /**
     *  Function: setUserName
     *  Desc: sets the username that belongs to the socket/thread
     */
    public void setUserName(String name){
        userName = name;
    }

    public void run(){
        //System.out.println("Running a user socket");
        ObjectInput input = null;
        try{
            boolean ok = true;
            while(ok){
                input = new ObjectInputStream(socket.getInputStream());
                Packet packet = (Packet)input.readObject();
                if(packet.getMessageType() == Packet.MSGTYPE_SIGN_OUT){
                    ok = false;
                }
                //System.out.println("I got a packet " + packet.getMessageType());
                Message m = new Message(socket, packet);
                leder.queue(m);
            }
        }catch(Exception e){
            System.out.println("*****Error happened in User Listener Thread");
           // e.printStackTrace();
        }
    }
}
