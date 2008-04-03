package retina.chat.client;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;

import retina.chat.common.Packet;

public class GroupManager
{
    private ChatClient parent;

    private Vector groupList;

    private GroupWindow groupWindow;

    /*
     * key: groupname
     * value: Vector of usernames of members of the group
     */
    private Hashtable groupMemberLists; // for groups that the user is in

    /** Creates a new instance of GroupManager */
    public GroupManager(ChatClient chatClient)
    {
        parent = chatClient;
        groupMemberLists = new Hashtable();
        groupList = new Vector();
        groupWindow = new GroupWindow(parent);
    }


    public void createAndJoinNewGroup(String groupname)
    {
        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while creating a new group. The group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (groupList.contains(groupname))
        {
            JOptionPane.showMessageDialog(null, "A group named '" + groupname + "' already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Packet packet = new Packet(Packet.MSGTYPE_CREATE_NEW_GROUP);
        packet.setGroupname(groupname);
        boolean messageSentSuccessfully = parent.getMessageManager().sendMessage(packet);

        if (!messageSentSuccessfully)
        {
            JOptionPane.showMessageDialog(null, "Could not connect to the server to create new group.\nPlease try again later.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleCreateAndJoinNewGroupMessage(Packet packet)
    {
        if (packet == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nThe new group could not be created.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String groupname = packet.getGroupname();

        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nThe new group could not be created because the name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int statusCode = packet.getStatusCode();

        if (statusCode == Packet.SUCCESS)
        {
            Vector memberList = new Vector();
//            System.out.println("username:" + parent.getAuthenticationManager().getUsername() + ", groupname: " + groupname);
	    String username = parent.getAuthenticationManager().getUsername();
	    if (username != null)
	    {
		memberList.add(username);
	    }

	    if (!groupMemberLists.containsKey(groupname))
	    {
		groupMemberLists.put(groupname, memberList);
	    }

            if (!groupList.contains(groupname))
	    {
		groupList.add(groupname);
	    }

            // now we open the new group
	    ChatManager chatManager = parent.getChatManager();
            chatManager.createChatWindow(groupname);
            chatManager.getChatWindow(groupname).updateGroupMemberListPanel(memberList);

            // and update the list in the group window
            if (groupWindow != null)
	    {
		groupWindow.updateGroupList();
	    }
	    else
	    {
		System.out.println("Could not update the group list. The groupWindow is null.");
	    }
        }
        else if (statusCode == Packet.ERROR_GROUP_EXISTS)
        {
            // group was created in the short time between when we pressed the create group button and when we pressed submit
            JOptionPane.showMessageDialog(groupWindow, "A group named '" + groupname + "' already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(groupWindow, "An unexpected error occurred. Received packet with status code " + statusCode, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Vector getGroupList()
    {
        return groupList;
    }

    public void setGroupList(Vector groupList)
    {
        if (groupList == null)
        {
            return;
        }

        this.groupList = groupList;
    }

    /**
     * This code is called asynchronously when it receives a message from the server.
     */
    public void handleUpdateGroupListWithNewGroupMessage(Packet packet)
    {
        if (packet == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nA new group was created but the message could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String groupname = packet.getGroupname();

        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nA new group was created but the group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!groupList.contains(groupname))
        {
            groupList.add(groupname);

            // and update the list in the group window
            groupWindow.updateGroupList();
        }
    }

    /**
     * This code is called asynchronously when it receives a message from the server.
     */
    public void handleUpdateGroupListRemoveGroupMessage(Packet packet)
    {
        if (packet == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nA group was removed but the message could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String groupname = packet.getGroupname();

        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nA group was removed but the group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (groupList.contains(groupname))
        {
            groupList.remove(groupname);

            // and update the list in the group window
            groupWindow.updateGroupList();
        }
    }

    public void joinGroup(String groupname)
    {
        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while joining the group. The group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (groupMemberLists.containsKey(groupname))
        {
            JOptionPane.showMessageDialog(groupWindow, "An error occurred: You are already a member of the group.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Packet packet = new Packet(Packet.MSGTYPE_JOIN_GROUP);
        packet.setGroupname(groupname);
        boolean messageSentSuccessfully = parent.getMessageManager().sendMessage(packet);

        if (!messageSentSuccessfully)
        {
            JOptionPane.showMessageDialog(null, "Could not connect to the server to create new group.\nPlease try again later.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleJoinGroupMessage(Packet packet)
    {
        if (packet == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nThe application was unable to join you into this group.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String groupname = packet.getGroupname();

        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while joining the group. The group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int statusCode = packet.getStatusCode();

        if (statusCode == Packet.SUCCESS)
        {
	    Vector memberList = packet.getDataVector();
            groupMemberLists.put(groupname, memberList);
	    ChatManager chatManager = parent.getChatManager();
            chatManager.createChatWindow(groupname);
            chatManager.getChatWindow(groupname).updateGroupMemberListPanel(memberList);
        }
        else if (statusCode == Packet.ERROR_ALREADY_IN_GROUP)
        {
            JOptionPane.showMessageDialog(groupWindow, "An error occurred: You are already a member of the group.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (statusCode == Packet.ERROR_GROUP_DOES_NOT_EXIST)
        {
            JOptionPane.showMessageDialog(groupWindow, "An error occurred: The group named '" + groupname + "' no longer exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(groupWindow, "An unexpected error occurred. Received status code " + statusCode, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void removeFromGroup(String groupname)
    {
        if (groupname == null)
        {
            return;
        }

        if (groupMemberLists.containsKey(groupname))
        {
            groupMemberLists.remove(groupname);
        }

    }

    public Vector getGroupMembers(String groupname) throws NotMemberOfGroupException
    {
        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while getting the members of the group. The group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (groupMemberLists.containsKey(groupname))
        {
            Vector members = (Vector)groupMemberLists.get(groupname);
            return members;
        }
        else
        {
            throw new NotMemberOfGroupException();
        }
    }

    /**
     * This method is called asynchronously.
     */
    public void handleUpdateGroupMemberListWithNewMemberMessage(Packet packet)
    {
        if (packet == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nAnother user has joined the group but the information could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String groupname = packet.getGroupname();

        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nAnother user has joined a group but the group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = packet.getUsername();

        if (username == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nAnother user has joined the group but the user name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (groupMemberLists.containsKey(groupname))
        {
            Vector members = (Vector)groupMemberLists.get(groupname);
            members.add(username);

            ChatWindow chatWindow = parent.getChatManager().getChatWindow(groupname);
	    if (chatWindow != null)
	    {
		chatWindow.updateGroupMemberListPanel(members);
		chatWindow.announceMemberArrival(username);
	    }
	    else
	    {
		System.out.println("An error occurred: Could not update the chat window because chatWindow is null.");
	    }
        }
    }

    /**
     * This method is called asynchronously.
     */
    public void handleUpdateGroupMemberListRemoveMemberMessage(Packet packet)
    {
        if (packet == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nAnother user has left the group but the information could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String groupname = packet.getGroupname();

        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nAnother user has left a group but the group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = packet.getUsername();

        if (username == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server.\nAnother user has left the group but the user name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (groupMemberLists.containsKey(groupname))
        {
            Vector members = (Vector)groupMemberLists.get(groupname);
            members.remove(username);

            ChatWindow chatWindow = parent.getChatManager().getChatWindow(groupname);
	    if (chatWindow != null)
	    {
		chatWindow.updateGroupMemberListPanel(members);
		chatWindow.announceMemberDeparture(username);
	    }
	    else
	    {
		System.out.println("An error occurred: Could not update the chat window because chatWindow is null.");
	    }
        }
    }

    public void showGroupWindow()
    {
        groupWindow.updateGroupList();
        groupWindow.setVisible(true);
    }
 }
