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
	 * The number of errors made during this compilation attempt.
	 */
	private int errors;
	
	public CompilationEvent(String user, String assignment, String time, int e)
	{
		super(user, assignment, time);
		errors = e;
	}
	
	/**
	 * Indicates whether or not this compilation was successful, i.e. no errors occurred.
	 */
	public boolean isSuccessful()
	{
		return errors == 0;
	}
	
	/**
	 * Returns the number of errors that were made.
	 */
	public int getErrors()
	{
		return errors;
	}

	/**
	 * Prints out the value of this object
	 */
	public String toString()
	{
		return user + " at " + time + " " + errors + " errors";
	}
}
