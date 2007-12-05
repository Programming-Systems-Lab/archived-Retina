package retina.db;

/**
 * This class contains all the methods for getting info from and putting info into the database.
 * 
 * TODO: Break out read/write functionality into separate classes, or break it up based on functionality
 * TODO: Store query-related stuff (tables, columns, etc.) in a separate entry
 */

import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;

import retina.common.CompilationErrorEvent;
import retina.common.CompilationEvent;
import retina.common.Event;

public class DatabaseManager extends DatabaseConnector implements DatabaseReader, DatabaseWriter
{
	// Constructor
	public DatabaseManager(){}

	/**
	 * This is just a toy method to insert into the "students" table.
	 * Use this as a starting point for any "insert" method.
	 * @return true if the insert succeeded, false otherwise
	 **/
	public boolean insertStudent(String uni, String name)
	{
		try
		{
			// get a database connection
			con = getConnection();
			if(con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();

				// now create the SQL query
				// TODO: use a PreparedStatement instead
				String query = "INSERT INTO students (uni, name) VALUES ('" + uni + "', '" + name + "')";

				// execute the query
				stmt.executeUpdate(query);
				//System.out.println("Executed " + query);
				
				return true;
            }
            else System.out.println("Error: No active Connection");
        }
        catch(Exception e)
        {
			e.printStackTrace();
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
		}
		finally
		{
			closeConnection();
		}	
		return null;
	}



	/**
	 * This method inserts an event into the appropriate
	 * table in the database. If successful, it returns true.
	 * If an error occurs, it returns false.
	 */
	public boolean insertEvent(Event e)
	{
		if (e instanceof CompilationErrorEvent)
		{
			return insertCompilationErrorEvent((CompilationErrorEvent)e);
		}
		else if (e instanceof CompilationEvent)
		{
			return insertCompilationEvent((CompilationEvent)e);
		}
		return true;
	}

	/**
	 * This method inserts an Event into the compilation_errors table.
	 * That table has columns called student, assignment, date, and error.
	 * Return false if there is a database error.
	 */
	private boolean insertCompilationErrorEvent(CompilationErrorEvent e)
	{
		try
		{
			// get a database connection
			con = getConnection();
			
			if(con != null)
			{
				// create a PreparedStatement
				PreparedStatement stmt = con.prepareStatement("INSERT INTO compilation_errors (student, assignment, date, error, message) VALUES (?, ?, ?, ?, ?)");
				
				// now create the SQL query
				// String query = "INSERT INTO compilation_errors (student, assignment, date, error, message) VALUES ('" + e.getUser() + "', '" + assignment + "', '" + e.getTime() + "', '" + e.getError() + "', '" + e.getMessage() + "')";
				
				stmt.setString(1, e.getUser());
				
				// TODO: what if we don't know the assignment number??
				stmt.setString(2, e.getAssignment().equals("unknown") ? "1" : e.getAssignment());
				
				stmt.setString(3, e.getTime());
				stmt.setString(4, e.getError());
				stmt.setString(5, e.getMessage());
				
				// execute the query
				stmt.executeUpdate();
				
			}
			else System.out.println("Error: No active Connection");
			
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		return true;
	}

	
	/**
	 * This method inserts an Event into the compilations table.
	 * That table has columns called student, assignment, date, and success.
	 * Return false if there is a database error.
	 */
	private boolean insertCompilationEvent(CompilationEvent e)
	{
		try
		{
			// get a database connection
			con = getConnection();
			
			if(con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String success = e.isSuccessful() ? "TRUE" : "FALSE";
				// TODO: how do we get the assignment?
				String assignment = e.getAssignment().equals("unknown") ? "1" : e.getAssignment();

				String query = "INSERT INTO compilations (student, assignment, date, success) VALUES ('" + e.getUser() + "', '" + assignment + "', '" + e.getTime() + "', '" + success + "')";
				
				// execute the query
				stmt.executeUpdate(query);
				//System.out.println("Executed " + query);
			}
			else System.out.println("Error: No active Connection");
			
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		return true;
	}
	
	
	/**
	 * This method selects all CompilationErrorEvents
	 * for the given user id and assignment id. It will
	 * return null if the user id or assignment id is invalid.
	 * It will throw an Exception if a database error occurs.
	 */
	public CompilationErrorEvent[] getCompilationErrorEvents(String user, String assignment) 
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT date, error, message FROM compilation_errors WHERE student = '" + user + "' AND assignment = '" + assignment + "' ORDER BY assignment";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				// System.out.println("Executed " + query);
				
				// we can't actually know the size of the ResultSet in advance
				// so we can't create an array... yet
				// use an ArrayList for now
				ArrayList<CompilationErrorEvent> results = new ArrayList<CompilationErrorEvent>();
				
				// iterate through the ResultSet and populate the ArrayList
				int count = 0;
				while (rs.next())
				{
					// get the "assignment" column for this particular result
					String date = rs.getString("date");
					String error = rs.getString("error");
					String message = rs.getString("message");
					results.add(new CompilationErrorEvent(user, assignment, date, error, message));
					count++;
				}
				
				// now return the results
				return results.toArray(new CompilationErrorEvent[count]);
				
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}	
		return null;
	}

