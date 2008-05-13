package retina.common;

/**
 * This is just a class that holds an array of Strings and an array of ints. The nth string
 * is mapped to the nth int, indicating that it has that number of occurrences.
 */


public class OccurrenceMap 
{
	public OccurrenceMap(String[] k, int[] o)
	{
		keys = k;
		occurrences = o;
	}
	
	public OccurrenceMap()
	{
		
	}
	
	public String[] keys;
	public int[] occurrences;
	
	public int size() { return keys.length; }
	
	/**
	 * This method modifies the occurrence map by sorting the keys and their occurrences.
	 */
	public void sortKeys()
	{
		for (int i = 0; i < keys.length-1; i++)
		{
			String min = keys[i];
			int minIndex = i;
			for (int j = i+1; j < keys.length; j++)
			{
				if (keys[j].compareTo(min) < 0)
				{
					min = keys[j];
					minIndex = j;
				}
			}
			String temp = keys[i];
			keys[i] = keys[minIndex];
			keys[minIndex] = temp;
			
			int tmp = occurrences[i];
			occurrences[i] = occurrences[minIndex];
			occurrences[minIndex] = tmp;
		}
	}

	/**
	 * This method modifies the occurrence map by sorting occurrence values and their keys.
	 */
	public void sortValues()
	{
		for (int i = 0; i < occurrences.length-1; i++)
		{
			int min = occurrences[i];
			int minIndex = i;
			for (int j = i+1; j < occurrences.length; j++)
			{
				if (occurrences[j] < min)
				{
					min = occurrences[j];
					minIndex = j;
				}
			}
			String temp = keys[i];
			keys[i] = keys[minIndex];
			keys[minIndex] = temp;
			
			int tmp = occurrences[i];
			occurrences[i] = occurrences[minIndex];
			occurrences[minIndex] = tmp;
		}
	}
	
	
	
	
	public static void main(String[] args)
	{
		String[] keys = { "C", "D", "B", "E", "A" };
		int[] vals = { 5, 2, 1, 8, 0 };
		OccurrenceMap map = new OccurrenceMap(keys, vals);

		map.sortKeys();
		
		for (int i = 0; i < map.size(); i++)
		{
			System.out.println(map.keys[i] + " " + map.occurrences[i]);
		}
	}
}
