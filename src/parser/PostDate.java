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
	
	Integer minute;
	
	Integer AmPm;
	
	String timeZone;
	
	Date date;
	
	DateFormat format;
	
	public PostDate(String dateStr){
	
		//  3/18/2016 2:39 PM
		dateStr = dateStr.replace("/", "-");
		dateStr = dateStr + "-0700";
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm a, Z", Locale.ENGLISH);
		
		try {
			this.date = df.parse(dateStr);
			extractDateElements(this.date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
	}
	
	
}
