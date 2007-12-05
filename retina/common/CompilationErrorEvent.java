package retina.common;
/***********************************************
 *
 * This class represents one compilation error recorded by the system.
 *
 ***********************************************/
public class CompilationErrorEvent extends Event
{
	/**
	 * The actual error represented by this event. Something like '; expected'. 
	 */
	private String error;
	
	/**
	 * The message received by the compilation error. This would provide additional
	 * information beyond just what the error itself is.
	 */
	private String message;
	public CompilationErrorEvent(String user, String assignment, String time, String err, String msg)
	{
		super(user, assignment, time);
		error = err;
		message = msg;
	}
	public String getError()
	{
		return error;
	}
	
	public String getMessage()
	{
		return message;
	}
	
}
