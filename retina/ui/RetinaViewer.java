package retina.ui;
/**
 * This is the main entry point into the UI application.
 */



public class RetinaViewer  {

	
	
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new MainFrame().setVisible(true);
                new BrowsePane(new UserInterfaceManager()).setVisible(true);
            }
        });
    }


}
