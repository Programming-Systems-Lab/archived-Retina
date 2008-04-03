package retina.db;
import javax.swing.Icon;

import retina.im.BotEventHandler;
import retina.im.MessageSender;

import com.itbs.aimcer.bean.*;
import com.itbs.aimcer.commune.Connection;
import com.itbs.aimcer.commune.MessageSupport;
import com.itbs.aimcer.commune.msn.*;
import com.itbs.aimcer.commune.smack.*;
import com.itbs.aimcer.commune.ymsg.*;
import com.itbs.util.GeneralUtils;

class GroupImplFactory implements GroupFactory {
            public Group create(String group) {
                return new GroupImpl();
            }
            public Group create(Group group) {
                return new GroupImpl();
            }
			public GroupList getGroupList() {
				// TODO Auto-generated method stub
				return null;
			}
    }
    class GroupImpl implements Group {
        public int size() { return 0; }
        public void clear(Connection connection) { }
        public Nameable get(int index) { return null; }
        public Nameable add(Nameable contact) { return null; }
        public boolean remove(Nameable contact) { return false; }
        public String getName() { return "Group"; }
		public Nameable[] toArray() {
			// TODO Auto-generated method stub
			return null;
		}
    }


    class ContactImplFactory implements ContactFactory {
            public Contact create(Nameable buddy, Connection connection) {
                return create(buddy.getName(), connection);
            }


            public Contact create(String name, Connection connection) {
                return new ContactImpl(connection, name);
            }


            public Contact get(String name, Connection connection) {
                return new ContactImpl(connection, name);
            }
    }


    class ContactImpl implements Contact {
        private final Status status = new StatusImpl(this);
        private final Connection connection;
        private final String name;


        public ContactImpl(Connection connection, String name) {
            this.connection = connection;
            this.name = name;
        }
        public void statusChanged() {}
        public Icon getIcon() { return null; }
        public void setIcon(Icon icon) {}
        public Icon getPicture() { return null; }
        public void setPicture(Icon icon) { }
        public String getDisplayName() { return name; }
        public void setDisplayName(String name) {}
        public Status getStatus() { return status; }
        public Connection getConnection() { return connection; }
        public String getName() { return name; }
    }



public class RetinaServer {
	
	public static void main(String[] args) throws Exception 
	{
		
		/* Initialize shared objects */
		MessageSender sender = new MessageSender();

		/* Start the IM bot */
        
		// for Yahoo
		MessageSupport conn = new YMsgConnection();
        conn.setUserName("retina_help");
        conn.setPassword("retina.help123");	

        /*
		// for MSN Messenger
		MessageSupport conn = new MSNConnection();
        conn.setUserName("retina.help@hotmail.com");
        conn.setPassword("columbia.123");
         */
        
        conn.assignGroupFactory(new GroupImplFactory());
        conn.assignContactFactory(new ContactImplFactory());

        try {
            conn.addEventListener(new BotEventHandler(sender));
            conn.connect();
            System.out.println("IM bot connected");
        } catch (Exception e) {
            System.out.println("Failed to create required pieces.  Shutting down.");
            e.printStackTrace();
            return; // No point waiting if connection isn't available
        }

        /* Now, start the XmlServer to listen for incoming XML data (like compilations) */
        XmlServer xs = new XmlServer(1234, sender);
        xs.start();
        
        /* Now the server to listen for login events */
        // TODO: merge this with XmlServer?
        LoginEventServer ls = new LoginEventServer(1235);
        ls.start();
        
        while (true) { // Simplified version of "stick around" wait
            //System.out.println("waiting");
            GeneralUtils.sleep(60*60*1000);
        }
    } // main()

}
