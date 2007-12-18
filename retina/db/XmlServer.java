package retina.db;
/**
 * This class is the front-end server for getting XML files sent from the field and then writing them
 * to the database. 
 * 
 * TODO: handle errors more gracefully
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class XmlServer extends Thread
{
    // the server
    private ServerSocket server;

    // the tool for writing to the database
    private XMLLoader loader;
    
    // used for sending messages
    private MessageSender sender;

    /* This is the main method for starting the Server */
    public static void main(String[] args)
    {
    	int port = 1234;
		if (args.length >= 1)
		{
			port = Integer.parseInt(args[0]);
		}
		else
		{
			System.out.println("Port not specified, using " + port + " as default");
		}

		XmlServer xs = new XmlServer(port);
		xs.start();
    }

    /**
     * Creates a socket server listening on the specified port
     * @param port the port to listen to
     */
    public XmlServer(int port)
    {
    	sender = new MessageSender();
    	loader = new XMLLoader(sender);
    	init(port);
    }

    public XmlServer(int port, MessageSender ms)
    {
    	sender = ms;
    	loader = new XMLLoader(sender);
    	init(port);
    }

    private void init(int port)
    {
		try
		{
		    server = new ServerSocket(port);
		    System.out.println("XmlServer started... waiting for connection");
		}
		catch (Exception e)
		{
		    System.out.println("Cannot create XmlServer!");
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
    	    // the output stream for writing the file
    	    PrintWriter out = null;

    	    // the input stream for reading from the network
    	    Scanner in = null; 

    	    try
    		{
    			//System.out.println("Connection established");
    			
    			// get the input stream
    			in = new Scanner(socket.getInputStream());

    			// the name of the file should be on the first line
    			String fileName = "_" + in.nextLine();
    			//System.out.println("File is " + fileName);
    			
    			// create the File object
    			File file = new File(fileName);
    			
    			// create the PrintWriter to write to the file
    			out = new PrintWriter(file);

    			// flag to indicate whether we should keep reading
    			boolean keepGoing = true;
    			
    			while (keepGoing)
    			{
    				// read the next line
    				String line = in.nextLine();

    				// TODO: how do we know when we're at the end of the message?
    				if(!line.equals("END"))
    				{
    					// echo it out
    					//System.out.println(line);
    					
    					// write to the file
    					out.println(line);
    					out.flush();
    				}
    				else
    				{
    					// if we're at the end, it's time to stop
    					keepGoing = false;
    				}
    			}
	    
    			// we have the file, now write it to the database using the "XMLLoader"
    			loader.readAndLoad(fileName);
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    		finally
    		{
    			try { out.flush(); } catch (Exception e) { }
    			try { out.close(); } catch (Exception e) { }
    			// try { server.close(); } catch (Exception e) { }
    		}
    	}
    }

}
