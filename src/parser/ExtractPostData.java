package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import util.PropertyManager;
import util.ReadFileBuffer;


/**
 * Extract the date of the post
 * 
 * @author adrianoc
 *
 */
public class ExtractPostData {

	HashMap<String,HashMap<String,Post>> tagPostMap = new 	HashMap<String,HashMap<String,Post>>();

	public static void main(String args[]){
		ExtractPostData extractor =  new ExtractPostData();
		extractor.run("2016");
	}

	String currentPostFileName;
	String year;
	
	public void run(String year){
		this.year = year;
		setup();

		ArrayList<Post> stageList = new ArrayList<Post>(); 

		Iterator<String> iter =  this.tagPostMap.keySet().iterator();
		while(iter.hasNext()){
			String tag = iter.next();
			HashMap<String, Post> mapPost = this.tagPostMap.get(tag);
			Iterator<String> iterID = mapPost.keySet().iterator();
			while(iterID.hasNext()){
				Post post = mapPost.get(iterID.next());
				this.currentPostFileName = post.filePath;
				post = this.readFileToPosts(post);
				stageList.add(post);
			}
		}

		//Update post map
		for(Post post:stageList){
			if(post!=null)
				addPost(post);
		}

		printToCSV();
	}

	private void setup(){
		PropertyManager manager = new PropertyManager();
		manager.initialize();
		Tags.initialize();
		for(String subfolder:Tags.tagMap.values()){
			listFilesInFolder(manager.SEWORLD_FOLDER_NAME+"//"+this.year+"//",subfolder);			
		}
	}		

	private void listFilesInFolder(String folderPath, String tag){

		final File folder = new File(folderPath + tag+"//");

		if(folder.exists()){

			for (File fileEntry : folder.listFiles()) {
				if (!fileEntry.isDirectory() && (fileEntry.getName().indexOf(".eml")>0)){
					//System.out.println("read name:"+ fileEntry.getName());
					Post post = new Post(folderPath, fileEntry.getName(),tag);
					addPost(post);
				}
			}
		}
		else{
			System.out.println("Attention, did not find folder: "+  folderPath + tag+"//");
		}
	}

	private void addPost(Post post){

		String tag = post.tag;
		HashMap<String,Post> postMap = this.tagPostMap.get(tag);
		if(postMap==null){
			postMap = new HashMap<String,Post>();
		}
		postMap.put(post.ID, post);
		this.tagPostMap.put(tag, postMap);
	}

	public  Post readFileToPosts(Post post){

		ArrayList<String> orginalList = ReadFileBuffer.readToBuffer(post.filePath);

		ArrayList<String> list = scopeList(orginalList);
		if(list.size()>0){
			post.setReceivedDate(this.extractDateFromPost(list));
			post.subject = this.extractSubjectFromPost(list);
			post.subscriberEmail = this.extractSenderFromPost(list);
			return post;
		}
		else{
			System.out.println("Null post: "+ post.filePath);
			return null;
		}
	}


	public ArrayList<String> scopeList(ArrayList<String> list){
		String firstMark = "ceived:";//For some reason we cannot match the first letter.

		int start =  findLastLineOfFirstMark(list,firstMark);
		//System.out.println("start: "+start);
		int end = findEndSeachList(list,start);
		ArrayList<String> reversedList = reverseList(list,start,end);
		//System.out.println("reversedList.size: "+reversedList.size());
		//System.out.println(reversedList.get(0));
		return reversedList;
	}

	public String extractDateFromPost(ArrayList<String> list){

		String token = "ate: ";//For some reason we cannot match the first letter.
		
		int position = getLineOfToken(list,token,6);
		//System.out.println("file: "+this.currentPostFileName);
		//System.out.println("position:"+ position);
		String dateLine = list.get(position);
		dateLine = dateLine.replaceAll("D"+token,"");
		//System.out.println(dateLine);
		return dateLine;
	}


	private int getLineOfToken(ArrayList<String> list, String token, int searchLength){

		//System.out.println("START getLineOfToken:===============");
		int position=0; 
		for(int i=0;i<list.size();i++){

			String line =  list.get(i);
			if(line.length()>searchLength)
				line = line.substring(0, searchLength); //Only look at the first characters
			//System.out.println(line);
			if(line.toUpperCase().indexOf(token.toUpperCase())>0){
				position=i;
				break;
			}
		}
		//System.out.println("END getLineOfToken:===============");
		return position;
	}


