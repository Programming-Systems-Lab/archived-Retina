package retina.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import retina.common.CompilationErrorEvent;
import retina.common.OccurrenceMap;

public class CompilationErrorEventManager extends CompilationEventManager 
{


	/**
	 * This method inserts an Event into the compilation_errors table.
	 * That table has columns called student, assignment, date, and error.
	 * Return false if there is a database error.
	 */
	public boolean insertCompilationErrorEvent(CompilationErrorEvent e)
	{
		try
		{
			// get a database connection
			con = getConnection();
			
			if(con != null)
			{
				// create a PreparedStatement
				PreparedStatement stmt = con.prepareStatement("INSERT INTO compilation_errors (student, assignment, date, error, message, filename, line) VALUES (?, ?, ?, ?, ?, ?, ?)");
				
				// now create the SQL query
				// String query = "INSERT INTO compilation_errors (student, assignment, date, error, message) VALUES ('" + e.getUser() + "', '" + assignment + "', '" + e.getTime() + "', '" + e.getError() + "', '" + e.getMessage() + "')";
				
				stmt.setString(1, e.getUser());
				stmt.setString(2, e.getAssignment());
				stmt.setString(3, e.getTime());
				stmt.setString(4, e.getError());
				stmt.setString(5, e.getMessage());
				stmt.setString(6, e.getFile());
				stmt.setString(7, e.getLine());
				
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
				String query = "SELECT date, error, message, filename, line FROM compilation_errors WHERE student = '" + user + "' AND assignment = '" + assignment + "' ORDER BY date";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
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
					String file = rs.getString("filename");
					String line = rs.getString("line");
					results.add(new CompilationErrorEvent(user, assignment, date, error, message, file, line));
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
					return new CompilationErrorEvent(user, assignment, "N/A", error, null, null, null);
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
	 * This method gets the most common errors for ALL students for the given assignment.
	 */
	public OccurrenceMap getMostCommonCompilationErrors(String assignment, int num)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT error, count(*) FROM compilation_errors WHERE assignment = '" + assignment + "' GROUP BY error ORDER BY count(*) DESC";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				// create the arrays
				OccurrenceMap map = new OccurrenceMap();
				map.keys = new String[num];
				map.occurrences = new int[num];
				
				// counts how many objects have been placed in the arrays
				int counter = 0;
				
				while (counter < num && rs.next())
				{
					map.keys[counter] = rs.getString(1);
					map.occurrences[counter] = rs.getInt(2);
					counter++;
				}

				return map;
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
	 * For the specified assignment, this returns an OccurrenceMap that details how many compilation errors occurred
	 * at different times of the day.
	 */
	public OccurrenceMap getCompilationErrorTimes(String assignment)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "select datepart(mm, date), datepart(d, date), datepart(hh, date), count(*) from compilation_errors where assignment = '" + assignment + "' group by datepart(mm, date), datepart(d, date), datepart(hh, date)";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				// we can't actually know the size of the ResultSet in advance
				// so we can't create an array... yet
				// use an ArrayList for now
				// TODO: can we be certain that the ArrayList will keep everything in the right order?
				ArrayList<String> times = new ArrayList<String>();
				ArrayList<Integer> events = new ArrayList<Integer>();
				
				// iterate through the ResultSet and populate the ArrayList
				int count = 0;
				while (rs.next())
				{
					String month = rs.getString(1);
					String day = rs.getString(2);
					String hour = rs.getString(3);
					String date = month + "-" + day + " " + hour + ":00";
					times.add(date);
					
					int compile = rs.getInt(4);
					events.add(compile);
					
					count++;
				}
				
				// now return the results
				OccurrenceMap map = new OccurrenceMap();
				map.keys = times.toArray(new String[count]);
				map.occurrences = new int[count];
				for (int i = 0; i < count; i++)
				{
					map.occurrences[i] = events.get(i).intValue();
				}
				
				return map;
				
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

	
	public static void main(String[] args)
	{
		CompilationErrorEventManager m = new CompilationErrorEventManager();
		OccurrenceMap map = m.getCompilationErrorTimes("1");
		for (int i = 0; i < map.keys.length; i++)
		{
			System.out.println(map.keys[i] + " " + map.occurrences[i]);
		}
	}
}
