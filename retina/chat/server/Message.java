package retina.chat.server;

import retina.chat.common.Packet;

import java.net.*;


public class Message{
    //String userName;
    Packet packet;
    Socket socket;

    /** Creates a new instance of Message */
    public Message(Socket s, Packet p) {
      //  userName = "";
        socket = s;
        packet = p;
    }

    public Packet getPacket(){
        return packet;
    }

    public Socket getSocket(){
        return socket;
    }
}
