package retina.db;

/**
 * Defines methods that we currently use for reading from the database.
 */

import retina.common.CompilationErrorEvent;

public interface DatabaseReader
{
	/**
	 * This method returns the list of all student IDs.
	 */
	public String[] getStudents();


	/**
	 * This method returns the list of all assignment IDs.
	 */
	public String[] getAssignments();


	/**
	 * This method selects all CompilationErrorEvents
	 * for the given user id and assignment id. It will
	 * return null if the user id or assigment id is invalid.
	 * It will throw an Exception if a database error occurs.
	 */
	public CompilationErrorEvent[] getCompilationErrorEvents(String user, String assignment);


	/**
	 * This method gets the most common error for the particular student and assignment.
	 */
	public CompilationErrorEvent getMostCommonCompilationError(String user, String assignment);

	
	/**
	 * This method returns the total number of compilations for a user on an assignment. 
	 */
	public int getTotalNumberOfCompilations(String user, String assignment);
	
	/**
	 * This method returns the number of successful compilations for a user on an assignment. 
	 */
	public int getNumberOfSuccessfulCompilations(String user, String assignment);
	
}