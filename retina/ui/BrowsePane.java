package retina.ui;

/**
 * This is the pane that is used for the "Browse" feature. A user selects a student and an assignment,
 * and all compilation errors are listed.
 */

/*
 * BrowsePane.java
 */




import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.event.ListSelectionEvent;

import javax.swing.event.ListSelectionListener;

import javax.swing.table.TableColumn;

import retina.common.CompilationErrorEvent;
import retina.db.CompilationErrorEventManager;



/**
 * @author sh2503
 *
 */
public class BrowsePane extends javax.swing.JPanel {



    /*Variable Declaration*/

    private javax.swing.JComboBox assignmentCombo;

    private javax.swing.JLabel assignmentLabel;

    private javax.swing.JButton backButton;

    private javax.swing.JLayeredPane centerPane;

    private javax.swing.JLayeredPane statsPane;
    
    private javax.swing.JLabel commonErrorLabel;

    private javax.swing.JLabel commonErrorLabelData;

    private javax.swing.JTable dataTable;

    private javax.swing.JScrollPane listScrollPane;

    private javax.swing.JLayeredPane optionsPane;

    private javax.swing.JLabel selectedStudentLabel;

    private javax.swing.JLabel studentLabelData;

    private javax.swing.JList studentList;

    private javax.swing.JScrollPane tableScrollPane;

    private javax.swing.JLabel titleLabel;

    private javax.swing.JLabel totalErrorsLabel;

    private javax.swing.JLabel totalErrorsLabelData;

    private javax.swing.JLabel totalCompilationsLabel;
    
    private javax.swing.JLabel timeSpentLabel;
    
    private javax.swing.JLabel timeSpentLabelData;
    
    private javax.swing.JLabel totalCompilationsLabelData;

    private javax.swing.JButton viewButton;

    private ListSelectionModel listSelectionModel;

    private String selectedID, selectedAssignment;

    private DBTableModel dbtable;

    private CompilationErrorEvent compileError;

    private UserInterfaceManager uimanager;
    
    private CompilationErrorEventManager errorEventManager;


    /** Creates new form BrowsePane */

    public BrowsePane(UserInterfaceManager ui) {

        uimanager = ui;

        initComponents();

    }



