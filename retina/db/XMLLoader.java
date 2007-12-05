package retina.db;

/**
 * This class contains methods for reading an XML file from disk, creating the necessary Events,
 * and then using the DatabaseManager to insert those into the database.
 */

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import retina.common.CompilationErrorEvent;
import retina.common.CompilationEvent;

public class XMLLoader {

	// the Document object
	private Document dom;

	// the set of objects that need to be inserted into the database
	private ArrayList<CompilationErrorEvent> events;

	// the utility we'll use for writing to the database
	private DatabaseWriter mgr = new DatabaseManager();

	
	/**
	 * This is the starting point for any other object that wants to use this one.
	 * Specify the name of the file to read and then load into the database
	 * @param file The full path to the file
	 */
	public void readAndLoad(String file) {

		// reset the list of events
		events = new ArrayList<CompilationErrorEvent>();
		
		//parse the xml file and get the dom object
		parseXmlFile(file);

		//get each element and create objects
		parseDocument();

		// now write all the compilation error events to the database
		for (CompilationErrorEvent event : events)
		{
			mgr.insertEvent(event);
			//System.out.println("Inserted");
		}


	}


	/**
	 * Helper method to parse the XML file and create the Document object
	 * @param file
	 */
	private void parseXmlFile(String file)
	{
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try 
		{
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(file);

			// System.out.println("Finished with " + file);

		}
		catch(ParserConfigurationException pce) 
		{
			pce.printStackTrace();
		}
		catch(SAXException se) 
		{
			se.printStackTrace();
		}
		catch(IOException ioe) 
		{
			ioe.printStackTrace();
		}
	}


	/**
	 * This method parses the Document object and returns an ArrayList of objects within.
	 */
	private void parseDocument(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();

		//get a nodelist of <metric> elements
		NodeList nl = docEle.getElementsByTagName("metric");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the element
				Element el = (Element)nl.item(i);
				// create the objects and put them in the "events" ArrayList
				createObjects(el);
			}
		}

	}


	/**
	 * This method creates objects from the Element and then stores them in the
	 * ArrayList. It's usually not good to have a "side effect" of altering the state of
	 * an argument (in this case, the ArrayList), but... eh? What are ya gonna do?
	 */
	private void createObjects(Element empEl) {

		String hour="0";
		String minutes="0";
		String seconds="0";
		String year="0";
		String month="0";
		String day="0";

		NodeList nl = empEl.getElementsByTagName("time");
		if(nl != null && nl.getLength() > 0) {
				Element e = (Element)nl.item(0);
				year = getTextValue(e, "year");
				month = getTextValue(e, "month");
				day = getTextValue(e, "day");
				hour = getTextValue(e, "hour");
				minutes = getTextValue(e, "minute");
				seconds = getTextValue(e, "second");

				/*
				System.out.println("year " + year);
				System.out.println("month " + month);
				System.out.println("day "+ day);
				System.out.println("hours " + hour);
				System.out.println("mins " + minutes);
				System.out.println("secs " + seconds);
				*/
		}

		String timeFormat = month + "-" + day + "-" + year.substring(2) + "-" + hour +":" + minutes +":"+ seconds;
		//System.out.println(timeFormat);


		// get the username
		String user = "unknown";
		NodeList nl2 = empEl.getElementsByTagName("user");
		if(nl2 != null && nl2.getLength() > 0) {
				Element e = (Element)nl2.item(0);
				user = e.getAttribute("name");

				//System.out.println(user);
		}

		// get the assignment number
		String assignment = "unknown";
		NodeList nl3 = empEl.getElementsByTagName("assignment");
		if(nl3 != null && nl3.getLength() > 0) {
				Element e = (Element)nl3.item(0);
				assignment = e.getAttribute("name");

				//System.out.println(assignment);
		}


		// records whether or not this was a successful compilation
		boolean success = true;

		nl = empEl.getElementsByTagName("measure");
		if(nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++)
			{
				Element e = (Element)nl.item(i);
				String error = getTextValue(e, "error");
				if (error != null) error = error.trim();
				String message = getTextValue(e, "message");
				if (message != null) message = message.trim();

				//System.out.println("error: " + error.trim());
				//System.out.println("message: " + message.trim());

				CompilationErrorEvent errComp = new CompilationErrorEvent(user, assignment, timeFormat, error, message);
				events.add(errComp);

				success = false;
			}

		}

		// now record the fact that a compilation occurred
		CompilationEvent ce = new CompilationEvent(user, assignment, timeFormat, success);
		mgr.insertEvent(ce);

	}


	/**
	 * Take a xml element and the tag name, look for the tag and get
	 * the text content
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}


	/**
	 * Calls getTextValue and returns a int value
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}


	/* Only use this as a standalone method. It currently reads all files from a directory. */
	public static void main(String[] args)
	{
		/* 
		// this is for reading one file at a time
		String filename = null;
		if (args.length == 0)
		{
			System.out.println("Please specify an XML file to read.");
			Scanner scan = new Scanner(System.in);
			filename = scan.nextLine();
		}
		else
			filename = args[0];
		 */
		
		// to read an entire directory
		String dirname = null;
		if (args.length == 0)
		{
			System.out.println("Please specify a directory to read.");
			Scanner scan = new Scanner(System.in);
			dirname = scan.nextLine();
		}
		else
			dirname = args[0];
		
		try
		{
			File dir = new File(dirname);

			if (dir.isDirectory())
			{
				File[] files = dir.listFiles();

				//create an instance
				XMLLoader loader = new XMLLoader();
				
				for (File file : files)
				{
					// read each file
					String filename = file.getAbsolutePath();
					// only care about xml files, of course
					if (filename.contains(".xml"))
					{
						System.out.println("Reading " + filename);
						loader.readAndLoad(filename);
					}
				}
			}
			else
				System.out.println(dir + " is not a directory");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}


