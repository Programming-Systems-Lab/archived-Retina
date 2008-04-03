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

import graph.*;

import retina.common.CompilationErrorEvent;
import retina.common.CompilationEvent;
import retina.common.OccurrenceMap;
import retina.db.CompilationErrorEventManager;



/**
 * @author sh2503
 *
 */
public class AssignmentPane extends javax.swing.JPanel {



    /*Variable Declaration*/

    private javax.swing.JComboBox assignmentCombo;
    
    private javax.swing.JComboBox statCombo;

    private javax.swing.JLabel assignmentLabel;

    private javax.swing.JButton backButton;

    private javax.swing.JLayeredPane centerPane;

    private javax.swing.JTable commonErrorsTable; 

    private javax.swing.JLayeredPane optionsPane;
    
    private javax.swing.JLayeredPane statsPane;
    
    private javax.swing.JLayeredPane generalStatsPane; 

    private javax.swing.JList studentList;
   
    private javax.swing.JScrollPane commonErrorsScrollPane; 
    
    private javax.swing.JScrollPane listScrollPane;

    private javax.swing.JLabel selectedStudentLabel;
    
    private javax.swing.JLabel selectedStatLabel;

    private javax.swing.JLabel studentLabelData;

    private javax.swing.JLabel titleLabel;
    
    private javax.swing.JLabel topErrorsLabel;
    
    private javax.swing.JLabel totalErrorsLabel;

    private javax.swing.JLabel totalErrorsLabelData;

    private javax.swing.JLabel totalCompilationsLabel;

    private javax.swing.JLabel totalCompilationsLabelData;
    
    private javax.swing.JButton viewButton;

    private ListSelectionModel listSelectionModel;

    private String selectedID, selectedAssignment;
    
    private int selectedStat;

    private CompilationErrorEvent compileError;

    private UserInterfaceManager uimanager;
    
    private OccurrenceMap commonErrorsMap, compileTimesMap, errorTimesMap;
    
    private CompilationErrorEventManager errorEventManager;
    
    private DateConverter dateConverter; 
    
   
    /** Creates new form BrowsePane */

    public AssignmentPane(UserInterfaceManager ui) {

        uimanager = ui;

        initComponents();

    }



