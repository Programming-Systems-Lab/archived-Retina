package retina.ui;/** * This class contains some helper methods for dealing with UI stuff */

import javax.swing.*;
public class UserInterfaceManager extends JFrame{    private MainPane mainpane;     private BrowsePane browsepane;    private StudentPane studentpane;    private ClassPane classpane;     private RuntimePane runpane;     public UserInterfaceManager() {        initComponents();         showMainPane();    }
    
    private void initComponents(){        setSize(1010, 740);        setLocation(10, 10);        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);        setBounds(new java.awt.Rectangle(120, 10, 1010, 740 ));        setVisible(true);        setResizable(true);        setTitle("RetinaServer Viewer");        mainpane = new MainPane(this);         add(mainpane);        browsepane = new BrowsePane(this);        add(browsepane);                studentpane = new StudentPane(this);        add(studentpane);                classpane = new ClassPane(this);        add(classpane);                runpane = new RuntimePane(this);        add(runpane);    }    public void showClassPane(){    	browsepane.setVisible(false);    	mainpane.setVisible(false);    	studentpane.setVisible(false);    	runpane.setVisible(false);    	classpane.setVisible(true);    	validate();    }    
    public void showStudentPane(){    	browsepane.setVisible(false);    	mainpane.setVisible(false);    	classpane.setVisible(false);        runpane.setVisible(false);    	studentpane.setVisible(true);    	validate();    }
    public void showMainPane(){        browsepane.setVisible(false);        studentpane.setVisible(false);        classpane.setVisible(false);        runpane.setVisible(false);        mainpane.setVisible(true);        validate();     }        public void showRuntimePane(){    	mainpane.setVisible(false);    	browsepane.setVisible(false);    	studentpane.setVisible(false);    	classpane.setVisible(false);    	runpane.setVisible(true);    	validate();     }

    public void showBrowsePane(){        mainpane.setVisible(false);        studentpane.setVisible(false);        classpane.setVisible(true);        browsepane.setVisible(true);        validate();     }
    
    public static void main(String args[]) {        java.awt.EventQueue.invokeLater(new Runnable() {            public void run() {                new UserInterfaceManager();             }        });    }

}
