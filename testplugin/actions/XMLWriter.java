/**
 * Writes out to the XML file.
 * 
 * @author - Chris Murphy
 */
package testplugin.actions;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class XMLWriter extends PrintWriter {
	StringBuffer errorBfr;
	public XMLWriter(File file) throws FileNotFoundException
    {
	super(file);
	errorBfr = new StringBuffer();
    }

	/**
	 * Getter
	 * @return a string containing all the errors encountered.
	 */
	public String getErrors() {
		return(errorBfr.toString());
	}
	
	// whether or not we're currently writing out an error message
    private boolean writing = false;
    
    // a counter to use when we're skipping lines
    private int counter = 0;
    
	/**
	 * Writes to the xml file
	 * @params s the string to be written to the file
	 */
    public void println(String s)
    {
	// TODO: how do we know which line to write?
	if (s.contains(".java:"))
	{
		// this is kind of a hack but we want to extract the name of the file and such and just get the error
		// we're assuming that the error message starts after the third colon (but only on Windows!!!)  
		int index = s.indexOf(':');
		String error = s.substring(index+1);
		index = error.indexOf(':');
		error = error.substring(index+1);
		index = error.indexOf(':');
		error = error.substring(index+1).trim();
		//System.out.println(error);
		
		// if it's a problem with something already being defined, just extract that
		if (error.contains("already defined")) error = "already defined";
		// if they're trying to access a non-static thing from a static method
		else if (error.contains("static context"))
		{
			if (error.contains("non-static method")) error = "non-static method cannot be referenced from a static context";
			else if (error.contains("non-static variable")) error = "non-static variable cannot be referenced from a static context";
		}


		// this is where we write the actual error message
	    super.println("\n\t\t<measure name=\"errors\">");
	    super.println("\t\t\t<error>");
	    super.println("\t\t\t\t" + error);
	    super.println("\t\t\t</error>");	    

	    // indicate that we're currently writing an error
	    writing = true;
	    
	    // to skip two lines when there's an unexpected type... this will print the line of code
	    if (error.equals("unexpected type")) counter = 2;

	}
	else if (writing)
	{
		if (counter == 0)
		{
			// now we write the message itself
			super.println("\t\t\t<message>");
			super.println("\t\t\t\t" + s);
			super.println("\t\t\t</message>");
			super.println("\t\t</measure>");
			// and we're done
			writing = false;
		}
		else
			counter--;
	}
	
	// we want EVERYTHING printed to the view.
	errorBfr.append(s);
	errorBfr.append(System.getProperty("line.separator"));
	//System.out.println(s);
    }
    
    /**
     * Writes a header to the XML file.
     *
     */
    public void writeHeader()
    {
	super.println("<measurements>");
	super.println("\t<metric name=\"compilation_errors\" measure=\"true\">");
	super.println("\t\t<user name=\"" + System.getProperty("user.name") + "\"/>");
	super.println("\t\t<time>");
	Calendar now = Calendar.getInstance();
	now.setTime(new Date());
	super.println("\t\t\t<year>" + now.get(Calendar.YEAR) + "</year>");
	super.println("\t\t\t<month>" + (now.get(Calendar.MONTH)+1) + "</month>");
	super.println("\t\t\t<day>" + now.get(Calendar.DAY_OF_MONTH) + "</day>");
	super.println("\t\t\t<hour>" + now.get(Calendar.HOUR_OF_DAY) + "</hour>");
	super.println("\t\t\t<minute>" + now.get(Calendar.MINUTE) + "</minute>");
	super.println("\t\t\t<second>" + now.get(Calendar.SECOND) + "</second>");
	super.println("\t\t</time>");
    }

    /**
     * Writes a footer to the XML file.
     *
     */
    public void writeFooter()
    {
	super.println("\n\t</metric>");
	super.println("</measurements>");
    }
}
