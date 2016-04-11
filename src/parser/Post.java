package parser;

import java.util.Date;


/** 
 * Represents a SEWORLD post
 * @author adrianoc
 *
 */
public class Post {

	/** Folder from which the post was extracted. It is usually the name of the tag.*/
	String tag;
	
	/** The id of the post is the file name of the .eml file.*/
	String ID;

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
	

	public Post(String folderPath, String fileName, String tagName) {
		this.ID = fileName;
		this.tag = tagName;
	}
	
}
