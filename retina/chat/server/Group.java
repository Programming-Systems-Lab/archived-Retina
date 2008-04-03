package retina.chat.server;

import java.util.*;
import java.io.*;

public class Group {
    String groupName;
    Vector users;
    File log;
    FileWriter out;

    /** Creates a new instance of User */
    public Group(String name, User firstUser) {
        groupName = name;
        users = new Vector();
	if (firstUser != null)
	{
	    users.addElement(firstUser);
	    firstUser.addGroup(this);
	}

        log = new File(ChatServer.LOG_LOCATION + System.currentTimeMillis() + "_" +  groupName + ".log");
        try {
            log.createNewFile();
            out = new FileWriter(log, true);

        } catch (FileNotFoundException e){
            System.out.println("Could not open log file for group: " + groupName);
            System.out.println(e.getMessage());
            out = null;
        } catch (Exception e) {
            out = null;
        }

    }

    /**
     *  Function: getName
     *  Desc: returns the usr name
     */
    public String getName(){
        return groupName;

    }

    /**
     *  Function: inGroup
     *  Desc: returns if the user is in a group
     */


    public boolean isInGroup(User u){
        if(u != null)
            return users.contains(u);

        return false;
    }

    /**
     *  Function: addGroup
     *  Desc: adds a group to the user group vector
     */
    public void addUser(User u){
        if(u != null && !users.contains(u))
            users.addElement(u);
    }

    /**
     *  Function: removeGroup
     *  Desc: remove a user from a group
     */
    public void removeUser(User u){
        if(u != null && users.contains(u)){
            users.removeElement(u);
        }
    }

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
