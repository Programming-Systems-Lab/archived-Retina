package retina.chat.server;

import java.util.*;
import java.net.*;
import java.io.*;


public class ChatServer {
	public static final String LOG_LOCATION = "./";

    public static final int DEFAULT_PORT =4444;
    public static final String DEFAULT_GROUP = "RETINA HELP";
    private int serverPortNumber;

    Vector users;
    Vector groups;
    Vector sockets;
    Vector messQ;
    HashMap user2Socket;
    HashMap socket2User;
    Handler handler;
    MessageWriter writer;

    File log;
    FileWriter out;

    /** Creates a new instance of LederhosenServer */
    public ChatServer(int serverPortNumber) {
        users = new Vector();
        groups = new Vector();
        sockets = new Vector();
        messQ = new Vector();
        user2Socket = new HashMap();
        socket2User = new HashMap();
        handler = new Handler(this);
        writer = new MessageWriter(this);
        this.serverPortNumber = serverPortNumber;

        log = new File(LOG_LOCATION + System.currentTimeMillis() + "_SYSTEM.log");

	try {
	    log.createNewFile();
	    out = new FileWriter(log, true);

	} catch (FileNotFoundException e){
	    System.out.println("Could not open log file for server");
			System.out.println(e.getMessage());
			out = null;
	} catch (Exception e) {
	    out = null;
	}

	// default group
	groups.add(new Group(DEFAULT_GROUP, null));
    }

    /**
     *  RUNNIT
     */
    public static void main(String[] args){
        String usageStatement = "Usage: java retina.chat.server.ChatServer SERVER_PORT";

        // set port number to default port.  only change it if command-line argument port number read correctly.
        int serverPortNumber = DEFAULT_PORT;
        if (args.length == 1)
        {
            try
            {
                serverPortNumber = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e)
            {
                System.out.println("Error reading specified port number. Trying to listen on the default port (" + DEFAULT_PORT + ").");
            }
        }

        ChatServer L = new ChatServer(serverPortNumber);
        L.run();
    }

    /**
     *  Function:run
     *  Desc: Begin Processing Messages
     */
    public void run(){
        //set up the server socket
        ServerSocket server = null;

        try{
            server = new ServerSocket(serverPortNumber);

			String logStart = handler.getTimestamp() + "Retina ChatServer started!";
			System.out.println(logStart);
			writeLog(logStart);
        }
        catch(BindException be)
        {
            System.out.println("****ERROR: The port number " + serverPortNumber + " is already in use!");
            System.exit(1);
        }
        catch(IOException e){
            System.out.println("****ERROR: An IOException occurred: " + e.toString());
            System.exit(1);
        }
        catch(Exception e){
            System.out.println("*****Error happened in Server");
            e.printStackTrace();
            System.exit(1);
        }


//        System.out.println("Starting Server Socket");
        // set up the socket request listener
        ServerSocketListenerThread sslt = new ServerSocketListenerThread(this, server);
        sslt.start();

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        // start processing messages
        try{
			while(!input.ready() || !input.readLine().equals("quit")){
				Message msg = popQ();
				if (msg != null){
				  //  System.out.println("have a message");
					handler.process(msg);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		String logMessage = handler.getTimestamp() + "ServerShutdown";
		System.out.println(logMessage);
		writeLog(logMessage);

		System.exit(0);
    }

    /**** new users functions ****/

    public void addSocket(Socket s){
        if(s != null)
            sockets.add(s);
    }

    public void addUser(User u, Socket s){
        if(u != null && s != null){
            users.add(u);
            user2Socket.put(u, s);
            socket2User.put(s, u);
        }
    }

    public void addGroup2User(User u, Group g){
        if(u != null && g != null){
            int i = users.indexOf(u);
            ((User) users.elementAt(i)).addGroup(g);
         //   System.out.println("Added group to the user");
        }
    }

    public void addUser2Group(User u, Group g){
         if(u != null && g != null){
            int i = groups.indexOf(g);
            ((Group) groups.elementAt(i)).addUser(u);
        //    System.out.println("Added user to the group");
        }
    }

    public void removeUserFromGroup(User u, Group g){
       if(u != null && g != null){
            int i = users.indexOf(u);
            int j = groups.indexOf(g);

            if(i >= 0 && j >= 0){
//                ((User) users.elementAt(i)).removeGroup(g);
                ((Group) groups.elementAt(j)).removeUser(u);

                if(g.users.size() == 0 && g.getName().equals(DEFAULT_GROUP) == false) {
                    g.closeOutput();
                    groups.removeElementAt(j);
                }
            }
         }
    }

    public void removeUser(Socket s){
        if(s == null) return;

        User u = (User)socket2User.get(s);

        if(u == null) return;

        // remove user from groups
        for(int i = 0; i < u.groups.size(); i++){
            Group g = (Group) u.groups.elementAt(i);
            if(g != null)
                removeUserFromGroup(u, g);
        }

        users.removeElement(u);
        user2Socket.remove(u);
        socket2User.remove(s);
        sockets.remove(s);
    }

    /**** group functions ****/
    public void addGroup(Group g){
        if(g != null)
            groups.addElement(g);
    }

    public Group findGroup(String gName){
        if(gName == null || gName.equals("")) return null;
        for(int i = 0; i < groups.size(); i++){
            if(((Group)groups.elementAt(i)).getName().equals(gName)){
                return (Group) groups.elementAt(i);
            }
        }
        return null;
    }

    public Vector getSockets(Group g){
        if(g == null) return null;

        Vector socks = new Vector();
        for(int i = 0; i < g.users.size(); i++){
            User u  = (User) g.users.elementAt(i);
            socks.addElement((Socket)user2Socket.get(u));
        }
        return socks;
    }

    /***** Message Q functions *****/

    public void queue(Message m){
        if(m != null)
            messQ.addElement(m);
    }

    public Message popQ(){
        if(messQ.isEmpty()) return null;
        else return (Message)messQ.remove(0);
    }

	/** Log methods */

    public void writeLog(String s){
        if (out != null){
            try{
                out.write(s + "\n");
                out.flush();
            } catch (Exception e){
            }
        }
    }

    public void closeOutput(){
        if (out != null){
            try{
                out.close();
            } catch (Exception e) {
            }
        }
    }
}
