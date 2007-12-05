package testplugin.actions;

/**
 * 
 * TODO: why does the "carat" not get put in the write place when writing to the console? 
 */

import java.util.Calendar;
import java.util.Date;
import java.io.File;
import com.sun.tools.javac.Main;

//import javax.sql.rowset.spi.XmlReader;
import javax.swing.JOptionPane;
import java.io.PrintWriter;

public class Compiler {
	public static String compileFile(String javaFile, String displayName)
	{
		XMLWriter out = null;
		//int status = -1;
		String errors = null;
		
		String outputFile = null;
		
		try
		{
			// create the output file

			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			outputFile = System.getProperty("user.name") + "-" + displayName + "-" + (now.get(Calendar.MONTH)+1) + "-" +  now.get(Calendar.DAY_OF_MONTH) + "-" +  now.get(Calendar.HOUR_OF_DAY) + "-" + now.get(Calendar.MINUTE) + "-" + now.get(Calendar.SECOND) + ".xml";

			//JOptionPane.showMessageDialog(null, "output is" + outputFile);
			File file = new File(outputFile);
			out = new XMLWriter(file);

			// write the header
			out.writeHeader();

			// create the arguments necessary for compilation.
			String[] args = new String[1];
			args[0] = javaFile;
			//JOptionPane.showMessageDialog(null, "about to compile " + javaFile);
			// do the compilation
			//Main javac = new Main();
			Main.compile(args, out);
			errors = out.getErrors();
			if (errors == null || errors.equals(""))
				JOptionPane.showMessageDialog(null, "Compiled " + displayName + " successfully.");
			else
				JOptionPane.showMessageDialog(null, "Errors occurred while compiling " + displayName + ". See below.");

			//System.out.println(status);
			//JOptionPane.showMessageDialog(null, "errors: " + errors);
			// write the footer
			out.writeFooter();	
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			errors = "Entered catch. Error at " + e.getMessage() + " cause:" + e.getCause();
			JOptionPane.showMessageDialog(null, "An unexpected error occurred " + e.getMessage() + e.toString());
		}
		finally
		{
			try { out.flush(); } catch (Exception e) { }
			try { out.close(); } catch (Exception e) { }
		}
		
		// now send the file to where it needs to go
		try
		{
			XmlClient client = new XmlClient("localhost", 1234);
			if (client != null) client.readAndSendFile(outputFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return(errors);
	}
	

	
	public static void main(String[] args)
	{
		Compiler.compileFile("D:/Retina/Eclipse plugin/src/testplugin/actions/TestClass.java", "TestClass.java");
	}
}
