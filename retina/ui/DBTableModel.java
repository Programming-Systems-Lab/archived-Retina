package retina.ui;

/**
 * This represents the data to be shown in the table in the BrowsePane
 */

import javax.swing.table.AbstractTableModel;

import retina.common.CompilationErrorEvent;
import retina.common.RuntimeErrorEvent;
import retina.common.Event;

public class DBTableModel extends AbstractTableModel
{
    private String[] columnNames = {"Error", "Message", "Time", "File", "Line"}; 
    private Object[][] data;
    final int COLUMN_MAX = 5; 
    
    /*CONSTRUCTOR METHODS*/
    /** 
     *Constructs an instance of a DBTableModel when passed the user id and assignment number*
     *@param id User ID
     *@param assignemtn Assignment number
     */
    public DBTableModel(String id, String assignment){
        
        if(id.equals("xyx7890") && assignment.equals("2")){
        data = new Object [][]{{"Error1", "21/06/87", "13:45", "sh2503"}, 
        {"Error2", "17/6/87", "12:45", "pol1722"}, 
        {"Error3", "05/05/77", "18:15", "xdy3821"},
        {"Error1", "12/7/81", "19:12", "hjd2847"}};
        }
        else if(id.equals("abc1234") && assignment.equals("1")){
        data = new Object[][]{{"Error4", "11/08/12", "12:34", "kol121"},
        {"Error7", "15/10/44", "16:13", "mas2114"}, 
        {"Error9", "12/12/32","12:12", "ofi7664"}, 
        {"DOES THIS TABLE AUTO-ADJUST TO THE LENGTH OF THE TEXT IN THE FIRST COLUMN?", "14/03/07", "08:13", "tea1766"}};
        }
        else{
             System.out.println("Error checking required. No data found\n");
        }

    } 
    
    /**
     *Constructs a DBTableModel by initializing and populating 2D data array 
     *@param Event[] e 
     */
    public DBTableModel(Event[] e){
        int row, col = 0; 
        if (e instanceof CompilationErrorEvent[]){
        CompilationErrorEvent[] ce = (CompilationErrorEvent[])e; 
        data = new Object[ce.length][COLUMN_MAX];
        for(row = 0; row < ce.length; row++){    
            data[row][col++] = ce[row].getError(); 
            data[row][col++] = ce[row].getMessage(); 
            data[row][col++] = ce[row].getTime(); 
            data[row][col++] = ce[row].getFile(); 
            data[row][col++] = ce[row].getLine(); 
            // data[row][col++] = ce[row].getUser(); 
            col = 0; 
           
        }
       }
        else if (e instanceof RuntimeErrorEvent[]){
        	RuntimeErrorEvent[] ce = (RuntimeErrorEvent[])e; 
            data = new Object[ce.length][COLUMN_MAX];
            for(row = 0; row < ce.length; row++){    
                data[row][col++] = ce[row].getError(); 
                data[row][col++] = ce[row].getMessage(); 
                data[row][col++] = ce[row].getTime(); 
                data[row][col++] = ce[row].getFile(); 
                data[row][col++] = ce[row].getLine(); 
                // data[row][col++] = ce[row].getUser(); 
                col = 0; 
               
            }
           }
       else if (e == null){
            data = new Object[1][COLUMN_MAX];
            data[0][col++] = ""; 
            data[0][col++] = "";
            data[0][col++] = ""; 
         
            
       }
            
            
       
    }
    
    /*Accessor Methods*/ 
    public int getRowCount(){
        return data.length;
    }
    
    public int getColumnCount(){
        return columnNames.length; 
    }
    
    public Object getValueAt(int row, int column){
        return data[row][column];
    }
    
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }
    
}