	public String extractSubjectFromPost(ArrayList<String> list){

		String token = "ubject:";

	
		int position=getLineOfToken(list,token,10);

		String subject = list.get(position);
		String secondLine=null;
		if(position>0)
			secondLine = checkNextLine(list.get(position-1));
			
		if(secondLine!=null){
			//	System.out.println(secondLine);
			StringBuffer buffer =  new StringBuffer();
			buffer.append(subject);
			buffer.append(secondLine); //.toString());//replace("/n","");
			subject = buffer.toString();
			//	System.out.println(subject);
		}
		return subject.replaceAll("S"+token," ");
	}


	private ArrayList<String> reverseList(ArrayList<String> list, int start,
			int end) {

		ArrayList<String> reversedList =  new ArrayList<String>();

		for(int i=end;i>start;i--){
			reversedList.add(list.get(i));
		}

		return reversedList;
	}


	private int findLastLineOfFirstMark(ArrayList<String> list, String firstMark) {

		int startPosition = 0;
		for(int i=0;i<list.size();i++){
			String line = list.get(i);
			if(line.length()>(firstMark.length()+2))
				line = line.substring(0,firstMark.length()+2);//Considers only the beginning of the line.
			//System.out.println(line);
			if(line.toUpperCase().indexOf(firstMark.toUpperCase())>0){
				startPosition = i;
				//System.out.println(line);
			}
		} 
		//	System.out.println(startPosition);
		return startPosition;
	}


	private String checkNextLine(String line){

		if(line.indexOf(":")>0)
			return null;
		else
			return line;
	}

	private int findEndSeachList(ArrayList<String> list,int start){

		String token_1 = "ontent-Type:";
		String token_2 = "ontent-Transfer-Encoding";
		String token_3 = "ser-agent:";
		String token_4 = "-Mailer:";

		int position_token_1 = lastPosition(list,start,token_1);
		int position_token_2 = lastPosition(list,start,token_2);
		int position_token_3 = lastPosition(list,start,token_3);
		int position_token_4 = lastPosition(list,start,token_4);

		int largest = position_token_1 > position_token_2 ? position_token_1 : position_token_2;
		largest = largest > position_token_3 ? largest : position_token_3;
		largest = largest > position_token_4 ? largest : position_token_4;

		return largest;  
	}

	private int lastPosition(ArrayList<String> list,int start,String token){
		int position=0; 
		for(int i=start;i<list.size();i++){
			String line =  list.get(i); 
			//System.out.println(line);
			if(line.toUpperCase().indexOf(token.toUpperCase())>0){
				position=i;
				break;
			}
		}
		//System.out.println("position End:"+ position);
		return position;
	}

	public String extractSenderFromPost(ArrayList<String> list){

		String token = "rom:"; //This email address appears before the sender address

		int position = getLineOfToken(list,token,6);
		String emailLine = list.get(position);

		return extractEmail(emailLine);
	}


	public String extractEmail(String line){

		int middle = line.indexOf("@");
		int end = line.indexOf(">", middle); //find next blank space after the @
		int start = line.indexOf("<");
		
		if(end<0 || start<0){//Means that email has no < > delimiters, so just remove the From: and trim the content.
			line = line.replaceAll("From:"," ");
			line = line.replace(","," ");
			line = line.trim();
			return line;
		}
		else{
			return line.substring(start+1,end).trim();
		}
	}

	public String extractEmailBodyFromPost(String line){

		String[] lineParts = line.split(",");
		return lineParts[5];
	}


	public ArrayList<String> getLinesToPrint(){

		ArrayList<String> linesToPrint = new ArrayList<String>();

		Iterator<String> iter =  this.tagPostMap.keySet().iterator();
		while(iter.hasNext()){
			String tag = iter.next();
			HashMap<String, Post> mapPost = this.tagPostMap.get(tag);
			Iterator<String> iterID = mapPost.keySet().iterator();
			while(iterID.hasNext()){
				Post post = mapPost.get(iterID.next());
				linesToPrint.add(post.toString());
			}
		}
		return linesToPrint;
	}


	public void printToCSV(){

		String destination = "C://seworld//statistics//stats.csv";
		BufferedWriter log;
		ArrayList<String> linesToPrint = this.getLinesToPrint();

		try {
			log = new BufferedWriter(new FileWriter(destination));
			//Print file header

			log.write(Post.header+"\n");

			for(String line: linesToPrint){
				log.write(line+"\n");
			}

			log.close();
			System.out.println("file written at: "+destination);
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + destination);
			e.printStackTrace();
		}


	}

	//----------------------------





}
