
package retina.chat.client;

public class ScrollTest extends javax.swing.JFrame
{

    /** Creates new form ScrollTest */
    public ScrollTest()
    {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 200));
        jEditorPane1.setEditable(false);
        jEditorPane1.setText("\new\nds\nds\nds\nsd\n\n\n\n\nds\nfds\n\n\n\n\n\n\n\ndssf\nasd\nf\nasd\nf\nads\nfda\nsf\na\nsf\n");
        jEditorPane1.setMinimumSize(new java.awt.Dimension(200, 200));
        jScrollPane1.setViewportView(jEditorPane1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new ScrollTest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}