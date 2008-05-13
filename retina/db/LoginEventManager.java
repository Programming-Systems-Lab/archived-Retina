package retina.db;

/**
 */

import java.sql.*;
import java.util.ArrayList;

import retina.common.Logger;


public class LoginEventManager extends DatabaseManager
{	// Constructor	public LoginEventManager(){}

	/**
	 * This method updates the "logins" table with the student and date.
	 */
	public boolean insertLoginEvent(String student, String date, String action)	{		try
		{
			// get a database connection			con = getConnection();
			if(con != null)			{				// create a Statement
				Statement stmt = con.createStatement();

				// now create the SQL query
				// TODO: use a PreparedStatement instead
				String query = "INSERT INTO login_events (student, date, action) VALUES ('" + student + "', '" + date + "', '" + action + "')";

				// execute the query
				stmt.executeUpdate(query);
				//System.out.println("Executed " + query);
				
				return true;
            }            else System.out.println("Error: No active Connection");
        }        catch(Exception e)        {			e.printStackTrace();			if (Logger.isLogError()) Logger.logError(e);
		}
		finally
		{
			closeConnection();
		}
		
		// if we get here, the command did not succeed
		return false;
	}

}