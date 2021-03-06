/*
 * ScrollWindow.java
 *
 * Created on February 25, 2005, 9:02 PM
 */
package retina.chat.client;
/**
 *
 * @author  chris
 */
public class ScrollWindow extends javax.swing.JFrame
{
    String text;

    /** Creates new form ScrollWindow */
    public ScrollWindow()
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
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
        });

        jEditorPane1.setEditable(false);
        jEditorPane1.setContentType("text/html");
        jEditorPane1.setPreferredSize(new java.awt.Dimension(300, 200));
        jScrollPane1.setViewportView(jEditorPane1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jSplitPane1.setRightComponent(jTextArea1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buttonHandler(evt);
            }
        });

        jPanel1.add(jButton1, java.awt.BorderLayout.EAST);

        jTextField1.setText("jTextField1");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                mouseClickedHandler(evt);
            }
        });

        jPanel1.add(jTextField1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    boolean first = true;
    private void mouseClickedHandler(java.awt.event.MouseEvent evt)//GEN-FIRST:event_mouseClickedHandler
    {//GEN-HEADEREND:event_mouseClickedHandler
        if (first)
        {
            jTextField1.setText("");
            first = false;
        }
    }//GEN-LAST:event_mouseClickedHandler

    boolean flag = false;
    private void buttonHandler(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonHandler
    {//GEN-HEADEREND:event_buttonHandler
        text += jTextField1.getText() + "<p>";
        jEditorPane1.setText(text);
        jTextField1.setText("");
    }//GEN-LAST:event_buttonHandler

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)//GEN-FIRST:event_exitForm
    {
        System.exit(0);
    }//GEN-LAST:event_exitForm

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        new ScrollWindow().show();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
