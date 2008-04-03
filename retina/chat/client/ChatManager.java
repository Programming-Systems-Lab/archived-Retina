package retina.chat.client;

import java.util.Hashtable;
import javax.swing.JOptionPane;

import retina.chat.common.Packet;

public class ChatManager
{

    private ChatClient parent;

    /**
     * key: groupname (String)
     * value: ChatWindow
     */
    private Hashtable chatWindows;

    /** Creates a new instance of ChatManager */
    public ChatManager(ChatClient chatClient)
    {
        parent = chatClient;
        chatWindows = new Hashtable();
    }

    public void sendChatMessage(String groupname, String chatMessage, ChatWindow chatWindow)
    {
        if (groupname == null || chatMessage == null || chatWindow == null)
        {
            JOptionPane.showMessageDialog(null, "An internal error occurred while trying to send the message.\nPlease try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            // put back the message...
            chatWindow.setTextField(chatMessage);
        }

	/**
	// see what type of message it is - default to regular old "chat message"
	int type = Packet.MSGTYPE_CHAT_MESSAGE;

	// the text itself is inside an HTML tag, so to evaluate it we need to strip it out
	System.out.println(chatMessage);
	if (chatMessage.contains("help"))
	    {
		type = Packet.MSGTYPE_HELP;
		System.out.println("HELP MESSAGE");
	    }
	else System.out.println("REGULAR MESSAGE");
	**/

        Packet packet = new Packet(Packet.MSGTYPE_CHAT_MESSAGE);
        packet.setGroupname(groupname);
        packet.setChatMessage(chatMessage);
        boolean sendMessageStatus = parent.getMessageManager().sendMessage(packet);

        if (!sendMessageStatus)
        {
            JOptionPane.showMessageDialog(null, "Could not connect to the server to send message.\nPlease try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            // put back the message...
            chatWindow.setTextField(chatMessage);
        }
    }


    public void leaveGroup(String groupname)
    {
        if (groupname == null)
        {
            return;
        }
        Packet packet = new Packet(Packet.MSGTYPE_LEAVE_GROUP);
        packet.setGroupname(groupname);
        parent.getMessageManager().sendMessage(packet);
    }

    /**
     * This code is called asynchronously when it receives a message from the server.
     */
    public void handleReceiveChatMessageMessage(Packet packet)
    {
        if (packet == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server. Another user sent a chat message but it could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String groupname = packet.getGroupname();
        String username = packet.getUsername();
        String chatMessage = packet.getChatMessage();

        if (groupname == null || username == null || chatMessage == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while communicating with the server. Another user sent a chat message but it could not be fully read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!chatWindows.containsKey(groupname))
        {
            return;
        }

        ChatWindow chatWindow = (ChatWindow)chatWindows.get(groupname);
        chatWindow.appendChatMessageToChatPanel(username, chatMessage);
    }

    public void createChatWindow(String groupname)
    {
        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while creating the chat window. The group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ChatWindow window = new ChatWindow(parent, groupname);
        chatWindows.put(groupname, window);
        window.setVisible(true);
    }

    public void removeChatWindow(String groupname)
    {
        if (groupname != null && chatWindows.containsKey(groupname))
        {
            chatWindows.remove(groupname);
        }
    }

    public ChatWindow getChatWindow(String groupname)
    {
        if (groupname == null)
        {
            JOptionPane.showMessageDialog(null, "An error occurred while receiving a chat message. The group name could not be read.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return (ChatWindow)(chatWindows.get(groupname));
    }
}
