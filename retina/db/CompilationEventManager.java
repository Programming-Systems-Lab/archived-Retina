package retina.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import retina.common.CompilationEvent;
import retina.common.OccurrenceMap;

/**
 * This class holds all methods for getting info related to compilation events.
 *
 */


public class CompilationEventManager extends DatabaseManager 
{
	/**
	 * This method inserts an Event into the compilations table.
	 * That table has columns called student, assignment, date, and success.
	 * Return false if there is a database error.
	 */
	public boolean insertCompilationEvent(CompilationEvent e)
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
				String query = "INSERT INTO compilations (student, assignment, date, errors) VALUES ('" + e.getUser() + "', '" + e.getAssignment() + "', '" + e.getTime() + "', '" + e.getErrors() + "')";
				
				// execute the query
				stmt.executeUpdate(query);
				//System.out.println("Executed " + query);
			}
			else System.out.println("Error: No active Connection");
			
			// we need to add this student if he/she doesn't already exist
			if (getStudentName(e.getUser()) == null)
			{
				insertStudent(e.getUser(), "New Student");
				//System.out.println("Inserted " + e.getUser());
			}
			
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
	 * This method selects all CompilationEvents
	 * for the given user id and assignment id. It will
	 * return null if the user id or assignment id is invalid.
	 * It will throw an Exception if a database error occurs.
	 */
	public CompilationEvent[] getCompilationEvents(String user, String assignment) 
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT date, errors FROM compilations WHERE student = '" + user + "' AND assignment = '" + assignment + "' ORDER BY date";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				// we can't actually know the size of the ResultSet in advance
				// so we can't create an array... yet
				// use an ArrayList for now
				ArrayList<CompilationEvent> results = new ArrayList<CompilationEvent>();
				
				// iterate through the ResultSet and populate the ArrayList
				int count = 0;
				while (rs.next())
				{
					// get the "assignment" column for this particular result
					String date = rs.getString("date");
					int errors = rs.getInt("errors");
					results.add(new CompilationEvent(user, assignment, date, errors));
					count++;
				}
				
				// now return the results
				return results.toArray(new CompilationEvent[count]);
				
				
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
		CompilationEvent[] events = getCompilationEvents(user, assignment);
		if (events != null) return events.length;
		else return 0;
	}
	
	/**
	 * This method returns the number of successful compilations for a user on an assignment. 
	 */
	public int getNumberOfSuccessfulCompilations(String user, String assignment)
	{
		CompilationEvent[] events = getCompilationEvents(user, assignment);
		if (events != null) 
		{
			int total = 0;
			for (CompilationEvent e : events)
			{
				if (e.isSuccessful()) total++;
			}
			return total;
		}
		else return 0;
		
		
	}

	
	/**
	 * For the specified assignment, this returns an OccurrenceMap that details how many compilations occurred
	 * at different times of the day.
	 */
	public OccurrenceMap getCompilationTimes(String assignment)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "select datepart(mm, date), datepart(d, date), datepart(hh, compilations.date), count(*) from compilations where assignment = '" + assignment + "' group by datepart(mm, date), datepart(d, date), datepart(hh, compilations.date)";
				
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
		CompilationEventManager m = new CompilationEventManager();
		OccurrenceMap map = m.getCompilationTimes("1");
		for (int i = 0; i < map.keys.length; i++)
		{
			System.out.println(map.keys[i] + " " + map.occurrences[i]);
		}
	}

}
