package retina.common;

/**
 * This is just a class that holds an array of Strings and an array of ints. The nth string
 * is mapped to the nth int, indicating that it has that number of occurrences.
 */


public class OccurrenceMap 
{
	public String[] keys;
	public int[] occurrences;
	
	public int size() { return keys.length; }
}
