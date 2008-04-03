package retina.chat.server;

import java.util.*;

public class User {
    String userName;
    Vector groups;

    /** Creates a new instance of User */
    public User(String name) {
        userName = name;
        groups = new Vector();
    }

    /**
     *  Function: getName
     *  Desc: returns the usr name
     */
    public String getName(){
        return userName;
    }

    /**
     *  Function: inGroup
     *  Desc: returns if the user is in a group
     */
    public boolean isInGroup(Group group){
        if(group != null)
            return groups.contains(group);

        return false;
    }

    /**
     *  Function: addGroup
     *  Desc: adds a group to the user group vector
     */
    public void addGroup(Group group){
        if(group != null && !groups.contains(group)){
            groups.addElement(group);
        }
    }

    /**
     *  Function: removeGroup
     *  Desc: remove a user from a group
     */
    public void removeGroup(Group group){
        if(group != null && groups.contains(group)){
            groups.removeElement(group);
        }
    }

    public String toString()
    {
	return userName;
    }
}
