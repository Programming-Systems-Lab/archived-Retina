package retina.chat.server;

import retina.chat.common.Packet;
import java.util.*;
import java.net.*;
import java.text.*;


public class Handler {

    private ChatServer server;

    /** Creates a new instance of Handler */
    public Handler(ChatServer server) {
        this.server = server;
    }

    /** The main access to the Handler class.  This method is called with every
	 *  message that is passed to the server
	 */
	public boolean process(Message message){
        if (message == null) return false;

		Packet packet = message.getPacket();

		if (packet == null) return false;

        switch (packet.getMessageType())	{
			case Packet.MSGTYPE_SIGNON:
				//System.out.println("received a sign on message");
				//parent.getAuthenticationManager().handleSignInMessage(packet);
				signOnServerHandler(message);
				break;
			case Packet.MSGTYPE_CREATE_NEW_GROUP:
				//System.out.println("received a create new group message");
				//  parent.getGroupManager().handleCreateAndJoinNewGroupMessage(packet);
				createGroupHandler(message);
				break;
			case Packet.MSGTYPE_JOIN_GROUP:
				//System.out.println("received a join group message");
			    //parent.getGroupManager().handleJoinGroupMessage(packet);
				joinGroupHandler(message);
				break;
			case Packet.MSGTYPE_LEAVE_GROUP:
				//System.out.println("received a join group message");
			    //parent.getGroupManager().handleJoinGroupMessage(packet);
				leaveGroupHandler(message);
				break;
			case Packet.MSGTYPE_SIGN_OUT:
				//System.out.println("received a join group message");
			    //parent.getGroupManager().handleJoinGroupMessage(packet);
				signOutServerHandler(message);
				break;
			case Packet.MSGTYPE_CHAT_MESSAGE:
				//System.out.println("received a join group message");
			    //parent.getGroupManager().handleJoinGroupMessage(packet);
			    	if (message.getPacket().getGroupname().equals(server.DEFAULT_GROUP))
				    specialMessageHandler(message);
				else
				    // the default... it's just a regular old chat message
				    chatMessageHandler(message);
				break;
			default:
				System.out.println("received some unknown type of packet");
				System.out.println("Message type: " + packet.getMessageType());
				break;
		}
        return true;
		//debugUser();
		//debugGroups();
    }

    /**
	 * Processes a message of type Packet.MSGTYPE_SIGNON
	 */
	public boolean signOnServerHandler(Message message){
        String userName = message.getPacket().getUsername();
		//System.out.println("username: " + userName);

		Socket socket = message.getSocket();

        if (userName == null || userName.equals("")){
            server.writer.signOnFailed(socket, Packet.ERROR_USER_EMPTY);
            System.out.println(getTimestamp() + "SignOnFailed: Empty username");
			return false;
        }

        for (int i = 0; i < server.users.size(); i++){
            if (((User)server.users.get(i)).getName().equals(userName)){
                server.writer.signOnFailed(socket, Packet.ERROR_USER_EXISTS);
                System.out.println(getTimestamp() + "SignOnFailed: Username " + userName + " already in use");
                return false;
            }
        }

        // add the user to the user list and set up the hashmap.
        User newUser = new User(userName);
        server.addUser(newUser, socket);

        // send OK back to user
        server.writer.signOnOK(socket, server.groups);

		String logMessage = getTimestamp() + "SignOnOK: " + userName;
        System.out.println(logMessage);
		server.writeLog(logMessage);
        return true;
    }


    /**
	 * Processes a message of type Packet.MSGTYPE_CREATE_NEW_GROUP
	 */
    public boolean createGroupHandler(Message message){
        String groupName = message.getPacket().getGroupname();
		//System.out.println("new Group: " + groupName);

		Socket socket = message.getSocket();

        if (groupName == null || groupName.equals("")){
            server.writer.openGroupFailed(socket, Packet.ERROR_GROUP_EMPTY);
            System.out.println(getTimestamp() + "OpenGroupFailed: Empty groupname");
            return false;
        }

        Group group = server.findGroup(groupName);
        if (group != null){
          server.writer.openGroupFailed(socket, Packet.ERROR_GROUP_EXISTS);
          System.out.println(getTimestamp() + "OpenGroupFailed: Groupname " + groupName + " already in use");
          return false;
        }

        //add the group to the group list
        User user = (User)server.socket2User.get(socket);
        // System.out.println("username: " + u.getName());
        Group newGroup = new Group(groupName, user);
        server.addGroup(newGroup);
        server.addGroup2User(user, newGroup);

        // send back to the user
        server.writer.openGroupOK(socket, groupName);
        server.writer.broadcastNewGroup(server.sockets, groupName);

        String logMessage = getTimestamp() + "OpenGroupOK: " + groupName;
        System.out.println(logMessage);
        newGroup.writeLog(logMessage);
		server.writeLog(logMessage);
        return true;
    }

