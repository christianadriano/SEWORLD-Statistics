package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.berico.similarity.CosineSimilarity;

import util.PropertyManager;
import util.ReadFileBuffer;

public class ComputeTimeToApprove {

	//Index Approved by Subject

	//Get posts that do not have SEWORLD_MODERATOR AS SENDER 
	//

	//Index the Replies by Subject

	//Sender Email <Subject,Message>
	HashMap<String,HashMap<String,SentMail>> mailMap = new 	HashMap<String,HashMap<String,SentMail>>();

	public static void main(String args[]){

		ExtractPostData extractor =  new ExtractPostData();
		extractor.run("2016");
		ComputeTimeToApprove computeTimeToApprove =  new ComputeTimeToApprove();
		computeTimeToApprove.run("2016");
		HashMap<String,Post> postMap2016 = extractor.tagPostMap.get(Tags.accept_job);

		computeTimeToApprove.run("2015");
		HashMap<String,Post> postMap2015 = extractor.tagPostMap.get(Tags.accept_job);

		HashMap<String,Post> postMap = computeTimeToApprove.appendMaps(postMap2016,postMap2015);
		postMap = computeTimeToApprove.updatePostSentDates(postMap);

	}

	public HashMap<String,Post> appendMaps(HashMap<String,Post> postMap1,HashMap<String,Post> postMap2){
		postMap1.putAll(postMap2);
		return postMap1;
	}

	public HashMap<String,Post> updatePostSentDates(HashMap<String,Post> postMap){
		for(Map.Entry<String, Post> entry: postMap.entrySet()){
			Post post = entry.getValue();
			SentMail mail = this.findSentEmail(post);
			if(mail!=null){
				post.sentDate = mail.date;
				postMap.put(entry.getKey(),post);
			}
		}
		return postMap;
	}

	String currentPostFileName;
	String year;

	public void run(String year){
		this.year = year;
		PropertyManager manager = new PropertyManager();
		ArrayList<String> indexContentList = ReadFileBuffer.readToBuffer(manager.SEWORLD_FOLDER_NAME+"//sentMessages//index_sentMessages.csv");		

		ArrayList<SentMail> messageList = loadMessages(indexContentList); 
		printMessages(messageList);
		populateMap(messageList);
	}


	public ArrayList<SentMail>  loadMessages(ArrayList<String> indexContentList){

		ArrayList<SentMail> sentList = new ArrayList<SentMail>(); 

		//Skip first line (header)
		indexContentList.remove(0);

		for(String line : indexContentList){
			String[] tokens = line.split("#");
			if(tokens.length<4)
				System.out.println(tokens[1]);
			SentMail message = new SentMail();
			message.setSubject(tokens[0]);
			message.setSenderEmail(tokens[1]);
			message.setReceiverEmail(tokens[2]);
			message.setDate(tokens[3]); 
			sentList.add(message);
		}

		System.out.println("Total messages loaded:" + sentList.size());

		return sentList;
	}


	public void printMessages(ArrayList<SentMail> sentList){

		System.out.println(SentMail.header);
		for(int i=0; i<20;i++){ 	
			SentMail message = sentList.get(i);
			System.out.println(message.toString());		
		}
	}


	private void populateMap(ArrayList<SentMail> sentList){

		int i=0;
		for(SentMail message : sentList){
			if(!this.mailMap.containsKey(message.senderEmail)){
				this.mailMap.put(message.senderEmail,new HashMap<String,SentMail>());
			}

			HashMap<String, SentMail> map = this.mailMap.get(message.senderEmail);
			map.put(message.subject, message);
			this.mailMap.put(message.senderEmail,map);
			i++;
		}
		//System.out.println("populated messages: "+ i);
	}	

	private SentMail findSentEmail(Post post){

		CosineSimilarity cosineSimilarity = new CosineSimilarity();

		if(this.mailMap.containsKey(post.subscriberEmail)){
			HashMap<String, SentMail> map = this.mailMap.get(post.subscriberEmail);
			double similarity =0;
			String mostSimilarSubject="";
			Date mostSimilarSubjectDate=null;
			SentMail mostSimilarMail=null;
			for(Map.Entry<String,SentMail> entry: map.entrySet()){
				String subject = entry.getKey();
				double index = cosineSimilarity.calculate(subject.trim(),post.subject.replace(",", " ").trim());
				if(index>similarity){
					similarity=index;
					mostSimilarSubject=subject;
					mostSimilarSubjectDate=entry.getValue().date;
					mostSimilarMail = entry.getValue();

				}
			}
			System.out.println("Found! Post: "+post.subject +" same as: " +mostSimilarSubject);
			post.sentDate = mostSimilarSubjectDate;
			return mostSimilarMail;
		}
		else{
			System.out.println("Did not find sentMail for: "+ post.subject);
			return null;
		}
	}

}

