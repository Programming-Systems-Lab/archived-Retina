package retina.db;
import java.sql.*;

import retina.common.Logger;
import retina.common.Student;

/***********************************************************************************
 * 
 * This class holds all the code for dealing with suggestions of networks to join,
 * people to get in touch with, etc.
 * 
 * @author cmurphy
 *
 **********************************************************************************/


public class SuggestionManager extends DatabaseManager 
{
	public SuggestionManager() { }

	
	
	/**
	 *  For the user specified in the parameter, this method tries to find another
	 *  user who has made similar errors.
	 */
	public Student suggestFriend(String user)
	{
		try
		{
			con = getConnection();

			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();

				// now create the SQL query
				String query = "SELECT uni, name FROM students WHERE uni <> '" + user + "'";

				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);

				if (rs.next())
				{
					// get the "name" column for this particular result
					return new Student(rs.getString("uni").trim(), rs.getString("name").trim());
				}
				else return null;
			}
		}
        catch(Exception e)
        {
			e.printStackTrace();
			if (Logger.isLogError()) Logger.logError(e);
		}
		finally
		{
			closeConnection();
		}

		return null;
	}
}
