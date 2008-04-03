package retina.im;

import java.util.HashMap;

import com.itbs.aimcer.bean.MessageImpl;
import com.itbs.aimcer.bean.Message;
import com.itbs.aimcer.commune.MessageSupport;
import retina.common.CompilationErrorEvent;
import retina.db.DatabaseManager;

/**
 * This class contains methods for sending instant messages to the students "asynchronously",
 * i.e. not responding to messages but rather sending them whenever needed.
 *
 */

public class MessageSender 
{
	// maps RetinaServer usernames to IM connections
	private HashMap<String, MessageSupport> connections = new HashMap<String, MessageSupport>();
	// maps RetinaServer usernames to IM messages (not sure what those are, though...)
	private HashMap<String, Message> messages = new HashMap<String, Message>();
	
	// for accessing the database
	private DatabaseManager manager = new DatabaseManager();

	public void map(String username, MessageSupport connection, Message message)
	{
		connections.put(username, connection);
		messages.put(username, message);
	}
	
    /**
     * This method returns an instant messaging connection to the specified user
     */
    public MessageSupport getConnection(String username)
    {
    	return connections.get(username);
    }

    /**
     * This method returns the IM message used to send something to the user
     */
    public Message getMessage(String username)
    {
    	return messages.get(username);
    }
	
	/**
	 * This method should be called whenever some compilation errors have been processed
	 */
	public void handleCompilationErrorEvents(CompilationErrorEvent[] events)
	{
		// get the username
		String username = events[0].getUser();
		
		// get the stuff we need to send a message
		MessageSupport connection = getConnection(username);
		Message message = getMessage(username);
		
		// only send if we can figure out who the user is, of course
		if (connection != null && message != null)
		{
			String reply = null;
			if (events.length == 0)
			{
				reply = "Compilation successful! Good job!";
				connection.sendMessage(new MessageImpl(message.getContact(), true, reply));
			}
			else
			{
				// generate an appropriate message based on the compilation errors
				if (events.length == 1)
					reply = "You made one compilation error.";
				else
					reply = "You made " + events.length + " compilation errors.";
				connection.sendMessage(new MessageImpl(message.getContact(), true, reply));
				
				/*
				// get the most common error for this assignment
				CompilationErrorEvent error = manager.getMostCommonCompilationError(username, events[0].getAssignment());
				reply = "Your most common error on this assignment is '" + error.getError() + "'";
				connection.sendMessage(new MessageImpl(message.getContact(), true, reply));
				*/
				
				CompilationErrorEvent error = events[0];
				//System.out.println("ERROR IS " + error.getError());
				if (error.getError().equals("cannot find symbol"))
				{
					reply = "You may be forgetting to declare variables or perhaps are just misspelling variable/method names";
					connection.sendMessage(new MessageImpl(message.getContact(), true, reply));
				}
				else if (error.getError().equals("';' expected"))
				{
					reply = "Don't forget to end each line with a semi-colon";
					connection.sendMessage(new MessageImpl(message.getContact(), true, reply));
				}
				
				
			}
		}
	}
	
}
