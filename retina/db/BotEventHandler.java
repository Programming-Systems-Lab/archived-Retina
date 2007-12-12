package retina.db;
import java.util.Date;
import java.util.HashMap;
import com.itbs.aimcer.bean.Contact;
import com.itbs.aimcer.bean.Message;
import com.itbs.aimcer.bean.MessageImpl;
import com.itbs.aimcer.bean.Nameable;
import com.itbs.aimcer.commune.Connection;
import com.itbs.aimcer.commune.ConnectionEventListener;
import com.itbs.aimcer.commune.FileTransferSupport;
import com.itbs.aimcer.commune.IconSupport;
import com.itbs.aimcer.commune.MessageSupport;
import com.itbs.util.GeneralUtils;
import retina.common.CompilationErrorEvent;
import retina.common.Student;

public class BotEventHandler implements ConnectionEventListener {
	
	private DatabaseManager dbmanager = new DatabaseManager();
	private SuggestionManager smanager = new SuggestionManager();
	private MessageSender sender;
	
	// maps IM handles to Retina usernames
	private HashMap<String, String> names = new HashMap<String, String>();

	
	/**
	 * Public constructor is used to initialize the reference to the MessageSender
	 */
	public BotEventHandler(MessageSender ms)
	{
		sender = ms;
	}
	
	/**
	 * No-argument constructor probably shouldn't be used, otherwise it can't share
	 * the MessageSender
	 */
	public BotEventHandler()
	{
		sender = new MessageSender();
	}
	
	
    public boolean messageReceived(MessageSupport connection, Message message) {
        if (message.isOutgoing()) // Not doing anything with outgoing messages
            return true;
        try {
        	// lookup the student's username based on the contact name
        	String username = names.get(message.getContact().getName());
        	
            String line = GeneralUtils.stripHTML(message.getText());
            //System.out.println(message.getContact().getName()+ " says: " + message.getPlainText());
            
            String reply = "";
            if (line.startsWith("status")) 
            {
            	reply = "up! " + new Date();
            } 
            else if (line.equals("help"))
            {
            	reply = "Here are the commands I currently understand: \n\r";
            	reply += "status: get the current date/time\n\r";
            	reply += "shutdown: shut down the server\n\r";
            	reply += "errors [assignment]: list all your errors for the specified assignment\n\r";
            	reply += "explain [error]: explain what the error means\n\r";
            	reply += "suggest friend: suggest the name of a student with similar errors\n\r";
            	reply += "login [username]: log in to the system with your username";

            }
            else if (line.equals("shutdown")) 
            {
                    connection.sendMessage(new MessageImpl(message.getContact(), true, "shutting down."));
                    Thread.sleep(1000); // Let it finalize communications.
                    connection.disconnect(true);
                    System.exit(1);
                    
            }
            else if (line.startsWith("errors"))
            {
        		String assignment = line.split(" ")[1];

            	CompilationErrorEvent[] errors = dbmanager.getCompilationErrorEvents(username, assignment);

        		if (errors == null || errors.length == 0)
            	{
            		reply = "You have made no compilation errors on assignment #" + assignment;
            	}
            	else if (errors.length == 1)
            	{
            		reply = "The only compilation error on assignment #" + assignment + " was " + errors[0]; 
            	}
            	else
            	{
            		reply = "You have made " + errors.length + " errors on assignment #" + assignment + ".\n\r";
            		reply += "The most common error is " + dbmanager.getMostCommonCompilationError(username, assignment).getError();
            	}
            }
            else if (line.startsWith("explain"))
            {
            	String error = line.replaceFirst("explain", "").trim();
            	if (error.equals("; expected")) 
            	{
            		reply = "That means you forgot to put a semicolon at the end of a line";
            	}
            	else if (error.equals("} expected"))
            	{
            		reply = "You probably forgot to put a curly brace at the end of a method";
            	}
            	else reply = "I'm sorry, I'm not sure what " + error + " means";
            }
            else if (line.equals("suggest friend"))
            {
            	Student user = smanager.suggestFriend(username);
            	if (user == null) reply = "I'm sorry, I don't have any suggestions for you";
            	else reply = user.getName() + " (" + user.getUni() + ") seems to be someone who is a similar programmer to you";
            }
            else if (line.contains("fuck"))
            {
            	reply = "You kiss your mother with that mouth?";
            }
            else if (line.startsWith("login"))
            {
            	username = line.split(" ")[1];
            	
            	// see if it's a valid username
            	String name = dbmanager.getStudentName(username);
            	if (name != null)
            	{
            		// map the IM name to the username
            		names.put(message.getContact().getName(), username);
            		// map the username to the IM connection
            		sender.map(username, connection, message);
            	
            		// pull out the first name, instead of the whole name
            		name = name.split(" ")[0];
            		reply = "Hello, " + name + ", welcome to Retina Help.";
            	}
            	else
            	{
            		reply = "I'm sorry, I don't recognize that username";
            	}
            }
            else if (line.startsWith("hello"))
            {
            	if (username == null)
            	{
            		reply = "Hello, " + message.getContact().getName() + ", it's nice to meet you. Use the \"login\" command to log in";
            	}
            	else
            	{
            		reply = "Hello, " + username + ", let me know if I can be of any help.";
            	}
            }
            else 
            {
            	reply = "I'm sorry, I don't understand '" + line + "'\nType 'help' if you need help";
            }
            
            // now send the reply
            connection.sendMessage(new MessageImpl(message.getContact(), true, reply));
            
           
        } 
        catch (InterruptedException e) 
        {
            System.out.println("Failed while processing a message.");
            e.printStackTrace();
        }
        return true;
    } 


    
	public void connectionEstablished(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void connectionFailed(Connection arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void connectionInitiated(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void connectionLost(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean contactRequestReceived(String arg0, MessageSupport arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean emailReceived(MessageSupport arg0, Message arg1) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public void errorOccured(String arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	public void fileReceiveRequested(FileTransferSupport arg0, Contact arg1, String arg2, String arg3, Object arg4) {
		// TODO Auto-generated method stub
		
	}

	public void pictureReceived(IconSupport arg0, Contact arg1) {
		// TODO Auto-generated method stub
		
	}

	public void statusChanged(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void statusChanged(Connection arg0, Contact arg1, boolean arg2, boolean arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	public void typingNotificationReceived(MessageSupport arg0, Nameable arg1) {
		// TODO Auto-generated method stub
		
	}
}