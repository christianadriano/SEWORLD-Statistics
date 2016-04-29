package parser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostDate {

	Integer dayOfMonth;
	
	Integer weekOfYear;
	
	Integer month;
	
	Integer year;
	
	Integer hour;
	
	public Integer minute;
	
	Integer AmPm;
	
	String timeZone;
	
	Date date;
	
	DateFormat format;
	
	public PostDate(Date date, Post post){
		this.date = date;
	}
	
	public PostDate(String dateStr, Post post){
	
		// Fri, 18 Mar 2016 21:39:19 +0000
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		
		dateStr = trimBeginning(dateStr);
		
		try {
			this.date = df.parse(dateStr);
			extractDateElements(this.date);
		} catch (ParseException e) {
			System.out.println("Date: "+dateStr);
			System.out.println("Post: "+post.filePath);
			e.printStackTrace();
		}
	}
		
	
	private String trimBeginning(String dateStr) {
		
		int i=0;
		while(dateStr.charAt(i)==' ' && i<dateStr.length()){
			i++;
		}

		if(i>0){
			dateStr = dateStr.substring(i,dateStr.length());
		}

		return dateStr;
	}


	private void extractDateElements(Date date){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		this.month = calendar.get(Calendar.MONTH);
		this.year = calendar.get(Calendar.YEAR);
		this.hour = calendar.get(Calendar.HOUR);
		this.minute = calendar.get(Calendar.MINUTE);
		this.AmPm = calendar.get(Calendar.AM_PM);
		this.weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		this.timeZone = new Integer(calendar.get(Calendar.ZONE_OFFSET)/60000).toString();
	}


	 
	
	public static Double computeDifferenceHours(Date sentDate,
			Date receivedDate) {
		
		long diffInMilliseconds = sentDate.getTime() - receivedDate.getTime();
		double diffInHours =  diffInMilliseconds / (1000*60*60);
		return diffInHours; 
	}
	
	
	public String toString(){
		
		// Fri, 18 Mar 2016 21:39:19 +0000
		DateFormat df = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		return df.format(date);
	}
	
}
