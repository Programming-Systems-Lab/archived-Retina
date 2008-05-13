package retina.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import retina.common.CompilationEvent;
import retina.common.Logger;
import retina.common.OccurrenceMap;
import retina.ui.DateConverter;

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
			if (Logger.isLogError()) Logger.logError(e);
		}
		finally
		{
			closeConnection();
		}	
		return null;
	}
	
	/**
	 * This method calculates the working time for ALL students for the given assignment.
	 */
	public String[][] getWorkingTimes(String assignment)
	{
        DateConverter dc = new DateConverter();
		
        // the array of students
		String[] students = getStudents();
		
		// the array of times... times[i][0] is the hours, times[i][1] is the minutes
		long[][] times = new long[students.length][2];
		
		// to keep track of the times in minutes, for comparison
		long[] totalTimes = new long[students.length];
		
		
		// get all the working times
		for (int i = 0; i < students.length; i++)
		{
			// set the 2d "times" array
			times[i] = dc.computeWorkTime(getCompilationEvents(students[i], assignment));
			// set the total time
			totalTimes[i] = times[i][0] * 60 + times[i][1];
		}
		

		// now sort by the total times, but remember to also move the student names and the hour/minute times around, too
		// TODO: the names are not sorted when the values are the same

		for (int i = 0; i < totalTimes.length; i++)
		{
			for (int j = totalTimes.length-1; j > i; j--)
			{
				if (totalTimes[j-1] < totalTimes[j])
				{
					// swap the values
					String temp = students[j];
					students[j] = students[j-1];
					students[j-1] = temp;
					
					long tmp = totalTimes[j];
					totalTimes[j] = totalTimes[j-1];
					totalTimes[j-1] = tmp;
					
					long[] t = times[j];
					times[j] = times[j-1];
					times[j-1] = t;
					
				}
			}
		}
		
		/* at this point, the arrays are sorted based on what was in totalTimes */
		
		// this is the array that holds the string representation of the working times
		String[] workTimes = new String[students.length]; 
		
		for (int i = 0; i < students.length; i++){
			String result = ""; 
			if(times[i][0] == 0){
				result = "Less Than 1 Hour";
			}
			else{
				result = "Approx. " + (int)times[i][0] + " hrs. and " + (int)times[i][1] + " mins.";
			}
			workTimes[i] = result;
		}

		// this is the return value... retValue[0][i] is the name of the student, retValue[1][i] is the corresponding working time
		String[][] retValue = new String[2][students.length];
		retValue[0] = students;
		retValue[1] = workTimes;
		return retValue; 
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
					
					int fillInCounter = 0;
					// this part figures out if we need to fill in any empty times
					if (Integer.parseInt(hour) - Integer.parseInt(lastHour) > 1)
					{
						// in this part, we're just assuming it's the same day
						// but with some hours in which there were no errors
						
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
						// we assume it's only been one day
						
						// first, fill in for the rest of the previous day
						fillInCounter = 23 - Integer.parseInt(lastHour);
						for (int i = 1; i <= fillInCounter; i++)
						{
							int tempHour = Integer.parseInt(lastHour) + i;
							times.add(lastMonth + "-" + lastDay + " " + tempHour + ":00");
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
					count += fillInCounter + 1;
					
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
	 * For the specified assignment and a specified student id, this returns an OccurrenceMap that details how many compilations occurred
	 * at different times of the day.
	 */
	public OccurrenceMap getCompilationTimes(String assignment, String student)
	{
		try
		{
			con = getConnection();
			
			if (con != null)
			{
				// create a Statement
				Statement stmt = con.createStatement();
				
				// now create the SQL query
				String query = "select datepart(mm, date), datepart(d, date), datepart(hh, date), count(*) from compilations where assignment = '" + assignment + "' and student = '" + student + "' group by datepart(mm, date), datepart(d, date), datepart(hh, compilations.date)";
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
					
					int fillInCounter = 0;
					// this part figures out if we need to fill in any empty times
					if (Integer.parseInt(hour) - Integer.parseInt(lastHour) > 1)
					{
						// in this part, we're just assuming it's the same day
						// but with some hours in which there were no errors
						
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
						// we assume it's only been one day
						
						// first, fill in for the rest of the previous day
						fillInCounter = 23 - Integer.parseInt(lastHour);
						for (int i = 1; i <= fillInCounter; i++)
						{
							int tempHour = Integer.parseInt(lastHour) + i;
							times.add(lastMonth + "-" + lastDay + " " + tempHour + ":00");
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
					count += fillInCounter + 1;
					
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
		CompilationEventManager m = new CompilationEventManager();
		OccurrenceMap map = m.getCompilationTimes("4");
		for (int i = 0; i < map.keys.length; i++)
		{
			System.out.println(map.keys[i] + " " + map.occurrences[i]);
		}
	}

}
