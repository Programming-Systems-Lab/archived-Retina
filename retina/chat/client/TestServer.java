package retina.chat.client;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import retina.chat.common.Packet;

public class TestServer extends Thread
{
    private int port;

    private ServerSocket server = null;
    private Socket socket = null;

    /** Creates a new instance of TestServer */
    public TestServer(int port)
    {
        this.port = port;
    }

    public static void main(String args[])
    {
        TestServer server = new TestServer(1234);
        server.start();

    }

    public void run()
    {
        try
        {
            server = new ServerSocket(1234);
            System.out.println("server waiting for connection");
            socket = server.accept();
            System.out.println("server got a connection!");

            while(true)
            {
                handleMessage();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { server.close(); } catch (Exception e) { }
        }

    }

    private void handleMessage() throws Exception
    {
        ObjectInput input = null;
        input = new ObjectInputStream(socket.getInputStream());


        Packet incomingPacket = (Packet)input.readObject();

        int type = incomingPacket.getMessageType();
        type = type == Packet.MSGTYPE_CHAT_MESSAGE ? Packet.MSGTYPE_BROADCAST_CHAT_MESSAGE : type;
        String username = incomingPacket.getUsername();
        String chatMessage = incomingPacket.getChatMessage();
        String groupname = incomingPacket.getGroupname();
        System.out.println("message type = " + type);

        Packet outgoingPacket = new Packet(type);
        outgoingPacket.setStatusCode(Packet.SUCCESS);
        outgoingPacket.setUsername(username);
        outgoingPacket.setChatMessage(chatMessage);
        outgoingPacket.setGroupname(groupname);
        Vector list = new Vector();
        list.add("Chris");
        list.add("Howie");
        outgoingPacket.setDataVector(list);

        MessageManager.sendMessage(socket, outgoingPacket);

    }

}
