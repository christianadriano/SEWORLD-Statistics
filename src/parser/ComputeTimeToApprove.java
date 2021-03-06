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

	HashMap<String,HashMap<String,SentMail>> mailMap = new 	HashMap<String,HashMap<String,SentMail>>();

	String currentPostFileName;
	
	public static void main(String args[]){
		
		ComputeTimeToApprove computer = new ComputeTimeToApprove();
		//computer.runAll();
		computer.runByTags(Tags.accept_other);
	}
	
	public void runAll(){

		HashMap<String,HashMap<String,Post>> consolidatedMap = extractAllPost();
		
		for(Map.Entry<String,HashMap<String,Post>> entry: consolidatedMap.entrySet()){
			HashMap<String,Post> map = updatePostSentDates(entry.getValue());
			entry.setValue(map);
			consolidatedMap.put(entry.getKey(), entry.getValue());		
		}
		
		ExtractPostData extractor =  new ExtractPostData();
		extractor.printToCSV(consolidatedMap);
		
	}
	
	
	public void runByTags(String tag){

		ExtractPostData extractor =  new ExtractPostData();
		extractor.setupModeratedPosts("2016");
		extractor.run();
		HashMap<String,Post> postMap2016 = extractor.tagPostMap.get(tag);
		
		extractor =  new ExtractPostData();
		extractor.setupModeratedPosts("2015");
		extractor.run();
		HashMap<String,Post> postMap2015 = extractor.tagPostMap.get(tag);
		
		ComputeTimeToApprove computeTimeToApprove =  new ComputeTimeToApprove();
		computeTimeToApprove.run();
		
		System.out.println("Size of map 2015:"+ postMap2015.size()+", map 2016:"+ postMap2016.size());

		HashMap<String,Post> postMap = computeTimeToApprove.appendMaps(postMap2015,postMap2016);
		postMap = computeTimeToApprove.updatePostSentDates(postMap);
		HashMap<String,HashMap<String,Post>> tagPostMap = new HashMap<String,HashMap<String,Post>>();
		tagPostMap.put(tag, postMap);
		extractor.printToCSV(tagPostMap);
	}
	

	public HashMap<String,HashMap<String,Post>> extractAllPost(){
		ExtractPostData extractor =  new ExtractPostData();
		extractor.setupModeratedPosts("2016");
		extractor.run();
		HashMap<String,HashMap<String,Post>> postMap2016 = extractor.tagPostMap;
		
		extractor.setupModeratedPosts("2015");
		extractor.run();
	
		return extractor.appendTagPostMap(postMap2016);	
	}

	public HashMap<String,Post> appendMaps(HashMap<String,Post> postMap1,HashMap<String,Post> postMap2){
		postMap1.putAll(postMap2);
		return postMap1;
	}

	//-----------------------------------------------------------------------------------------------------
	
	public HashMap<String,Post> updatePostSentDates(HashMap<String,Post> postMap){
		for(Map.Entry<String, Post> entry: postMap.entrySet()){
			Post post = entry.getValue();
			SentMail mail = this.findSentEmail(post);
			if(mail!=null && post!=null){
				post.setSentDate(mail.date);
				postMap.put(entry.getKey(),post);
			}
		}
		return postMap;
	}

	

	public void run(){
		
		PropertyManager manager = new PropertyManager();
		ArrayList<String> indexContentList = ReadFileBuffer.readToBuffer(manager.SEWORLD_FOLDER_NAME+"//sentMessages//index_sentMessages.csv");		

		ArrayList<SentMail> messageList = loadMessages(indexContentList); 
		//printMessages(messageList);
		populateMap(messageList);
	}


	public ArrayList<SentMail>  loadMessages(ArrayList<String> indexContentList){

		ArrayList<SentMail> sentList = new ArrayList<SentMail>(); 

		//Skip first line (header)
		indexContentList.remove(0);

		for(String line : indexContentList){
			String[] tokens = line.split("#");
			SentMail message = new SentMail();
			message.setSubject(tokens[2]);
			message.setSenderEmail(tokens[3]);
			//message.setReceiverEmail(tokens[2]);
			message.setSentDate(tokens[5]); 
			message.sentDelay = tokens[6];
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

		if(this.mailMap.containsKey(post.subscriberEmail.trim())){
			HashMap<String, SentMail> map = this.mailMap.get(post.subscriberEmail);
			double similarity =0;
			String mostSimilarSubject="";
			Date mostSimilarSubjectDate=null;
			String mostSimilar_sentMailDelay=null;
			SentMail mostSimilarMail=null;
			for(Map.Entry<String,SentMail> entry: map.entrySet()){
				String subject = entry.getKey();
				mostSimilarSubject=subject;
				double index = cosineSimilarity.calculate(subject.trim(),post.subject.replace(",", " "));
				if(index>similarity){
					similarity=index;
					mostSimilarSubject=subject;
					mostSimilarSubjectDate=entry.getValue().date;
					mostSimilarMail = entry.getValue();
					mostSimilar_sentMailDelay = entry.getValue().sentDelay.replace(",", " ");
				}
			}
			System.out.println("Found! Post: "+post.subject +" same as: " +mostSimilarSubject);
			post.setSentDate(mostSimilarSubjectDate);
			post.sentMailDelay = mostSimilar_sentMailDelay;
			return mostSimilarMail;
		}
		else{
			System.out.println("Did not find sentMail for: "+ post.subject+", subscriberEmail:"+post.subscriberEmail);
			return null;
		}
	}

}