    private void initComponents() {

        errorEventManager = new CompilationErrorEventManager();
        
        dateConverter = new DateConverter();

        centerPane = new javax.swing.JLayeredPane();

        titleLabel = new javax.swing.JLabel();
        
        topErrorsLabel = new javax.swing.JLabel();

        optionsPane = new javax.swing.JLayeredPane();

        assignmentCombo = new javax.swing.JComboBox();
        
        statCombo = new javax.swing.JComboBox();

        assignmentLabel = new javax.swing.JLabel();

        selectedStudentLabel = new javax.swing.JLabel();
        
        studentLabelData = new javax.swing.JLabel();

        selectedStatLabel = new javax.swing.JLabel();

        viewButton = new javax.swing.JButton();

        statsPane = new javax.swing.JLayeredPane();
        
        generalStatsPane = new javax.swing.JLayeredPane();
        
        totalErrorsLabel = new javax.swing.JLabel();

        totalErrorsLabelData = new javax.swing.JLabel();

        totalCompilationsLabel = new javax.swing.JLabel();

        totalCompilationsLabelData = new javax.swing.JLabel();

        backButton = new javax.swing.JButton();

        commonErrorsScrollPane = new javax.swing.JScrollPane(); 

        commonErrorsTable = new javax.swing.JTable(); 

        listScrollPane = new javax.swing.JScrollPane();

        studentList = new javax.swing.JList();

        /*All students, all assignments*/

        selectedID = "All";

        selectedAssignment = "1";
        
        selectedStat = 0;

        setBounds(new java.awt.Rectangle(0, 0, 1010, 740));

        setSize(1010, 740);

        setLocation(0, 0);

        setBackground(new java.awt.Color(153, 204, 255));

        setName("AssignmentPane");
        
        commonErrorsTable.setVisible(false);
        
        statsPane.setVisible(false);

        titleLabel.setFont(new java.awt.Font("Verdana", 1, 24));

        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        titleLabel.setLabelFor(centerPane);

        titleLabel.setText("ASSIGNMENT STATISTICS");

        titleLabel.setBounds(170, 0, 400, 50);

        centerPane.add(titleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        optionsPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        
        assignmentCombo.setModel(new javax.swing.DefaultComboBoxModel(errorEventManager.getAssignments()));
        
        assignmentCombo.setBounds(170, 18, 140, 20);
        
        optionsPane.add(assignmentCombo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        assignmentCombo.getAccessibleContext().setAccessibleParent(optionsPane);

        assignmentLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        
        assignmentLabel.setLabelFor(optionsPane);
        
        assignmentLabel.setText("Select an Assignment:");
        
        assignmentLabel.setBounds(10, 20, 170, 16);
        
        optionsPane.add(assignmentLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        assignmentLabel.getAccessibleContext().setAccessibleParent(optionsPane);

        selectedStudentLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        
        selectedStudentLabel.setLabelFor(optionsPane);
        
        selectedStudentLabel.setText("Selected Student:");
        
        selectedStudentLabel.setBounds(330, 20, 170, 16);
        
        optionsPane.add(selectedStudentLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        studentLabelData.setFont(new java.awt.Font("Verdana", 1, 12));

        studentLabelData.setLabelFor(optionsPane);
        
        studentLabelData.setMaximumSize(new java.awt.Dimension(117, 16));
        
        studentLabelData.setBounds(450, 20, 170, 16);
        
        optionsPane.add(studentLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);

        selectedStatLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        
        selectedStatLabel.setLabelFor(optionsPane);
        
        selectedStatLabel.setText("Select a Statistic:");
        
        selectedStatLabel.setBounds(120, 53, 140, 20);
        
        optionsPane.add(selectedStatLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        statCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Top 10 Most Common Errors(Entire Class)", "Graph-Errors vs. Time", "Graph-Class # of Compilations vs. Time", "Graph-Class # of Errors vs. Times" }));
       
        statCombo.setBounds(255, 53, 340, 20);
        
        optionsPane.add(statCombo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        optionsPane.setBounds(70, 50, 600, 80);
        
        centerPane.add(optionsPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        viewButton.setFont(new java.awt.Font("Verdana", 1, 11));
        
        viewButton.setText("View Data");
        
        viewButton.setActionCommand("view");
        
        viewButton.setBounds(320, 140, 100, 23);
        
        centerPane.add(viewButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        statsPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        
        statsPane.setBounds(20, 170, 730, 470);
        centerPane.add(statsPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        generalStatsPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalCompilationsLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        totalCompilationsLabel.setLabelFor(optionsPane);
        totalCompilationsLabel.setText("Total Number of Compilations:");
        totalCompilationsLabel.setBounds(20, 10, 200, 20);
        generalStatsPane.add(totalCompilationsLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        totalCompilationsLabelData.setFont(new java.awt.Font("Verdana", 1, 12));
        totalCompilationsLabelData.setLabelFor(optionsPane);
        totalCompilationsLabelData.setText("");
        totalCompilationsLabelData.setBounds(240, 10, 200, 20);
        generalStatsPane.add(totalCompilationsLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);

        totalErrorsLabel.setFont(new java.awt.Font("Verdana", 1, 12));
        totalErrorsLabel.setLabelFor(optionsPane);
        totalErrorsLabel.setText("Total Number of Errors:");
        totalErrorsLabel.setBounds(20, 40, 170, 20);
        generalStatsPane.add(totalErrorsLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        totalErrorsLabelData.setFont(new java.awt.Font("Verdana", 1, 12));
        totalErrorsLabelData.setLabelFor(optionsPane);
        totalErrorsLabelData.setBounds(190, 40, 520, 20);
        generalStatsPane.add(totalErrorsLabelData, javax.swing.JLayeredPane.DEFAULT_LAYER);

        generalStatsPane.setBounds(20, 640, 730, 70);
        centerPane.add(generalStatsPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        backButton.setFont(new java.awt.Font("Verdana", 1, 11));
        backButton.setText("Back to Main");
        backButton.setActionCommand("back");
        backButton.setBounds(640, 10, 120, 30);
        centerPane.add(backButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
        
        statCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                handleStatActionPerformed(evt);

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
    
    
    /*Method to handle event for JComboBox

     *Store the statistic selected in variable selectedStat

     */

     private void handleStatActionPerformed(ActionEvent evt){

          JComboBox cb = (JComboBox) evt.getSource();

          selectedStat = (int)cb.getSelectedIndex();

     }


    /*Unnecessary accessor methods*/

    private String getSelectedAssignment(){

        return selectedAssignment;

    }
    
    private int getSelectedStat(){
    	
    	return selectedStat; 
    	
    }



    private String getSelectedID(){

        return selectedID;

    }



    /*Handle viewButton clicked event

     *updateTable method selectedID from Jlist and selectAssignment from JComboBox*/

    public void handleViewButtonActionPerformed(ActionEvent evt){

        String id = getSelectedID();

        String assignment = getSelectedAssignment();
        
        int stat = getSelectedStat();
        
        if("view".equals(evt.getActionCommand())){

            viewButton.setEnabled(true);

            updateData(id, assignment, stat);

        }

        else{

            viewButton.setEnabled(false);

        }

    }
    
    public void handleBackButtonActionPerformed(ActionEvent evt){

        if("back".equals(evt.getActionCommand())){

            backButton.setEnabled(true);
            
            studentLabelData.setText("");
            
            totalCompilationsLabelData.setText("");
            
            totalErrorsLabelData.setText("");
            
            selectedID = "All";
            
            selectedAssignment = "1";
            
            statsPane.removeAll(); 
            
            statsPane.setVisible(false);

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

    private void updateData(String id, String assignment, int stat){
    	
    	statsPane.removeAll();
        totalCompilationsLabelData.setText("");
        totalErrorsLabelData.setText("");
   	
    	//Selected Stat: 0. Top 10 Most Common Errors
        if(stat == 0){
        	//Create a JTable to list top 10 most common errors 
        	final int mostErrors = 10;
        	try{
        		commonErrorsMap = errorEventManager.getMostCommonCompilationErrors(assignment, mostErrors);
        	}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		if(commonErrorsMap == null){
    			 JOptionPane.showMessageDialog(this, "No data available for selected assignment, student, or statistic");
                 
    		}
    		else{
    			String[] headers = new String[3]; 
    			headers[0] = ""; 
    			headers[1] = "Error";
    			headers[2] = "Frequency";
    			Object[][] data = new Object[commonErrorsMap.size()][3];
    			int col = 0, row = 0;
    			for(row = 0; row < commonErrorsMap.size(); row++)
    			{
    				data[row][col++] = (row)+1; 
    				data[row][col++] = commonErrorsMap.keys[row];		
    				data[row][col] = commonErrorsMap.occurrences[row];
    				col = 0;
    			}
    		    topErrorsLabel.setText("Top 10 Most Common Errors");
    		    topErrorsLabel.setBounds(300, 30, 300, 100);
    		    statsPane.add(topErrorsLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    			commonErrorsTable.setModel(new javax.swing.table.DefaultTableModel(data, headers));
    			commonErrorsScrollPane.setViewportView(commonErrorsTable);
    			TableColumn column = commonErrorsTable.getColumnModel().getColumn(0);
    			column.setMaxWidth(30);
    			commonErrorsScrollPane.setBounds(90, 120, 560, 190);
    			statsPane.add(commonErrorsScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
    			statsPane.setVisible(true);
    			commonErrorsTable.setVisible(true);
    		}
        }
        else if (stat == 1){
        	//Stat 1: Graph of Number of Errors over Time
        	if(id.equals("All")){
        		JOptionPane.showMessageDialog(this, "Please select a student ID");
        		
        	}
        	else{
        		CompilationEvent[] events = errorEventManager.getCompilationEvents(id, assignment);
        		int length = events.length;
        		int i = 0;
        		int totalerrors = 0; 
        		String[] dates = new String[length];
        		int[] errorsnum = new int[length];
        		
        		//Check to see if student made any errors before plotting graph
        		if(length != errorEventManager.getNumberOfSuccessfulCompilations(id, assignment))
        		{
        			for(i = 0; i < length; i++)
        			{        		
        				dates[i] = events[i].getTime();
        				errorsnum[i] = events[i].getErrors();
        				totalerrors += errorsnum[i];
        			}

        		
        		//Create basic integer array for x-axis
        			int[] xaxis = new int[length];
        			for(i = 0; i < length; i++)
        			{
        				xaxis[i] = i*5;
        			}

        			StatGraph etgraph = new StatGraph(xaxis, errorsnum, length);
        			etgraph.setXTitle("Time");
        			etgraph.setYTitle("Number of Errors per Compilation");
        			Graph2D graph = etgraph.getGraph();
        			graph.setBounds(10, 10, 710, 455);
        			statsPane.add(graph, javax.swing.JLayeredPane.DEFAULT_LAYER);
        			statsPane.setVisible(true);
        			
        			//Update general stats: Total number of compilations, total number of errors
        			
        			 String totalCompilations = Integer.toString(errorEventManager.getTotalNumberOfCompilations(id, assignment));
        	         String successfulCompilations = Integer.toString(errorEventManager.getNumberOfSuccessfulCompilations(id, assignment));
        	         totalCompilationsLabelData.setText(totalCompilations + "  (" + successfulCompilations + " successful)");
        	         totalErrorsLabelData.setText(Integer.toString(totalerrors));
        
        		}
        		else if(length == 0)
        		{
        			JOptionPane.showMessageDialog(this, "No data available for this student on this assignment");
                		
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(this, "No errors were made by this student on this assignment");
            	
        		}
           	}
        }
        else if(stat == 2){
        //Graph the number of compilations over time for the whole class 
        	try{
        		compileTimesMap = errorEventManager.getCompilationTimes(assignment);
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
            if(compileTimesMap == null){
            	JOptionPane.showMessageDialog(this, "No data available for selected assignment");
                         
            }
            else{
            	
            	int col = 0, row = 0; 
            	//Create basic integer array for x-axis
            	int length = compileTimesMap.size(); 
    			int[] xaxis = new int[length];
    			
    			String[] date_strings = compileTimesMap.keys;
    			
    			for(int i = 0; i < length; i++)
    			{
    				xaxis[i] = dateConverter.dateToInt(date_strings[i]);
    				
    			}
    			
    		    StatGraph ctgraph = new StatGraph(xaxis, compileTimesMap.occurrences, length);
    		//	ctgraph.setXLabels(dates);
    			ctgraph.setXTitle("Date & Time");
    			ctgraph.setYTitle("Number of Compilations");
    		
    			Graph2D graph = ctgraph.getGraph();
    			graph.setBounds(10, 10, 710, 455);
    			statsPane.add(graph, javax.swing.JLayeredPane.DEFAULT_LAYER);
    			statsPane.setVisible(true);
    			
            }
        }
        else if(stat == 3){
            //Graph the number of compilations over time for the whole class 
            	try{
            		errorTimesMap = errorEventManager.getCompilationErrorTimes(assignment);
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
                if(errorTimesMap == null){
                	JOptionPane.showMessageDialog(this, "No data available for selected assignment");
                             
                }
                else{
                	
                	int col = 0, row = 0; 
                	//Create basic integer array for x-axis
                	int length = errorTimesMap.size(); 
        			int[] xaxis = new int[length];
        			
        			String[] date_strings = errorTimesMap.keys;
        			
        			for(int i = 0; i < length; i++)
        			{
        				xaxis[i] = dateConverter.dateToInt(date_strings[i]);
        			}
        		
        			
        			StatGraph errorTimesGraph = new StatGraph(xaxis, errorTimesMap.occurrences, length);
        	//		errorTimesGraph.setXLabels(dates);
        			errorTimesGraph.setXTitle("Date & Time");
        			errorTimesGraph.setYTitle("Number of Errors");
        		
        			Graph2D graph = errorTimesGraph.getGraph();
        			graph.setBounds(10, 10, 710, 455);
        			statsPane.add(graph, javax.swing.JLayeredPane.DEFAULT_LAYER);
        			statsPane.setVisible(true);
        			
                }
            }

        
    }        
          
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AssignmentPane(new UserInterfaceManager()).setVisible(true);
            }
        });
    }

}