    private void initComponents() {

        errorEventManager = new CompilationErrorEventManager();

        centerPane = new javax.swing.JLayeredPane();

        titleLabel = new javax.swing.JLabel();

        optionsPane = new javax.swing.JLayeredPane();

        assignmentCombo = new javax.swing.JComboBox();

        assignmentLabel = new javax.swing.JLabel();

        selectedStudentLabel = new javax.swing.JLabel();

        studentLabelData = new javax.swing.JLabel();

        viewButton = new javax.swing.JButton();

        statsPane = new javax.swing.JLayeredPane();

        commonErrorLabel = new javax.swing.JLabel();

        commonErrorLabelData = new javax.swing.JLabel();

        totalErrorsLabel = new javax.swing.JLabel();

        totalErrorsLabelData = new javax.swing.JLabel();

        totalCompilationsLabel = new javax.swing.JLabel();

        totalCompilationsLabelData = new javax.swing.JLabel();
        
        timeSpentLabel = new javax.swing.JLabel();

        timeSpentLabelData = new javax.swing.JLabel();

        backButton = new javax.swing.JButton();

        tableScrollPane = new javax.swing.JScrollPane();

        dataTable = new javax.swing.JTable();

        listScrollPane = new javax.swing.JScrollPane();

        studentList = new javax.swing.JList();

        /*All students, all assignments*/

        selectedID = "All";

        selectedAssignment = "1";
        
        tableScrollPane.setVisible(false);

        setBounds(new java.awt.Rectangle(0, 0, 1010, 740));

        setSize(1010, 740);

        setLocation(0, 0);

        setBackground(new java.awt.Color(153, 204, 255));

        setName("BrowsePane");

        titleLabel.setFont(new java.awt.Font("Verdana", 1, 24));

        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        titleLabel.setLabelFor(centerPane);

        titleLabel.setText("BROWSE");

        titleLabel.setBounds(280, 0, 190, 50);

        centerPane.add(titleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        optionsPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        assignmentCombo.setModel(new javax.swing.DefaultComboBoxModel(errorEventManager.getAssignments()));

        assignmentCombo.setBounds(170, 20, 140, 20);
        optionsPane.add(assignmentCombo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        assignmentCombo.getAccessibleContext().setAccessibleParent(optionsPane);

        assignmentLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        assignmentLabel.setLabelFor(optionsPane);
        assignmentLabel.setText("Select an Assignment:");
        assignmentLabel.setBounds(10, 20, 170, 20);
        optionsPane.add(assignmentLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        assignmentLabel.getAccessibleContext().setAccessibleParent(optionsPane);

        selectedStudentLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        selectedStudentLabel.setLabelFor(optionsPane);
        selectedStudentLabel.setText("Selected Student:");
        selectedStudentLabel.setBounds(10, 60, 170, 20);
        optionsPane.add(selectedStudentLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        studentLabelData.setFont(new java.awt.Font("Verdana", 1, 12));
        studentLabelData.setLabelFor(optionsPane);
        studentLabelData.setMaximumSize(new java.awt.Dimension(117, 16));
        studentLabelData.setBounds(170, 60, 180, 20);
        optionsPane.add(studentLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);

        optionsPane.setBounds(210, 50, 320, 90);
        centerPane.add(optionsPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        viewButton.setFont(new java.awt.Font("Verdana", 1, 11));
        viewButton.setText("View Data");
        viewButton.setActionCommand("view");
        viewButton.setBounds(330, 150, 100, 23);
        centerPane.add(viewButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        statsPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalCompilationsLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        totalCompilationsLabel.setLabelFor(statsPane);
        totalCompilationsLabel.setText("Total Number of Compilations:");
        totalCompilationsLabel.setBounds(10, 10, 210, 20);
        statsPane.add(totalCompilationsLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        totalCompilationsLabelData.setFont(new java.awt.Font("Verdana", 1, 12));
        totalCompilationsLabelData.setLabelFor(statsPane);
        totalCompilationsLabelData.setText("");
        totalCompilationsLabelData.setBounds(240, 10, 140, 20);
        statsPane.add(totalCompilationsLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        totalErrorsLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        totalErrorsLabel.setLabelFor(statsPane);
        totalErrorsLabel.setText("Total Number of Errors:");
        totalErrorsLabel.setBounds(10, 40, 200, 20);
        statsPane.add(totalErrorsLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        totalErrorsLabelData.setFont(new java.awt.Font("Verdana", 1, 12));
        totalErrorsLabelData.setLabelFor(statsPane);
        totalErrorsLabelData.setBounds(190, 40, 70, 20);
        statsPane.add(totalErrorsLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);

        timeSpentLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        timeSpentLabel.setLabelFor(statsPane);
        timeSpentLabel.setText("Time spent on assignment:");
        timeSpentLabel.setBounds(250, 40, 190, 20);
        statsPane.add(timeSpentLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        timeSpentLabelData.setFont(new java.awt.Font("Verdana", 1, 12));
        timeSpentLabelData.setLabelFor(statsPane);
        timeSpentLabelData.setText("");
        timeSpentLabelData.setBounds(440, 40, 250, 20);
        statsPane.add(timeSpentLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);

        commonErrorLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        commonErrorLabel.setLabelFor(statsPane);
        commonErrorLabel.setText("Most Common Error:");
        commonErrorLabel.setBounds(10, 70, 170, 20);
        statsPane.add(commonErrorLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        commonErrorLabelData.setFont(new java.awt.Font("Verdana", 1, 12));
        commonErrorLabelData.setLabelFor(statsPane);
        commonErrorLabelData.setBounds(190, 70, 550, 20);
        statsPane.add(commonErrorLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);

        statsPane.setBounds(20, 600, 730, 100);
        centerPane.add(statsPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        backButton.setFont(new java.awt.Font("Verdana", 1, 11));
        backButton.setText("Back to Main");
        backButton.setActionCommand("back");
        backButton.setBounds(640, 10, 120, 30);
        centerPane.add(backButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        tableScrollPane.setViewportView(dataTable);

        tableScrollPane.setBounds(20, 180, 730, 420);

        centerPane.add(tableScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(listScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(centerPane, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(centerPane, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addComponent(listScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

       /*Create JList; Data: User IDs retrieved with DatabaseManager method*/

        studentList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = errorEventManager.getStudents();

            public int getSize() { return strings.length; }

            public Object getElementAt(int i) { return strings[i]; }

        });

        studentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        listScrollPane.setViewportView(studentList);



        /*ADD A LISTENER TO THE LIST*/

        listSelectionModel = studentList.getSelectionModel();

        listSelectionModel.addListSelectionListener(new ListSelectionListener()

                {

                    public void valueChanged(ListSelectionEvent e){

                            handleValueChanged(e);

                    }

                }

              );

        assignmentCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                handleAssignmentActionPerformed(evt);

            }

        });

        viewButton.addActionListener(new ActionListener()
        {

                public void actionPerformed(ActionEvent evt){

                        handleViewButtonActionPerformed(evt);

                }

            }

           );

        backButton.addActionListener(new ActionListener()
        {

                public void actionPerformed(ActionEvent evt){

                        handleBackButtonActionPerformed(evt);

                }

            }

           );


    }



    /*Method to handle ListSelectionEvent for the Jlist

     *Store the ID selected by user in variable selectedID

     */

    public void handleValueChanged(ListSelectionEvent e) {

        int id_index;

        if (e.getValueIsAdjusting() == false) {

            id_index = studentList.getSelectedIndex();

            if (id_index == -1) {

            }

            else {

                selectedID = (String) studentList.getSelectedValue();
                studentLabelData.setText(selectedID);

            }

        }

    }



   /*Method to handle event for JComboBox

    *Store the assignment selected in variable selectedAssignment

    */

    private void handleAssignmentActionPerformed(ActionEvent evt){

         JComboBox cb = (JComboBox) evt.getSource();

         selectedAssignment = (String)cb.getSelectedItem();
         
    }



    /*Unnecessary accessor methods*/

    private String getSelectedAssignment(){

        return selectedAssignment;

    }



    private String getSelectedID(){

        return selectedID;

    }



    /*Handle viewButton clicked event

     *updateTable method selectedID from Jlist and selectAssignment from JComboBox*/

    public void handleViewButtonActionPerformed(ActionEvent evt){

        String id = getSelectedID();

        String assignment = getSelectedAssignment();

        if("view".equals(evt.getActionCommand())){

            viewButton.setEnabled(true);

            if(id.equals("All"))
            {
            	
                JOptionPane.showMessageDialog(this, "Please select a student ID");
            }
            else
            {
            
            	updateData(id, assignment);
            }

        }

        else{

            viewButton.setEnabled(false);

        }

    }

    public void handleBackButtonActionPerformed(ActionEvent evt){

        if("back".equals(evt.getActionCommand())){

            backButton.setEnabled(true);
            
            studentLabelData.setText("");
            
            selectedID = "All";
            
            selectedAssignment = "1";
        	 
    	    commonErrorLabelData.setText("");

    	    totalErrorsLabelData.setText("");
    	    
    	    totalCompilationsLabelData.setText("");
    	    
    	    timeSpentLabelData.setText("");

    	    assignmentCombo.setSelectedIndex(0); 
               
            centerPane.remove(tableScrollPane);

            uimanager.showMainPane();

        }

        else{

            viewButton.setEnabled(false);

        }

    }


    /*updateTable method draws a JTable based on user id and assignment selections

     *from the user by constructing new instance of DBTableModel

     *@param String id User ID

     *@param String assignment Assignment Number

     */

    private void updateData(String id, String assignment){


        try {
            CompilationErrorEvent[] errors = errorEventManager.getCompilationErrorEvents(id, assignment);
            if(errors == null)
            {
                //dataTable.setModel(new DBTableModel(dbmanager.getCompilationErrorEvents(id, assignment)));
                JOptionPane.showMessageDialog(this, "No data available for selected student and assignment");
                compileError = null;
            }
            else
            {
                compileError = errorEventManager.getMostCommonCompilationError(id, assignment);
            }

            dbtable = new DBTableModel(errors);
            dataTable.setModel(dbtable);
            tableScrollPane.setVisible(true);

            if(compileError != null)
            {

                commonErrorLabelData.setText(compileError.getError());
                totalErrorsLabelData.setText(Integer.toString(dbtable.getRowCount()));
                
            }
            else
            {

                commonErrorLabelData.setText("");
                totalErrorsLabelData.setText("");
            }
            
            String totalCompilations = Integer.toString(errorEventManager.getTotalNumberOfCompilations(id, assignment));
            String successfulCompilations = Integer.toString(errorEventManager.getNumberOfSuccessfulCompilations(id, assignment));
            totalCompilationsLabelData.setText(totalCompilations + "  (" + successfulCompilations + " successful)");
            
            DateConverter dc = new DateConverter();
            long[] workTime = dc.computeWorkTime(errorEventManager.getCompilationEvents(id, assignment));
            if(workTime[0] == 0){
            	timeSpentLabelData.setText("Less Than 1 Hour");
            }
            else{
            	timeSpentLabelData.setText("Approx. " + (int)workTime[0] + " hrs. and " + (int)workTime[1] + " mins.");
            }


        } catch (Exception ex) {

            ex.printStackTrace();

        }

        /*Adjust the dimensions of the table..Not sure if this is necessary*/

        TableColumn column = null;

        for(int i = 0; i < dataTable.getColumnCount(); i++){

            column = dataTable.getColumnModel().getColumn(i);

            if(i == 0){

                column.setPreferredWidth(200);

            }

            else{

                column.setPreferredWidth(30);

            }

        }



    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new MainFrame().setVisible(true);
                new BrowsePane(new UserInterfaceManager()).setVisible(true);
            }
        });

    }

}