    /**
	 * Processes a message of type Packet.MSGTYPE_JOIN_GROUP
	 */
    public boolean joinGroupHandler(Message message){
        String groupName = message.getPacket().getGroupname();
		//System.out.println("groupName: " + groupName);

		Socket socket = message.getSocket();

        if (groupName == null || groupName.equals("")){
            server.writer.joinGroupFailed(socket, Packet.ERROR_GROUP_EMPTY);
            System.out.println(getTimestamp() + "JoinGroupFailed: Empty groupname");
            return false;
        }

        // group does not exist
        Group group = server.findGroup(groupName);
        if (group == null){
          server.writer.joinGroupFailed(socket, Packet.ERROR_GROUP_DOES_NOT_EXIST);
          System.out.println(getTimestamp() + "JoinGroupFailed: Group " + groupName + " does not exist");
          return false;
        }

        User user = (User)server.socket2User.get(socket);

		if (user == null) return false;

		String userName = user.getName();

        //user already in group
        if (group.isInGroup(user)){
          server.writer.joinGroupFailed(socket, Packet.ERROR_ALREADY_IN_GROUP);
          System.out.println(getTimestamp() + "JoinGroupFailed: User " + userName + " is already in group " + groupName);
          return false;
        }

        //System.out.println("userName: " + u.getName());
        server.addUser2Group(user, group);
        server.addGroup2User(user, group);

        //group = server.findGroup(groupName);

        // send back to the user
        server.writer.joinGroupOK(socket, group.users, user, groupName);
        server.writer.broadcastNewJoin(server.getSockets(group), groupName,  userName);

        String logMessage = getTimestamp() + "JoinGroupOK: " + groupName + ", " + userName;
        System.out.println(logMessage);
        group.writeLog(logMessage);
        return true;
    }

    /**
	 * Processes a message of type Packet.MSGTYPE_LEAVE_GROUP
	 */
    public boolean leaveGroupHandler(Message message){
        String groupName = message.getPacket().getGroupname();
		//System.out.println("Leaving Group: " + groupName);

		Socket socket = message.getSocket();

		//System.out.println("username: " + u.getName());
        Group group = server.findGroup(groupName);

        if (groupName == null || groupName.equals("") || group == null){
            //server.writer.leaveGroupFailed(m.getSocket(), Packet.ERROR_GROUP_EMPTY);
            System.out.println(getTimestamp() + "LeaveGroupFailed: Groupname empty");
            return false;
        }

        User user = (User)server.socket2User.get(socket);

        boolean shouldDestroyGroup = (group.users.size() == 1) && (groupName.equals(server.DEFAULT_GROUP) == false);

        server.writer.leaveGroupOK(socket, groupName);
        server.writer.broadcastLeaveGroup(server.getSockets(group), groupName, user.getName());

        if (shouldDestroyGroup){
            server.writer.broadcastRemoveGroup(server.sockets, groupName);
        }

        String logMessage = getTimestamp() + "LeaveGroupOK: " + groupName + ", " + user.getName();
        System.out.println(logMessage);
        group.writeLog(logMessage);

        if (shouldDestroyGroup){
			String closeMessage = getTimestamp() + "GroupClosed: " + groupName;
			System.out.println(closeMessage);
			server.writeLog(closeMessage);
			group.writeLog(closeMessage);
		}

        server.removeUserFromGroup(user, group);

        return true;

    }

    /**
	 * Processes a message of type Packet.MSGTYPE_SIGN_OUT
	 */
    public boolean signOutServerHandler(Message message){
		//String userName = message.getPacket().getUsername();
		//System.out.println("Leaving program: " + userName);

/*        if (userName == null || userName.equals("")){
            System.out.println(getTimestamp() + "SignOutFailed: Username empty");
            return false;
        }
*/
		Socket socket = message.getSocket();

        User user = (User)server.socket2User.get(socket);

        if (user == null){
            System.out.println(getTimestamp() + "SignOutFailed: User empty");
            return false;
        }

        String userName = user.getName();

        if (userName == null || userName.equals("")){
            System.out.println(getTimestamp() + "SignOutFailed: Username empty");
            return false;
        }

        // remove the user from each of its groups
        for(int i = 0; i < user.groups.size(); i++){
            Group group = (Group) user.groups.elementAt(i);

            String logMessage = getTimestamp() + "LeaveGroupOK: " + group.getName() + ", " + userName;
            group.writeLog(logMessage);
            System.out.println(logMessage);

            server.writer.leaveGroupOK(socket, group.getName());
            server.writer.broadcastLeaveGroup(server.getSockets(group), group.getName(), userName);

	    if (group.users.size() == 1 && group.getName().equals(server.DEFAULT_GROUP) == false) {
				server.writer.broadcastRemoveGroup(server.sockets, group.getName());
				String closeMessage = getTimestamp() + "GroupClosed: " + group.getName();
				System.out.println(closeMessage);
				server.writeLog(closeMessage);
				group.writeLog(closeMessage);
			}
        }

		//server.writer.signOffOK(socket);
        server.removeUser(socket);

		String logMessage = getTimestamp() + "SignOutServerOK: " + userName;

        System.out.println(logMessage);
		server.writeLog(logMessage);

        return true;
    }



