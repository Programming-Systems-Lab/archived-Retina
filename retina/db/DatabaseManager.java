package retina.db;

/**
 * This class contains all the methods for getting info from and putting info into the database.
 * 
 * TODO: Break out read/write functionality into separate classes, or break it up based on functionality
 * TODO: Store query-related stuff (tables, columns, etc.) in a separate entry
 */

import java.sql.*;
import java.util.ArrayList;

import retina.common.Logger;


public class DatabaseManager extends DatabaseConnector
{	// Constructor	public DatabaseManager(){}

	/**
	 * This is just a toy method to insert into the "students" table.
	 * Use this as a starting point for any "insert" method.
	 * @return true if the insert succeeded, false otherwise
	 **/
	public boolean insertStudent(String uni, String name)	{		try
		{
			// get a database connection			con = getConnection();
			if(con != null)			{				// create a Statement
				Statement stmt = con.createStatement();

				// now create the SQL query
				// TODO: use a PreparedStatement instead
				String query = "INSERT INTO students (uni, name) VALUES ('" + uni + "', '" + name + "')";

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

	/**
	 * This is just a toy method to read from the "students" table.
	 * Use this as a starting point for any "get" method.
	 **/
	public String[] getStudentNames()
	{
		try
		{
			con = getConnection();

			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();

				// now create the SQL query
				String query = "SELECT name FROM students ORDER BY name";

				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);

				// we can't actually know the size of the ResultSet in advance
				// so we can't create an array... yet
				// use an ArrayList for now
				ArrayList<String> results = new ArrayList<String>();

				// iterate through the ResultSet and populate the ArrayList
				int count = 0;
				while (rs.next())
				{
					// get the "name" column for this particular result
					results.add(rs.getString("name"));
					count++;
				}

				// now return the results
				return results.toArray(new String[count]);
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


	/**
	 * This method returns the list of all student IDs.
	 * It's stored in the "students" table and the column is "uni".
	 */
	public String[] getStudents()
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT uni FROM students ORDER BY uni";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				// we can't actually know the size of the ResultSet in advance
				// so we can't create an array... yet
				// use an ArrayList for now
				ArrayList<String> results = new ArrayList<String>();
				
				// iterate through the ResultSet and populate the ArrayList
				int count = 0;
				while (rs.next())
				{
					// get the "uni" column for this particular result
					results.add(rs.getString("uni"));
					count++;
				}
				
				// now return the results
				return results.toArray(new String[count]);
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

	/**
	 * This method looks up a student's name, given their UNI
	 */
	public String getStudentName(String uni)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT name FROM students WHERE uni = '" + uni + "'";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				if (rs.next())
				{
					return rs.getString("name");
				}
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

	/**
	 * This method returns the list of all assignment IDs.
	 * It's stored in the "assignments" table and is called "assignment".
	 */
	public String[] getAssignments()
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT assignment FROM assignments ORDER BY assignment";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				// we can't actually know the size of the ResultSet in advance
				// so we can't create an array... yet
				// use an ArrayList for now
				ArrayList<String> results = new ArrayList<String>();
				
				// iterate through the ResultSet and populate the ArrayList
				int count = 0;
				while (rs.next())
				{
					// get the "assignment" column for this particular result
					results.add(rs.getString("assignment"));
					count++;
				}
				
				// now return the results
				return results.toArray(new String[count]);
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

	public static void main(String[] args)
	{
		DatabaseManager m = new DatabaseManager();
		m.insertStudent("foo", "bar");
		
	}
}