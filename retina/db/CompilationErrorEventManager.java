package retina.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import retina.common.CompilationErrorEvent;
import retina.common.OccurrenceMap;
import retina.ui.DateConverter;
import retina.common.Logger;

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
			if (Logger.isLogError()) Logger.logError(e, error);
			return false;
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
			if (Logger.isLogError()) Logger.logError(e);
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
			if (Logger.isLogError()) Logger.logError(e);
		}
		finally
		{
			closeConnection();
		}	
		return null;
	}
	
	/**
	 * This method gets the number of total compilation errors for ALL students for the given assignment.
	 */
	public OccurrenceMap getTotalNumberOfCompilationErrors(String assignment)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "SELECT student, count(*) FROM compilation_errors WHERE assignment = '" + assignment + "' GROUP BY student ORDER BY count(*) DESC";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				ArrayList<String> ids = new ArrayList<String>();
				ArrayList<Integer> occurrences = new ArrayList<Integer>(); 
				
				// iterate through the ResultSet and populate the ArrayList
				int count = 0;
				while (rs.next())
				{
					ids.add(rs.getString(1));
					occurrences.add(rs.getInt(2));
					
					count++;
				}
				
				// now return the results
				OccurrenceMap map = new OccurrenceMap();
				map.keys = ids.toArray(new String[count]);
				map.occurrences = new int[count];
				for (int i = 0; i < count; i++)
				{
					map.occurrences[i] = occurrences.get(i).intValue();
				}
				
				return map;
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
			if (Logger.isLogError()) Logger.logError(e);
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
				
				return calculateTimes(rs);
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
	 * For the specified assignment, this returns an OccurrenceMap that details how many compilation errors occurred
	 * at different times of the day.
	 */
	public OccurrenceMap getCompilationErrorTimes(String assignment, String student)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "select datepart(mm, date), datepart(d, date), datepart(hh, date), count(*) from compilation_errors where assignment = '" + assignment + "' and student = '" + student + "' group by datepart(mm, date), datepart(d, date), datepart(hh, date)";
				
				// execute the query
				ResultSet rs = stmt.executeQuery(query);
				//System.out.println("Executed " + query);
				
				// just call the helper method
				return calculateTimes(rs);
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


	/*
	 * This is a helper method that takes a ResultSet from a query used to get times when something happened,
	 * and then it creates an occurrence map that matches the times (grouped by hour) to the number of occurrences.
	 */
	private OccurrenceMap calculateTimes(ResultSet rs) throws Exception
	{
		
		// we can't actually know the size of the ResultSet in advance
		// so we can't create an array... yet
		// use an ArrayList for now
		// TODO: can we be certain that the ArrayList will keep everything in the right order?
		ArrayList<String> times = new ArrayList<String>();
		ArrayList<Integer> events = new ArrayList<Integer>();
		
		// iterate through the ResultSet and populate the ArrayList
		int count = 0; // count the number of values so we know how big to make the arrays in the OccurrenceMap
		
		// these keep track of the last value, in case we need to fill in any blanks
		String lastHour = null;
		String lastDay = null;
		String lastMonth = null;
		
		while (rs.next())
		{
			String month = rs.getString(1);
			String day = rs.getString(2);
			String hour = rs.getString(3);
			
			// in case they're null
			if (lastHour == null) lastHour = hour;
			if (lastDay == null) lastDay = day;
			if (lastMonth == null) lastMonth = month;
			
			int missingDays = 0;
			
			int fillInCounter = 0;
			// this part figures out if we need to fill in any empty times
			if (Integer.parseInt(hour) - Integer.parseInt(lastHour) > 1 && day.equals(lastDay))
			{
				// in this part, there are some hours in which there were no errors
				
				// figure out the number of spots to fill in
				fillInCounter = Integer.parseInt(hour) - Integer.parseInt(lastHour) - 1;
				// for each one, insert a time stamp
				for (int i = 1; i <= fillInCounter; i++)
				{
					int tempHour = Integer.parseInt(lastHour) + i;
					times.add(month + "-" + day + " " + tempHour + ":00");
				}
			}
			else if (Integer.parseInt(hour) < Integer.parseInt(lastHour) || (Integer.parseInt(day) != Integer.parseInt(lastDay) && Integer.parseInt(hour) != 0))
			{
				// in this part, we notice that the current hour is less than the previous, so a day must have changed
				
				// first, fill in for the rest of the previous day
				fillInCounter = 23 - Integer.parseInt(lastHour);
				for (int i = 1; i <= fillInCounter; i++)
				{
					int tempHour = Integer.parseInt(lastHour) + i;
					times.add(lastMonth + "-" + lastDay + " " + tempHour + ":00");
				}

				// see if it's been more than one day
				missingDays = Integer.parseInt(day) - Integer.parseInt(lastDay) - 1;
				//System.out.println(missingDays);

				// fill in any missing days
				// TODO: this doesn't work across months
				for (int i = 1; i <= missingDays; i++)
				{
					for (int j = 0; j < 24; j++)
					{
						times.add(lastMonth + "-" + (Integer.parseInt(lastDay)+i) + " " + j + ":00");
						events.add(0);
					}
				}
				
				// now we do it for the rest of the current day
				for (int i = 0; i < Integer.parseInt(hour); i++)
				{
					times.add(month + "-" + day + " " + i + ":00");
				}
				fillInCounter += Integer.parseInt(hour);
			}
			
			// now put in the one we just read from the database
			String date = month + "-" + day + " " + hour + ":00";
			times.add(date);
			
			// if we had any fillins, put zeroes in the events ArrayList
			for (int i = 0; i < fillInCounter; i++)
				events.add(0);
			
			// now put in the one we just read from the database
			int compile = rs.getInt(4);
			events.add(compile);
			
			// update the counter
			count += fillInCounter + 1 + (missingDays * 24);
			
			// update the values
			lastHour = hour;
			lastDay = day;
			lastMonth = month;
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
	
	public static void main(String[] args)
	{
		CompilationErrorEventManager m = new CompilationErrorEventManager();
		OccurrenceMap map = m.getCompilationErrorTimes("4");
		for (int i = 0; i < map.keys.length; i++)
		{
			System.out.println(map.keys[i] + " " + map.occurrences[i]);
		}
	}
}
