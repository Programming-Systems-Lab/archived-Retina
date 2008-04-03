package retina.common;
/**
 * 
 * Represents a student in the system.
 *
 */
public class Student 
{
	private String uni;
	private String name;
	
	public Student(String u, String n)
	{
		uni = u;
		name = n;
	}
	
	public String getUni()
	{
		return uni;
	}
	
	public String getName()
	{
		return name;
	}
}
