package parser;


/** 
 * Represents a SEWORLD post
 * @author adrianoc
 *
 */
public class Post {

	/** Folder from which the post was extracted. It is usually the name of the tag used for moderation*/
	String tag;
	
	/** The id of the post is the file name of the .eml file.*/
	String ID;
	
	/** complete file path with file name */
	String filePath;

	/** Text in the subject field of the post */
	String subject;
		
	/** Date when the post emailed to SEWORLD */
	PostDate receivedDate;
	
	/** Date that post was rejected */
	PostDate sentDate;
	
	/** name and email of person who sent the post */
	String subscriberEmail;
		

	public Post(String folderPath, String fileName, String tagName) {
		this.ID = fileName.replace(",", " ");
		this.tag = tagName;
		this.filePath = folderPath+"//"+tagName+"//"+fileName;
	}


	/** For testing 
	 * @param postFileName */
	public Post(String postFileName) {
		this.filePath = postFileName;
	}


	public void setReceivedDate(String extractDateFromPost) {
		this.receivedDate = new PostDate(extractDateFromPost,this);
	}
	
	public void setSentDate(String extractDateFromPost) {
		this.sentDate = new PostDate(extractDateFromPost,this);
	}
	
	
	public static String header="tag,ID,subject,subscriber Email,received Date,sent Date,post Delay,received Week of Year, month of Year, Year";
	
	public String computeDelay(){
		if ((sentDate==null) || (receivedDate==null))
			return "-1";
		else
			return PostDate.computeDifferenceHours(sentDate.date,receivedDate.date).toString();
	}
	
	public String toString(){
		String TOKEN=",";
		return tag+TOKEN+ID+TOKEN+subject+TOKEN+subscriberEmail+TOKEN+receivedDate+TOKEN+sentDate+TOKEN+computeDelay()+
				TOKEN+receivedDate.weekOfYear+TOKEN+receivedDate.month+TOKEN+receivedDate.year;
	}
	
}
