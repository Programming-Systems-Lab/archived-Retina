package retina.db;

/**
 */

import java.sql.*;
import java.util.ArrayList;


public class LoginEventManager extends DatabaseManager
{

	/**
	 * This method updates the "logins" table with the student and date.
	 */
	public boolean insertLoginEvent(String student, String date, String action)
		{
			// get a database connection
			if(con != null)
				Statement stmt = con.createStatement();

				// now create the SQL query
				// TODO: use a PreparedStatement instead
				String query = "INSERT INTO login_events (student, date, action) VALUES ('" + student + "', '" + date + "', '" + action + "')";

				// execute the query
				stmt.executeUpdate(query);
				//System.out.println("Executed " + query);
				
				return true;
            }
        }
		finally
		{
			closeConnection();
		}
		
		// if we get here, the command did not succeed
		return false;
	}

}