    /**
     * This is for handling a chat message that is to be broadcast to the whole group.
     */
    private boolean chatMessageHandler(Message message){
        String groupName = message.getPacket().getGroupname();
        String chatMessage = message.getPacket().getChatMessage();
	//System.out.println("Group: " + groupName);
        //System.out.println("Chat Message: " + chatMessage);

	//
	Socket socket = message.getSocket();

        if (groupName == null || groupName.equals("") || chatMessage == null || chatMessage.equals("")){
            return false;
        }

        User user = (User)server.socket2User.get(socket);
		//System.out.println("username: " + u.getName());
        Group group = server.findGroup(groupName);

        server.writer.broadcastChatMessage(server.getSockets(group), chatMessage, groupName, user.getName());

        String logMessage = getTimestamp() + "ChatMessage: " + groupName + ", " + user.getName() + " :: " + chatMessage;
        System.out.println(logMessage);
        group.writeLog(logMessage);

        return true;
    }

    /**
     * This method is for handling any special request. The "message" parameter contains everything
     * for sending back the response; the "msg" is the text that the user typed.
     */
    private boolean specialMessageHandler(Message message)
    {
	// don't change this part!!
	Packet packet = new Packet(Packet.MSGTYPE_BROADCAST_CHAT_MESSAGE);
	packet.setUsername("RETINA");
	packet.setGroupname(server.DEFAULT_GROUP);

	// get the message
	String chatMessage = message.getPacket().getChatMessage();
       	// strip out the HTML parts
	String sub = chatMessage.substring(chatMessage.indexOf(">")+1, chatMessage.length());
	String msg = sub.substring(0, sub.indexOf("<"));

	String reply = null;
	// this is the part that figures out what to send back
	if (msg.equals("help"))
	    reply = "Do you need help?";
	else if (msg.equals("who"))
	{
	    reply = "Here are the people currently logged in<br>";
	    for (Object user : server.users)
		reply += user + "<br>";
	}
	else if (msg.startsWith("broadcast"))
	{
	    msg = msg.substring(10, msg.length());
	    message.getPacket().setChatMessage(msg);
	    return chatMessageHandler(message);
	}
	else reply = "I'm sorry, I don't understand " + msg;

	// don't change this part!!
	packet.setChatMessage(reply);
	server.writer.writeOut(message.getSocket(), packet);

	// TODO: add a log message

	return true;
    }



    /**
     * Prints out debugging info about users in the system
     */
    public void debugUser(){
        System.out.println("System Users #" + server.users.size());
        System.out.println("num sockets: " + server.sockets.size());
        for(int i = 0; i < server.users.size(); i++){
            User user = (User)server.users.elementAt(i);
            System.out.println("user" + i + ": " + user.getName());
        }
        System.out.println("");
    }


    /**
	 * Prints out debugging info about groups in the system
	 */
    public void debugGroups(){
        System.out.println("System groups #" + server.groups.size());
        for(int i = 0; i < server.groups.size(); i++){
            Group group = (Group)server.groups.elementAt(i);
            System.out.println("Group Name: " + group.getName());
            for(int j = 0; j < group.users.size(); j++){
                User user = (User)group.users.elementAt(j);
                System.out.println("    user" + j + ": " + user.getName());
            }
            System.out.println("");
        }
    }

    /**
	 * Creates a time stamp string
	 */
    public String getTimestamp(){
        Calendar time = Calendar.getInstance();

        int year = time.get(Calendar.YEAR);
        int month = time.get(Calendar.MONTH) + 1;
        int day = time.get(Calendar.DAY_OF_MONTH);
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);
        int second = time.get(Calendar.SECOND);

		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMinimumIntegerDigits(2);

        return (year + "/" + formatter.format((long)month) + "/" + formatter.format((long)day) + "  " + formatter.format((long)hour) + ":" + formatter.format((long)minute) + ":" + formatter.format((long)second) + "\t");
    }
}
