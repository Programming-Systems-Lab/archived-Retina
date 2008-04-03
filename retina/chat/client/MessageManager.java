
package retina.chat.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import retina.chat.common.Packet;


public class MessageManager extends Thread
{
    private ChatClient parent;
    private String host;
    private int port;
    private Socket socket;

    /** Creates a new instance of MessageManager */
    public MessageManager(ChatClient parent, String host, int port)
    {
        this.parent = parent;
        this.host   = host;
        this.port   = port;
    }

    public void finalize()
    {
        disconnect();
    }

    public void run()
    {
        /**
         * serialization code based on
         * http://www.javapractices.com/Topic57.cjp
         */
        ObjectInput input = null;
        try
        {
            while (true)
            {
                // deserialize the Packet
                // InputStream buffer = new BufferedInputStream(socket.getInputStream());
                input = new ObjectInputStream(socket.getInputStream());
                Packet packet = (Packet)input.readObject();

                if (packet == null)
                {
                    System.out.println("A null packet was read!!");
                    continue;
                }

                if (parent == null)
                {
                    //System.out.println("null parent; received message" + packet.getMessageType());
                    continue;
                }

                // do the magic with the packet
                switch (packet.getMessageType())
                {
                    case Packet.MSGTYPE_SIGNON:
                        //System.out.println("received a sign on message");
                        parent.getAuthenticationManager().handleSignInMessage(packet);
                        break;
                    case Packet.MSGTYPE_CREATE_NEW_GROUP:
                        //System.out.println("received a create new group message");
                        parent.getGroupManager().handleCreateAndJoinNewGroupMessage(packet);
                        break;
                    case Packet.MSGTYPE_BROADCAST_NEW_GROUP:
                        //System.out.println("received MSGTYPE_BROADCAST_NEW_GROUP");
                        parent.getGroupManager().handleUpdateGroupListWithNewGroupMessage(packet);
                        break;
                    case Packet.MSGTYPE_BROADCAST_REMOVE_GROUP:
                        //System.out.println("received MSGTYPE_BROADCAST_REMOVE_GROUP");
                        parent.getGroupManager().handleUpdateGroupListRemoveGroupMessage(packet);
                        break;
                    case Packet.MSGTYPE_JOIN_GROUP:
                        //System.out.println("received a join group message");
                        parent.getGroupManager().handleJoinGroupMessage(packet);
                        break;
                    case Packet.MSGTYPE_BROADCAST_NEW_GROUP_MEMBER:
                        //System.out.println("received MSGTYPE_BROADCAST_NEW_GROUP_MEMBER");
                        parent.getGroupManager().handleUpdateGroupMemberListWithNewMemberMessage(packet);
                        break;
                    case Packet.MSGTYPE_BROADCAST_REMOVE_GROUP_MEMBER:
                        //System.out.println("received MSGTYPE_BROADCAST_REMOVE_GROUP_MEMBER");
                        parent.getGroupManager().handleUpdateGroupMemberListRemoveMemberMessage(packet);
                        break;
                    case Packet.MSGTYPE_BROADCAST_CHAT_MESSAGE:
                        //System.out.println("received MSGTYPE_BROADCAST_CHAT_MESSAGE");
                        parent.getChatManager().handleReceiveChatMessageMessage(packet);
                        break;
                    default:
                        //System.out.println("received some other type of packet");
                        break;
                }
            }
        }
        catch (java.net.SocketException e)
        {
            System.out.println("connection to server closed.");
            System.exit(0);
        }
        catch (java.io.EOFException e)
        {
            System.out.println("connection to server closed unexpectedly.  fo sheezy, i'm queazy.");
            System.exit(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean connect()
    {
        try
        {
            socket = new Socket(host, port);
            if (socket == null)
            {
                System.out.println("Could not create a socket to " + host + ":" + port);
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Could not create a socket to " + host + ":" + port + "; exception " + e.toString());
            return false;
        }
    }

    public void disconnect()
    {
        try
        {
            socket.close();
        }
        catch (Exception e)
        {
            System.out.println("MessageManager disconnect failed. " + e.toString());
        }
    }

    /**
     * returns true if packet was sent, or false on exception.
     */
    public static boolean sendMessage(Socket socket, Packet packet)
    {
        ObjectOutput output = null;
        try
        {
            /**
             * serialization code based on
             * http://www.javapractices.com/Topic57.cjp
             */
            //OutputStream buffer = new BufferedOutputStream(socket.getOutputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            //output = new ObjectOutputStream(buffer);
            output.writeObject(packet);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean sendMessage(Packet packet)
    {
        return sendMessage(socket, packet);
    }
}
