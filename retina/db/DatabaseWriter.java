package retina.db;

/**
 * Defines methods that we currently use for writing to the database.
 */


import retina.common.Event;

public interface DatabaseWriter
{	/**	 * This method inserts an event into the appropriate	 * table in the database. If successful, it returns true.	 * If an error occurs, it returns false.	 */	public boolean insertEvent(Event e);
}