package retina.chat.client;

import java.awt.Cursor;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import retina.chat.common.Packet;


public class AuthenticationManager
{
    private ChatClient parent;
    private String username;

    /** Creates a new instance of SignInManager */
    public AuthenticationManager(ChatClient chatClient)
    {
        parent = chatClient;
    }

    public void signIn(String username)
    {
        if (username == null)
        {
            // the user clicked "cancel"
            System.exit(0);
        }

        while (username.length() == 0)
        {
            username = JOptionPane.showInputDialog(null, "The username must not be blank!\nPlease enter your username:", "Welcome to Lederhosen 2.0", JOptionPane.PLAIN_MESSAGE);
            if (username == null)
            {
                // the user clicked "cancel"
                System.exit(0);
            }
        }

        this.username = username;

        Packet packet = new Packet(Packet.MSGTYPE_SIGNON);
        packet.setUsername(username);
        boolean sendMessageStatus = parent.getMessageManager().sendMessage(packet);

        if (!sendMessageStatus)
        {
            JOptionPane.showMessageDialog(null, "Could not connect to the server to try to sign in.\nPlease try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public void handleSignInMessage(Packet packet)
    {
        if (packet == null)
        {
            username = JOptionPane.showInputDialog(null, "An error occurred while communicating with the server. Please try to sign in again:", "Welcome to Lederhosen 2.0", JOptionPane.ERROR_MESSAGE);
            signIn(username);
            return;
        }

        Vector groupList = packet.getDataVector();
        int statusCode = packet.getStatusCode();


        if (statusCode == Packet.ERROR_USER_EXISTS)
        {
            username = JOptionPane.showInputDialog(null, "A user with that name already exists. Please enter a different username:", "Welcome to Lederhosen 2.0", JOptionPane.ERROR_MESSAGE);
            signIn(username);
        }
        else if (statusCode == Packet.SUCCESS)
        {
            parent.getGroupManager().setGroupList(groupList);
            parent.getGroupManager().showGroupWindow();
        }
        else
        {
            // something else happened...
            JOptionPane.showMessageDialog(null, "An unknown error occurred! Incorrect status code returned from server.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    public void signOut()
    {
        Packet packet = new Packet(Packet.MSGTYPE_SIGN_OUT);
        parent.getMessageManager().sendMessage(packet);

        // don't forget to clean up!!
        parent.getMessageManager().disconnect();
    }

    public String getUsername()
    {
        return username;
    }

    /*
     * This method is only called by test code.
     */
    public void setUsername(String name)
    {
        this.username = name;
    }
}
