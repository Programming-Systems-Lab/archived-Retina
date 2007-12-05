package testplugin.actions;

/**
 * TODO: why does the error popup not appear when it can't connect to the server?
 * TODO: handle errors more gracefully if something happens while reading/sending the file
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class XmlClient
{
    // the output stream for writing to the server
    private PrintWriter out;

    /**
     * Creates a new object, which connects to the server
     */
    public XmlClient(String host, int port)
    {
		try
		{
		    Socket connect = new Socket(host, port);

		    // get the output stream
		    out = new PrintWriter(connect.getOutputStream());
		}
		catch (Exception e)
		{
		    System.out.println("Cannot create XmlClient!");
			JOptionPane.showMessageDialog(null, "An unexpected error occurred " + e.getMessage() + e.toString());
		    e.printStackTrace();
		}
    }


    /**
     * Reads the file from the disk and then sends it to the server
     * @param filename the full path to the file to send to the server
     */
    public void readAndSendFile(String filename)
    {
		try
		{
		    // create a File object
		    File file = new File(filename);

		    // create a Scanner
		    Scanner scan = new Scanner(file);

			// this is the string we'll send to the server
			// it starts with the filename in the first line
		   	String fileContent=filename;

		    // keep looping as long as there is something to read
		    while (scan.hasNext())
		    {
				// read one line of the file
				String line = scan.nextLine();

				// append the lines read from the file together
				//System.out.println(line);

				fileContent = fileContent + "\n" + line;
		    }

		    //signifies we're done reading the file
		    String line = "END";
		    fileContent = fileContent + "\n" + line;

			// now write the contents of the file to the network connection
			//System.out.println(fileContent);
			out.write(fileContent + "\n");
			out.flush();
		}
		catch (FileNotFoundException e)
		{
		    System.out.println("That file doesn't exist");
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
		finally
		{
			try { out.close(); } catch (Exception e) { }
		}

    }

    /* 
     * The main method of the client - for running it as a standalone app only 
     * 
     */
    public static void main(String[] args)
    {
		if (args.length < 3)
		{
		    System.out.println("Please specify the host name, port number and file name!");
		    System.exit(0);
		}

		String host = args[0];
		int port = Integer.parseInt(args[1]);
		XmlClient xc = new XmlClient(host, port);
		String filename = args[2];
	    xc.readAndSendFile(filename);
    }

    
}
