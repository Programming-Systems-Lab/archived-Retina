package retina.db;
/**
 * This class is the front-end server for getting login events sent from the field and then writing them
 * to the database. 
 * 
 * TODO: handle errors more gracefully
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

import retina.common.Logger;
import retina.im.MessageSender;


public class LoginEventServer extends Thread
{
    // the server
    private ServerSocket server;

    /* This is the main method for starting the Server */
    public static void main(String[] args)
    {
    	int port = 1235;
		if (args.length >= 1)
		{
			port = Integer.parseInt(args[0]);
		}
		else
		{
			System.out.println("Port not specified, using " + port + " as default");
		}

		LoginEventServer s = new LoginEventServer(port);
		s.start();
    }

    /**
     * Creates a socket server listening on the specified port
     * @param port the port to listen to
     */
    public LoginEventServer(int port)
    {
    	init(port);
    }

    private void init(int port)
    {
		try
		{
		    server = new ServerSocket(port);
		    System.out.println("LoginEventServer started... waiting for connection");
		}
		catch (Exception e)
		{
		    System.out.println("Cannot create LoginEventServer!");
		    e.printStackTrace();
		}
    }
    
    
    /**
     * This method does all the work
     */
    public void run()
    {
    	// TODO: need a graceful shutdown
    	while (true)
    	{
    		try
    		{
    			// wait for a client
    			Socket socket = server.accept();
    			
    			// spin off a new thread
    			Handler h = new Handler(socket);
    			h.start();
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * An inner class to do the work of reading the file and writing to the database and such
     */
    class Handler extends Thread
    {
    	Socket socket = null;
    	
    	Handler(Socket s)
    	{
    		socket = s;
    	}

    	public void run()
    	{
    	    // the input stream for reading from the network
    	    Scanner in = null; 

    	    try
    		{
    			// get the input stream
    			in = new Scanner(socket.getInputStream());

    			String student = in.next();
    			String date = in.next().trim();
    			String action = in.nextLine().trim();

    		    // the database manager
    		    LoginEventManager manager = new LoginEventManager();
    		    manager.insertLoginEvent(student, date, action);

    		    if (action.equals("LOGIN")) System.out.println(student + " logged in at " + date);
    		    else if (action.equals("LOGOUT")) System.out.println(student + " logged out at " + date);
    		    else System.out.println(student + " unknown action " + action);
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    			if (Logger.isLogError()) Logger.logError(e);
    		}
    		finally
    		{
    		}
    	}
    }

}
