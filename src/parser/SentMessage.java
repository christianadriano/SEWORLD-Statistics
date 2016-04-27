package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Message sent either as a rejection reply or a post distributed to the subscribers.
 * 
 * @author adrianoc
 *
 */
public class SentMessage {

	String subject;
	
	Date date;
	
	String dateStr;
	
	String timeZome;
	
	String senderEmail;
	
	String receiverEmail;
	
	public void setDate(String dateStr){
		
		this.dateStr = dateStr;
		// 3/3/2014  8:06:00 AM
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aaa Z", Locale.ENGLISH);
		this.timeZome = "-0800";
		
		dateStr = trimBeginning(dateStr +" "+this.timeZome);
		
		try {
			this.date = df.parse(dateStr);
			
		} catch (ParseException e) {
			System.out.println("Date: "+dateStr);
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

}
