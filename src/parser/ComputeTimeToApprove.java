package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import util.PropertyManager;
import util.ReadFileBuffer;

public class ComputeTimeToApprove {

	//Index Approved by Subject
	
	//Get posts that do not have SEWORLD_MODERATOR AS SENDER 
	//
	
	//Index the Replies by Subject
	
	//Sender Email <Subject,Message>
	HashMap<String,HashMap<String,SentMessage>> tagPostMap = new 	HashMap<String,HashMap<String,SentMessage>>();

	public static void main(String args[]){
		ComputeTimeToApprove computeTimeToApprove =  new ComputeTimeToApprove();
		computeTimeToApprove.run("2016");
	}

	String currentPostFileName;
	String year;
	
	public void run(String year){
		this.year = year;
		PropertyManager manager = new PropertyManager();
		ArrayList<String> indexContentList = ReadFileBuffer.readToBuffer(manager.SEWORLD_FOLDER_NAME+"//sentMessages//index.csv");		
		
		ArrayList<SentMessage> stageList = new ArrayList<SentMessage>(); 

		for(String line : indexContentList){
			String[] tokens = line.split(",");
			SentMessage message = new SentMessage();
			message.subject =  tokens[0];
			message.senderEmail = tokens[3];
			message.receiverEmail = tokens[4];
			message.setDate(tokens[5]); 
		}

		//Add date to the Posts
		for(Post post:stageList){
			if(post!=null)
				addPost(post);
		}

		printToCSV();
	}

	
}
