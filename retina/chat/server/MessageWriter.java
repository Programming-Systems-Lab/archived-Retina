package retina.chat.server;


import retina.chat.common.Packet;

import java.util.*;
import java.net.*;
import java.io.*;

public class MessageWriter {
    ChatServer server;

    /** Creates a new instance of MessageWriter */
    public MessageWriter(ChatServer L) {
        server = L;
    }

    public void writeOut(Socket s, Packet p){
        if(s == null || p == null) return;

        if(!s.isConnected()) return;

        ObjectOutput output = null;
        try
        {
            output = new ObjectOutputStream(s.getOutputStream());
            output.writeObject(p);
        }
        catch (Exception e)
        {
            //System.out.println(e.getMessage());
           // e.printStackTrace();
        }
    }

    /* sign on to the server messages */
    // TESTED
    public void signOnOK(Socket s, Vector groups){
        if( s == null) return;

//        System.out.println("sign On OK");
    //    System.out.println("");

        Packet p = new Packet(Packet.MSGTYPE_SIGNON);
        p.setStatusCode(Packet.SUCCESS);

        Vector groupNames = new Vector();
        for(int i = 0; i < groups.size(); i++){
            Group g = (Group)groups.elementAt(i);
            groupNames.addElement(g.getName());
        }

        p.setDataVector(groupNames);

        writeOut(s, p);
    }

    // TESTED
    public void signOnFailed(Socket s, int error){
        if(s == null) return;

//        System.out.println("sign on failed");
        Packet p = new Packet(Packet.MSGTYPE_SIGNON);
        p.setStatusCode(error);
        writeOut(s, p);
    }

    /* open group messages */
    // TESTED
    public void openGroupOK(Socket s, String groupName){
        if(s == null || groupName == null || groupName.equals("")) return;
//        System.out.println("create group ok");
        Packet p = new Packet(Packet.MSGTYPE_CREATE_NEW_GROUP);
        p.setStatusCode(Packet.SUCCESS);
        p.setGroupname(groupName);
        writeOut(s, p);
    }

    // TESTED
    public void openGroupFailed(Socket s, int error){
        if(s == null) return;

//        System.out.println("create group failed");
        Packet p = new Packet(Packet.MSGTYPE_CREATE_NEW_GROUP);
        p.setStatusCode(error);
        writeOut(s, p);
    }
    // TESTED
    public void broadcastNewGroup(Vector sockets, String groupName){
        if(sockets == null || groupName == null || groupName.equals("")) return;

        Packet p = new Packet(Packet.MSGTYPE_BROADCAST_NEW_GROUP);
        p.setGroupname(groupName);

        for(int i = 0; i < sockets.size(); i++){
            Socket s = (Socket)sockets.elementAt(i);
            if(s != null)
                writeOut(s, p);
        }
    }

    /* join group messages */
    // TESTED
    public void joinGroupOK(Socket s, Vector users, User me, String groupName){
        if(s == null || users == null || me == null || groupName == null || groupName.equals("")) return;

//        System.out.println("join group ok");
        Packet p = new Packet(Packet.MSGTYPE_JOIN_GROUP);
        p.setStatusCode(Packet.SUCCESS);
        p.setGroupname(groupName);

        Vector userList = new Vector();
        for(int i = 0; i < users.size(); i++){
            User u = (User) users.elementAt(i);
            if(!u.equals(me))
                userList.addElement(u.getName());
        }
        p.setDataVector(userList);

        writeOut(s, p);
    }

    // TESTED
    public void joinGroupFailed(Socket s, int error_code){
        if(s == null) return;

//        System.out.println("join group failed");
        Packet p = new Packet(Packet.MSGTYPE_JOIN_GROUP);
        p.setStatusCode(error_code);
        writeOut(s, p);
    }

    public void broadcastNewJoin(Vector sockets, String groupName, String userName){
        if(sockets == null || groupName == null || groupName.equals("") || userName == null || userName.equals("")) return;

        Packet p = new Packet(Packet.MSGTYPE_BROADCAST_NEW_GROUP_MEMBER);
        p.setUsername(userName);
        p.setGroupname(groupName);

        for(int i = 0; i < sockets.size(); i++){
            Socket s = (Socket)sockets.elementAt(i);
            if(s != null)
                writeOut(s, p);
        }
    }

    /* leave group messages */
    // TESTED
    public void leaveGroupOK(Socket s, String groupName){
        if(s == null || groupName == null || groupName.equals("")) return;

//        System.out.println("Leaving Group OK");
        Packet p = new Packet(Packet.MSGTYPE_LEAVE_GROUP);
        p.setStatusCode(Packet.SUCCESS);
        p.setGroupname(groupName);

        writeOut(s, p);
    }

    // TESTED
    public void broadcastLeaveGroup(Vector sockets, String groupName, String userName){
        if(sockets == null || groupName == null || groupName.equals("") || userName == null || userName.equals("")) return;

//        System.out.println("broadcast leaving group");
        Packet p = new Packet(Packet.MSGTYPE_BROADCAST_REMOVE_GROUP_MEMBER);
        p.setGroupname(groupName);
        p.setUsername(userName);

         for(int i = 0; i < sockets.size(); i++){
            Socket s = (Socket)sockets.elementAt(i);
            if(s != null)
                writeOut(s, p);
        }
    }


    public void broadcastRemoveGroup(Vector sockets, String groupName){
        if(sockets == null || groupName == null || groupName.equals("")) return;

//        System.out.println("broadcast remove group");
        Packet p = new Packet(Packet.MSGTYPE_BROADCAST_REMOVE_GROUP);
        p.setGroupname(groupName);

        for(int i = 0; i < sockets.size(); i++){
            Socket s = (Socket) sockets.elementAt(i);
            if(s != null)
                writeOut(s, p);
        }
    }

    public void broadcastChatMessage(Vector sockets, String message, String groupName, String userName){
        if(sockets == null || groupName == null || groupName.equals("") || userName == null || userName.equals("") || message == null|| message.equals("")) return;

//        System.out.println("broadcast chat message");
        Packet p = new Packet(Packet.MSGTYPE_BROADCAST_CHAT_MESSAGE);
        p.setGroupname(groupName);
        p.setUsername(userName);
        p.setChatMessage(message);

        for(int i = 0; i < sockets.size(); i++){
            Socket s = (Socket)sockets.elementAt(i);
            if(s != null)
                writeOut(s, p);
        }
    }

    /* Sign off */
    // TESTED
    public void signOffOK(Socket s){
        if (s == null) return;
//        System.out.println("Leaving System OK");
        Packet p = new Packet(Packet.MSGTYPE_SIGN_OUT);
        p.setStatusCode(Packet.SUCCESS);
        writeOut(s, p);
    }
}
