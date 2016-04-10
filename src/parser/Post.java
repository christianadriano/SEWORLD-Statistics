package parser;

import java.util.Date;


/** 
 * Represents a SEWORLD post
 * @author adrianoc
 *
 */
public class Post {

	/** Text in the subject field of the post */
	String subject;
		
	/** Date when the post emailed to SEWORLD */
	PostDate receiveDate;
	
	/** Tag used to moderate the post */
	String moderationTag;
	
	/** Date that post was rejected */
	PostDate rejectDate;
	
	/** name and email of person who sent the post */
	String senderEmail;
	
	/** Content of the email body */
	String content;
	
	public Post(String receiveDateStr, String subject, String senderEmail, String content){
		this.receiveDate = new PostDate(receiveDateStr);
		this.subject = subject;
		this.senderEmail = senderEmail;
		this.content = content;
	}
	
}
