
package retina.chat.common;

import java.io.Serializable;
import java.util.Vector;

public class Packet implements Serializable
{
    // message types
    public static final int MSGTYPE_SIGNON     = 1;
    public static final int MSGTYPE_CREATE_NEW_GROUP = 2;
    public static final int MSGTYPE_BROADCAST_NEW_GROUP = 3;
    public static final int MSGTYPE_BROADCAST_REMOVE_GROUP = 4;
    public static final int MSGTYPE_JOIN_GROUP = 5;
    public static final int MSGTYPE_BROADCAST_NEW_GROUP_MEMBER = 6;
    public static final int MSGTYPE_BROADCAST_REMOVE_GROUP_MEMBER = 7;
    public static final int MSGTYPE_CHAT_MESSAGE = 8;
    public static final int MSGTYPE_BROADCAST_CHAT_MESSAGE = 9;
    public static final int MSGTYPE_LEAVE_GROUP = 10;
    public static final int MSGTYPE_SIGN_OUT = 11;
    public static final int MSGTYPE_HELP = 12;

    // generic error codes
    public static final int SUCCESS            = 100;

    // error codes for SIGNON messages
    public static final int ERROR_USER_EXISTS  = 101;
    public static final int ERROR_USER_EMPTY  = 105;

    // error codes for CREATE_NEW_GROUP messages
    public static final int ERROR_GROUP_EXISTS = 102;
    public static final int ERROR_GROUP_EMPTY = 106;

    // error codes for JOIN_GROUP messages
    public static final int ERROR_ALREADY_IN_GROUP = 103;
    public static final int ERROR_GROUP_DOES_NOT_EXIST = 104;

    private int messageType = -1;
    private int statusCode  = -1;
    private String chatMessage = null;
    private Vector dataVector = null;
    private String groupname = null;
    private String username = null;


    /** Creates a new instance of Packet. we'll set the values later with accessors. */
    public Packet(int messageType)
    {
        this.messageType = messageType;
    }

    public int getMessageType()
    {
        return messageType;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    public void setDataVector(Vector data)
    {
        this.dataVector = data;
    }

    public Vector getDataVector()
    {
        return dataVector;
    }

    public void setGroupname(String groupname)
    {
        this.groupname = groupname;
    }

    public String getGroupname()
    {
        return groupname;
    }

    public void setChatMessage(String chatMessage)
    {
        this.chatMessage = chatMessage;
    }

    public String getChatMessage()
    {
        return chatMessage;
    }
}
