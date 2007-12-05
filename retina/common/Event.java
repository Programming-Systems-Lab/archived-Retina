package retina.common;
/***********************************************
 *
 * This class is the base class for representing one event in the system.
 * It is abstract because we should never create just an "event", it should
 * always be a specific type of event.
 *
 ***********************************************/
public abstract class Event
{
	/**
	 * The user id.
	 */
	private String user;
	/**
	 * The assignment.
	 */
	private String assignment;
	/**
	 * The time in MM-DD-YY-hh:mm:ss format.
	 * TODO: we may want to use a Date object instead
	 */
	private String time;
	public Event(String user, String assignment, String time)
	{
		this.user = user;
		this.assignment = assignment;
		this.time = time;
	}
	/**
	 * Returns the value of the user id.
	 */
	public String getUser()
	{
		return user;
	}
	/**
	 * Returns the value of the assignment id.
	 */
	public String getAssignment()
	{
		return assignment;
	}
	/**
	 * Returns the value of the event time.
	 */
	public String getTime()
	{
		return time;
	}
}