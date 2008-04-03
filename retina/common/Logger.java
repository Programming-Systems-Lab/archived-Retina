package retina.common;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

/**
 * The point of this class is just to have static methods for logging stuff to files.
 *
 */


public class Logger 
{
	/* Names of files */
	private final static String ERROR_LOG = "error.log";
	private final static String WARNING_LOG = "warning.log";
	private final static String INFO_LOG = "info.log";
	
	/* Whether or not to log the different types of messages */
	private static boolean isLogError = true;
	private static boolean isLogWarning = true;
	private static boolean isLogInfo = true;
	
	/**********************************************************
	 * 
	 * Don't touch anything below here!!!!
	 * 
	 **********************************************************/

	
	private static PrintWriter errorLogFile;
	private static PrintWriter warningLogFile;
	private static PrintWriter infoLogFile;
	
	public static boolean isLogError() { return isLogError; }
	public static boolean isLogWarning() { return isLogWarning; }
	public static boolean isLogInfo() { return isLogInfo; }
	
	public static void logError(String msg)
	{
		if (errorLogFile == null)
		{
			// TODO: backup the original
			try { errorLogFile = new PrintWriter(new File(ERROR_LOG)); }
			catch (Exception e) { }
		}
		log(errorLogFile, msg);
	}
	
	public static void logWarning(String msg)
	{
		if (warningLogFile == null)
		{
			// TODO: backup the original
			try { warningLogFile = new PrintWriter(new File(WARNING_LOG)); }
			catch (Exception e) { }
		}
		log(warningLogFile, msg);
	}
	
	public static void logInfo(String msg)
	{
		if (infoLogFile == null)
		{
			// TODO: backup the original
			try { infoLogFile = new PrintWriter(new File(INFO_LOG)); }
			catch (Exception e) { }
		}
		log(infoLogFile, msg);
	}
	
	private static void log(PrintWriter out, String msg)
	{
		Date now = new Date();
		out.println(now + ": " + msg);
		out.flush();
	}
	
}
