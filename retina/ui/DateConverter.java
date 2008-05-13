package retina.ui;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.*;

import retina.common.CompilationEvent;

public class DateConverter {

	private final int timeCap = 3600;		 //units = "seconds"
	private long workTime = 0;
	private DateFormat formatter; 
	private Pattern datePattern1, datePattern2;
	private String dateRegex1, dateRegex2; 
	
	public DateConverter(){
		dateRegex1 = "\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}";
		dateRegex2 = "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d";

		datePattern1 = Pattern.compile(dateRegex1); //2-8 18:00
		datePattern2 = Pattern.compile(dateRegex2); //2008-02-08 12:51:23.0
		
		
	}
	public int dateToInt(String date){
		Matcher m1 = datePattern1.matcher(date);
		Matcher m2 = datePattern2.matcher(date);
		
		if(m1.matches()){
			StringTokenizer st = new StringTokenizer(date, "\\d-\\d \\d:\\d");
			String newdate = "";
			while(st.hasMoreTokens()){
				String temp = st.nextToken();
				if(temp.length() < 2){
					temp = "0" + temp; 
				}
				newdate += temp;
	        
	        
				}
			return Integer.parseInt(newdate);
		}
		else if(m2.matches()){
				StringTokenizer st = new StringTokenizer(date, "\\d-\\d-\\d \\d:\\d:\\d");
				String newdate = "";
				while(st.hasMoreTokens()){
					String temp = st.nextToken();		
					if(!temp.matches("\\d{4}")){
						if(temp.length() < 2){
							temp = "0" + temp; 
						}
						newdate += temp;
					}
				}
				newdate = newdate.substring(0, 10);
				return Integer.parseInt(newdate);
				
		}
		else{ 
			return 0; 
		}
	    
	}
	
	public long[] computeWorkTime(CompilationEvent[] errorEvents){
		
		long[] outputWorkTime = new long[3];
		
		try{
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int i=0;
		
		for(int j=i+1; j<errorEvents.length; j++) {
			String login = errorEvents[i].getTime();
			String logout = errorEvents[j].getTime();			
	        Date dateLogin = (Date)formatter.parse(login);
	        Date dateLogout = (Date)formatter.parse(logout);
	        
	        long startVal = dateLogin.getTime();
	        long endVal = dateLogout.getTime();
	        long loginSec = (endVal-startVal)/1000;
	        if (loginSec < timeCap) {
	        	workTime += loginSec;
	        }
	        
	        i++;
		} //end for-loop
		
		/** This converts the total documented workTime of a student from seconds
		 *  into hours, minutes, and seconds. It is then printed as a time estimate.
		 */
		long hours = (workTime-workTime%3600)/3600;
        workTime = workTime-(hours*3600);
        long minutes = (workTime-workTime%60)/60;
        workTime = workTime-(minutes*60);
        long seconds = workTime;
        
     /*   String[] outputWorkTime = new String[3];
        outputWorkTime[0] = String.valueOf(hours); 
        outputWorkTime[1] = String.valueOf(minutes);
        outputWorkTime[2] = String.valueOf(seconds); */
        
        outputWorkTime[0] = hours;
        outputWorkTime[1] = minutes;
        outputWorkTime[2] = seconds; 
       	} //end try{}
		catch (Exception e) { System.out.println("Error: "+e); }
		
		return outputWorkTime;
	}
}
