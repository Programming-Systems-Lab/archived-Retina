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

public class XmlServer
{
    // the output stream for writing the file
    private PrintWriter out;

    // the input stream for reading from the network
    private Scanner in;

    // the server
    private ServerSocket server;

    // the tool for writing to the database
    private XMLLoader reader = new XMLLoader();

    /* This is the main method for starting the Server */
    public static void main(String[] args)
    {
		if (args.length < 1)
		{
		    System.out.println("Please specify a port number!");
		    System.exit(0);
		}

		int port = Integer.parseInt(args[0]);
		XmlServer xs = new XmlServer(port);
		xs.run();
    }

    /**
     * Creates a socket server listening on the specified port
     * @param port the port to listen to
     */
    public XmlServer(int port)
    {
		try
		{
		    server = new ServerSocket(port);
		    System.out.println("Server started... waiting for connection");
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
    			System.out.println("Connection established");

    			// get the input stream
    			in = new Scanner(socket.getInputStream());

    			// the name of the file should be on the first line
    			String fileName = "_" + in.nextLine();
    			System.out.println("File is " + fileName);

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
    			reader.readAndLoad(fileName);
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
