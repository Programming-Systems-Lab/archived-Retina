package retina.db;/** * This class has all the base functionality for managing a database connection, like * connecting and closing it. */
public class DatabaseConnector
{	 
	protected java.sql.Connection  con = null;     	private final String url = "jdbc:sqlserver://";	private final String serverName= "vesey.cs.columbia.edu";	private final String portNumber = "1433";	private final String databaseName= "Retina";	private final String userName = "student";	private final String password = "password";	private final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    // Constructor    public DatabaseConnector(){}
    /* This method just creates the connection string based on all the properties. */
    private String getConnectionUrl()    {    	// return url+serverName+":"+portNumber+";databaseName="+databaseName+";selectMethod="+selectMethod+";";    	return url+serverName+":"+portNumber+";user="+userName+";password="+password+";databaseName="+databaseName;    }
    /* Create the database connection */    protected java.sql.Connection getConnection()    {    	try    	{    		Class.forName(driver);    		con = java.sql.DriverManager.getConnection(getConnectionUrl(),userName,password);            //if(con!=null) System.out.println("Connection Successful!");    	}    	catch (Exception e)    	{    		System.out.println("Error Trace in getConnection() : " + e.getMessage());    		e.printStackTrace();    	}        return con;    }

    /* Close the connection to the database. */    protected void closeConnection()    {    	try    	{    		if (con != null) con.close();        }	    	catch (Exception e)    	{    		// if an error occurs, well, what can you do?    	}    	finally    	{    		con = null;    	}    }
    /* Test method to display the driver properties and database details */    public void displayDbProperties()    {    	java.sql.DatabaseMetaData dm = null;    	java.sql.ResultSet rs = null;    	try{    		con= this.getConnection();    		if(con!=null){    			dm = con.getMetaData();    			System.out.println("Driver Information");    			System.out.println("\tDriver Name: "+ dm.getDriverName());    			System.out.println("\tDriver Version: "+ dm.getDriverVersion ());    			System.out.println("\nDatabase Information ");    			System.out.println("\tDatabase Name: "+ dm.getDatabaseProductName());    			System.out.println("\tDatabase Version: "+ dm.getDatabaseProductVersion());    			System.out.println("Avalilable Catalogs ");    			rs = dm.getCatalogs();    			while(rs.next()){    				System.out.println("\tcatalog: "+ rs.getString(1));    			}	    			rs.close();    			rs = null;    			closeConnection();    		}else System.out.println("Error: No active Connection");    	}catch(Exception e){    		e.printStackTrace();    	}    	dm=null;    }
	public static void main(String[] args)
	{
		DatabaseConnector dc = new DatabaseConnector();
		dc.displayDbProperties();
	}

}