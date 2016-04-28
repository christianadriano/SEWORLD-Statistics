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
public class SentMail {

	String subject;
	
	Date date;
	
	String dateStr;
	
	// Time zone of messages sent is PST
	String timeZome;
	
	String senderEmail;
	
	String receiverEmail;
	
	public void setDate(String dateStr){
		
		this.dateStr = dateStr;
		
		//System.out.println(dateStr);
		// 3/3/2014  8:06:00 AM
		DateFormat df = new SimpleDateFormat("m/dd/yyyy HH:mm Z", Locale.ENGLISH);
		this.timeZome = "-0800";
		
		dateStr = dateStr.replace(',', ' ');
		dateStr = dateStr.replace('*', ' ') + " "+ this.timeZome;
		
		
		try {
			this.date = df.parse(dateStr);
			this.dateStr = df.format(date);
			
		} catch (ParseException e) {
			System.out.println("Subject: "+ subject+ ",Date: "+dateStr);
			e.printStackTrace();
		}
	}
		
	public void setSenderEmail(String sender){
		this.senderEmail =  extractEmail(sender);
	}
	
	public void setReceiverEmail(String sender){
		this.receiverEmail =  extractEmail(sender);
	}

	private String extractEmail(String line){
		
		int middle = line.indexOf("@");
		int end = line.indexOf(">", middle); //find next blank space after the @
		int start = line.indexOf("<");
		
		if(end<0 || start<0){//Means that email has no < > delimiters, so just remove the commas and trim the content.
			line = line.replaceAll(","," ");
			line = line.trim();
			return line;
		}
		else{
			return line.substring(start+1,end).trim();
		}
	}

	public void setSubject(String line) {
		line = line.replaceAll("\"",  " ");
		line = line.replaceAll(",",  " ");
		this.subject = line.replaceAll("Re:"," ");		
	}
	
	
	public static String header = "Subject,Sender Email,Receiver Email, Date";
	
	public String toString(){
		String token = ",";
		
		return subject +token+senderEmail+token+receiverEmail+token+dateStr;
	}
}