	/**
	 * This method gets the most common error for the particular student and assignment.
	 */
	public CompilationErrorEvent getMostCommonCompilationError(String user, String assignment)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT error, count(*) FROM compilation_errors WHERE student = '" + user + "' and assignment = '" + assignment +"' GROUP BY error ORDER BY count(*) DESC";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
		
				// TODO: what if there's more than one?
				if (rs.next())
				{
					// get the "assignment" column for this particular result
					String error = rs.getString("error");
					return new CompilationErrorEvent(user, assignment, "N/A", error, null);
				}
				else
					return null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}	
		return null;
	}
	
	
	/**
	 * This method returns the total number of compilations for a user on an assignment. 
	 */
	public int getTotalNumberOfCompilations(String user, String assignment)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT count(*) FROM compilations WHERE student = '" + user + "' and assignment = '" + assignment +"'";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				if (rs.next())
				{
					// just return the number of compilations
					return rs.getInt(1);
				}
				else
					return 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}	
		return 0;
		
	}
	
	/**
	 * This method returns the number of successful compilations for a user on an assignment. 
	 */
	public int getNumberOfSuccessfulCompilations(String user, String assignment)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT count(*) FROM compilations WHERE student = '" + user + "' and assignment = '" + assignment +"' and success = 'true'";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				if (rs.next())
				{
					// just return the number of compilations
					return rs.getInt(1);
				}
				else
					return 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}	
		return 0;
		
		
	}


	public static void main(String[] args)
	{
		DatabaseManager dm = new DatabaseManager();
		
		/*
		// this tests the getCompilationErrorEvents method
		CompilationErrorEvent[] errors = dm.getCompilationErrorEvents("cdm6", "1");
		for (CompilationErrorEvent e : errors)
		{
			System.out.println(e.getTime() + " " + e.getErrorMessage());
		}
		*/
		
		/*
		// test the getMostCommonCompilationError method
		CompilationErrorEvent e = dm.getMostCommonCompilationError("sh2503", "2");
		System.out.println(e.getErrorMessage());
		*/
		
		/*
		// test the getTotalNumberOfCompilations and getNumberOfSuccessfulCompilations methods
		int num = dm.getTotalNumberOfCompilations("sh2503", "2");
		System.out.println("Total " + num);
		num = dm.getNumberOfSuccessfulCompilations("sh2503", "2");
		System.out.println("Successful " + num);
		*/
		
	}
	
	/** DON'T RUN THIS METHOD! It's just for testing purposes but it modifies the database! **/
	private void test()
	{
		DatabaseManager myDbTest = new DatabaseManager();
		CompilationErrorEvent event = new CompilationErrorEvent("hwl2102", "2", "2500", "error", "message");

		// test the insertStudent method
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter an ID: ");
		String id = scan.nextLine();
		System.out.print("Enter a name: ");
		String name = scan.nextLine();
		myDbTest.insertStudent(id, name);
		System.out.println("Done inserting " + name);

		// test the getStudentnames method
		String[] students = myDbTest.getStudentNames();
		System.out.println("Here are the students");
		for (String student : students) System.out.println(student);

		//test the getAssignments method
		String[] assignments = myDbTest.getAssignments();
		System.out.println("Here are the assignments");
		for(String assignment : assignments) System.out.println(assignment);

		// test the getStudents method
		String[] unis = myDbTest.getStudents();
		System.out.println("Here are the unis");
		for (String uni : unis) System.out.println(uni);

		// test insertComilationError method
		myDbTest.insertCompilationErrorEvent(event);
		System.out.println("Done inserting: " + event.getError() + event.getUser() + event.getAssignment() + event.getTime());


	}
}