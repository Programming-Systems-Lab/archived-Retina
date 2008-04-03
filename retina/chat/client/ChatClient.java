package retina.chat.client;

import java.awt.Cursor;
import javax.swing.*;
import javax.swing.JFrame;


public class ChatClient
{
    private AuthenticationManager authenticationManager;
    private GroupManager groupManager;
    private ChatManager chatManager;
    private MessageManager messageManager;
    private String username;
    
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 4444;

    /** Creates a new instance of Main
     *  serverMachineName is a domain name or IP address.
     */
    public ChatClient(String serverMachineName, int serverPortNumber)
    {
        authenticationManager = new AuthenticationManager(this);
        groupManager = new GroupManager(this);
        chatManager = new ChatManager(this);
        messageManager = new MessageManager(this, serverMachineName, serverPortNumber);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String usageStatement = "Usage: java ChatClient SERVER_NAME SERVER_PORT";

        /*
        if (args.length != 2)
        {
            System.out.println(usageStatement);
            System.out.println("SERVER_NAME: IP Address or domain name of machine where chat server is running.");
            System.out.println("SERVER_NAME: Port of machine where chat server is running.");
            System.exit(0);
        }
        */

        String serverMachineName = DEFAULT_HOST;
        int serverPortNumber = DEFAULT_PORT;
        
        if (args.length >= 2)
        {
        	serverMachineName = args[0];

        	serverPortNumber = -1;
        	try
        	{
        		serverPortNumber = Integer.parseInt(args[1]);
        	}
        	catch(NumberFormatException e)
        	{
        		System.out.println(usageStatement);
        		System.out.println("The SERVER_PORT must be an integer.");
        		System.exit(1);
        	}
        }
        
        ChatClient client = new ChatClient(serverMachineName, serverPortNumber);
        client.start();
    }

    public void finalize()
    {
        System.out.println("ChatClient has been garbage-collected.");
    }

    public void start()
    {
        if (!messageManager.connect())
        {
            JOptionPane.showMessageDialog(null, "A connection could not be made to the server!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // start the new thread
        messageManager.start();

        username = JOptionPane.showInputDialog(null, "Please enter your username:", "Welcome to Retina", JOptionPane.PLAIN_MESSAGE);
        // System.out.println("username: " + username);
        authenticationManager.signIn(username);
    }


    public GroupManager getGroupManager()
    {
        return groupManager;
    }

    public AuthenticationManager getAuthenticationManager()
    {
        return authenticationManager;
    }

    public ChatManager getChatManager()
    {
        return chatManager;
    }

    public MessageManager getMessageManager()
    {
        return messageManager;
    }
}
