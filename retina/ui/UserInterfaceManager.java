package retina.ui;/** * This class contains some helper methods for dealing with UI stuff */

import javax.swing.*;
public class UserInterfaceManager extends JFrame{    private MainPane mainpane;     private BrowsePane browsepane;    private AssignmentPane assignpane;    public UserInterfaceManager() {        initComponents();         showMainPane();    }
    
    private void initComponents(){        setSize(1010, 740);        setLocation(10, 10);        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);        setBounds(new java.awt.Rectangle(120, 10, 1010, 740 ));        setVisible(true);        setResizable(true);        setTitle("RetinaServer Viewer");        mainpane = new MainPane(this);         add(mainpane);        browsepane = new BrowsePane(this);        add(browsepane);                assignpane = new AssignmentPane(this);        add(assignpane);    }
    public void showAssignPane(){    	browsepane.setVisible(false);    	mainpane.setVisible(false);    	assignpane.setVisible(true);    	validate();    }
    public void showMainPane(){        browsepane.setVisible(false);        assignpane.setVisible(false);        mainpane.setVisible(true);        validate();     }

    public void showBrowsePane(){        mainpane.setVisible(false);        assignpane.setVisible(false);        browsepane.setVisible(true);        validate();     }
    
    public static void main(String args[]) {        java.awt.EventQueue.invokeLater(new Runnable() {            public void run() {                new UserInterfaceManager();             }        });    }

}
