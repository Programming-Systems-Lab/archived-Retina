package retina.ui;
/**
 * This is the class that is shown in the main (startup) screen. 
 */	


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MainPane extends javax.swing.JPanel {
    
  private javax.swing.JLabel assignmentDescriptionLabel;
  private javax.swing.JLabel studentDescriptionLabel;
  private javax.swing.JLabel browseDescriptionLabel;
  private javax.swing.JLabel errorDescriptionLabel;
  private javax.swing.JRadioButton buttonAStat;
  private javax.swing.JRadioButton buttonBrowse;
  private javax.swing.JRadioButton buttonEStat;
  private javax.swing.ButtonGroup buttonGroup;
  private javax.swing.JRadioButton buttonSStat;
  private javax.swing.JLayeredPane centerPane;
  private javax.swing.JLabel classLabel;
  private javax.swing.JButton goButton;
  private javax.swing.JLayeredPane optionsPane;
  private javax.swing.JLabel projectLabel;
  private javax.swing.JLabel titleLabel;
  private String browseString = "browse", astatString = "astat", estatString = "estat", sstatString = "sstat"; 
  private ActionListener buttonListener; 
  private UserInterfaceManager uimanager; 
  private String selectedButton; 
    
    
    /** Creates new form MainPane */
    public MainPane(UserInterfaceManager ui) {
        
        uimanager = ui; 
        
        initComponents();
    }
                             
    private void initComponents() {
       
        buttonGroup = new javax.swing.ButtonGroup();
        
        centerPane = new javax.swing.JLayeredPane();
        
        titleLabel = new javax.swing.JLabel();
        
        optionsPane = new javax.swing.JLayeredPane();
        
        buttonBrowse = new javax.swing.JRadioButton();
        
        buttonEStat = new javax.swing.JRadioButton();
        
        buttonAStat = new javax.swing.JRadioButton();
        
        buttonSStat = new javax.swing.JRadioButton();
        
        browseDescriptionLabel = new javax.swing.JLabel();
        
        assignmentDescriptionLabel = new javax.swing.JLabel();
        
        studentDescriptionLabel = new javax.swing.JLabel();
        
        errorDescriptionLabel = new javax.swing.JLabel();
        
        goButton = new javax.swing.JButton();
        
        classLabel = new javax.swing.JLabel();
        
        projectLabel = new javax.swing.JLabel();

        setBounds(new java.awt.Rectangle(0, 0, 1010, 740));
       
        setSize(1010, 740);
       
        setLocation(0, 0);
        
        setBackground(new java.awt.Color(153, 204, 255));

        setName("MainPane");
        
        centerPane = new javax.swing.JLayeredPane();
        optionsPane = new javax.swing.JLayeredPane();
        buttonGroup = new javax.swing.ButtonGroup();
        buttonBrowse = new javax.swing.JRadioButton();
        buttonEStat = new javax.swing.JRadioButton();
        buttonAStat = new javax.swing.JRadioButton();
        buttonSStat = new javax.swing.JRadioButton();
        titleLabel = new javax.swing.JLabel();
        browseDescriptionLabel = new javax.swing.JLabel();
        assignmentDescriptionLabel = new javax.swing.JLabel();
        studentDescriptionLabel = new javax.swing.JLabel();
        errorDescriptionLabel = new javax.swing.JLabel();
        goButton = new javax.swing.JButton();
        classLabel = new javax.swing.JLabel();
        projectLabel = new javax.swing.JLabel();
        
        selectedButton = browseString; 

        setBackground(new java.awt.Color(153, 204, 255));
        setMaximumSize(new java.awt.Dimension(1000, 700));
        setMinimumSize(new java.awt.Dimension(1000, 700));
        setName("BrowsePane");
        
        projectLabel.setFont(new java.awt.Font("Britannic Bold", 1, 40));
        projectLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        projectLabel.setText("RETINA VIEWER");
        projectLabel.setBounds(315, 10, 380, 70);
        centerPane.add(projectLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        titleLabel.setFont(new java.awt.Font("Verdana", 1, 24));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setLabelFor(centerPane);
        titleLabel.setText("MAIN MENU");
        titleLabel.setBounds(410, 160, 190, 50);
        centerPane.add(titleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        optionsPane.setBackground(new java.awt.Color(152, 204, 255));
        optionsPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        optionsPane.setForeground(new java.awt.Color(153, 204, 255));
        buttonBrowse.setBackground(new java.awt.Color(255, 255, 255));
        buttonBrowse.setFont(new java.awt.Font("Verdana", 1, 18));
        buttonBrowse.setSelected(true);
        buttonBrowse.setText("Browse");
        buttonBrowse.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonBrowse.setContentAreaFilled(false);
        buttonBrowse.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonBrowse.setBounds(20, 30, 200, 40);
        buttonBrowse.setActionCommand(browseString);
        optionsPane.add(buttonBrowse, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttonBrowse.getAccessibleContext().setAccessibleParent(optionsPane);

        buttonEStat.setFont(new java.awt.Font("Tahoma", 1, 18));
        buttonEStat.setText("Error Stats");
        buttonEStat.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonEStat.setContentAreaFilled(false);
        buttonEStat.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonEStat.setBounds(450, 30, 200, 40);
        buttonEStat.setActionCommand(estatString);
        optionsPane.add(buttonEStat, javax.swing.JLayeredPane.DEFAULT_LAYER);

        buttonAStat.setFont(new java.awt.Font("Verdana", 1, 18));
        buttonAStat.setText("Assignment Stats");
        buttonAStat.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonAStat.setContentAreaFilled(false);
        buttonAStat.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonAStat.setBounds(20, 150, 200, 40);
        buttonAStat.setActionCommand(astatString);
        optionsPane.add(buttonAStat, javax.swing.JLayeredPane.DEFAULT_LAYER);

        buttonSStat.setFont(new java.awt.Font("Verdana", 1, 18));
        buttonSStat.setText("Student Stats");
        buttonSStat.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonSStat.setContentAreaFilled(false);
        buttonSStat.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonSStat.setBounds(450, 150, 200, 40);
        buttonSStat.setActionCommand(sstatString);
        optionsPane.add(buttonSStat, javax.swing.JLayeredPane.DEFAULT_LAYER);

        browseDescriptionLabel.setFont(new java.awt.Font("Verdana", 2, 12));
        browseDescriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        browseDescriptionLabel.setText("Browse errors by student ID and assignment number");
        browseDescriptionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        browseDescriptionLabel.setBounds(20, 70, 370, 70);
        optionsPane.add(browseDescriptionLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        assignmentDescriptionLabel.setFont(new java.awt.Font("Verdana", 2, 12));
        assignmentDescriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        assignmentDescriptionLabel.setText("View statistics on each assignment");
        assignmentDescriptionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        assignmentDescriptionLabel.setBounds(20, 190, 370, 70);
        optionsPane.add(assignmentDescriptionLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        studentDescriptionLabel.setFont(new java.awt.Font("Verdana", 2, 12));
        studentDescriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        studentDescriptionLabel.setText("View statistics on each student");
        studentDescriptionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        studentDescriptionLabel.setBounds(450, 190, 370, 70);
        optionsPane.add(studentDescriptionLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        errorDescriptionLabel.setFont(new java.awt.Font("Verdana", 2, 12));
        errorDescriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        errorDescriptionLabel.setText("View statistics on specific errors");
        errorDescriptionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        errorDescriptionLabel.setBounds(450, 70, 370, 70);
        optionsPane.add(errorDescriptionLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        optionsPane.setBounds(155, 220, 700, 270);
        centerPane.add(optionsPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        goButton.setFont(new java.awt.Font("Verdana", 1, 18));
        goButton.setText("Start");
        goButton.setActionCommand("start");
        goButton.setBounds(410, 530, 130, 30);
        centerPane.add(goButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        classLabel.setFont(new java.awt.Font("Verdana", 1, 30));
        //classLabel.setText("COMS W1004");
        classLabel.setBounds(370, 110, 240, 50);
        centerPane.add(classLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        buttonGroup.add(buttonBrowse);
        buttonGroup.add(buttonAStat);
        buttonGroup.add(buttonEStat);
        buttonGroup.add(buttonSStat);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(centerPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(centerPane, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );
        
       buttonListener = new ActionListener() 
            {
                public void actionPerformed(ActionEvent evt){
               
                    handleJButtonAction(evt);
            }
        };
        
        buttonBrowse.addActionListener(buttonListener);
        buttonAStat.addActionListener(buttonListener);
        buttonEStat.addActionListener(buttonListener);
        buttonSStat.addActionListener(buttonListener); 
        
        goButton.addActionListener(new ActionListener()
        {

                public void actionPerformed(ActionEvent evt){

                        handleGoButtonAction(evt);

                }

            }

           );
           
       
    }                 

    
   private void handleJButtonAction(ActionEvent evt){
        

        

    }
    
    private void handleGoButtonAction(ActionEvent evt){
        
        String selectedButton = buttonGroup.getSelection().getActionCommand();
        
        if(selectedButton.equals(browseString)){
             uimanager.showBrowsePane();
     
        }
        else if (selectedButton.equals(estatString)){
            
            
        }
        else if (selectedButton.equals(astatString)){
        	uimanager.showAssignPane();
            
            
        }
       else if (selectedButton.equals(sstatString)){
            
            
        }
   
    }
     
                
}
