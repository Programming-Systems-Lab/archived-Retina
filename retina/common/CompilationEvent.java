package retina.common;
/**
 * 
 * This class represents a single instance of a student trying to compile a file,
 * and whether or not it compiled successfully.
 * 
 * TODO: Conceivably we could add stuff later, like the name of the file.
 *
 */



public class CompilationEvent extends Event 
{
	/**
	 * Whether or not this compilation was successful, i.e. no errors occurred.
	 */
	private boolean success;
	
	public CompilationEvent(String user, String assignment, String time, boolean s)
	{
		super(user, assignment, time);
		success = s;
	}
	
	/**
	 * Indicates whether or not this compilation was successful, i.e. no errors occurred.
	 */
	public boolean isSuccessful()
	{
		return success;
	}

}
