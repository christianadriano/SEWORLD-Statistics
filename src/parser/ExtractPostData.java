package parser;

import java.io.File;
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
		extractor.run();
	}

	public void run(){
		setup();
		Iterator<String> iter =  this.tagPostMap.keySet().iterator();
		while(iter.hasNext()){
			String tag = iter.next();
			 HashMap<String, Post> mapPost = this.tagPostMap.get(tag);
			 Post post = mapPost.get(tag);
			 post = this.readFileToPosts(post);
			 mapPost.put(post.ID, post);
		}
	}
	
	private void setup(){
		PropertyManager manager = new PropertyManager();
		manager.initialize();
		Tags.initialize();
		for(String subfolder:Tags.tagMap.values()){
			listFilesInFolder(manager.SEWORLD_FOLDER_NAME,subfolder);			
		}
	}		



	private void listFilesInFolder(String folderPath, String tag){

		final File folder = new File(folderPath + tag+"//");
		
		for (File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory() && (fileEntry.getName().indexOf(".eml")>0)){
					Post post = new Post(folderPath, fileEntry.getName(),tag);
					addPost(post);
			}
		}
	}
	
	private void addPost(Post post){
		
		String tag = post.tag;
		HashMap<String,Post> postMap = this.tagPostMap.get(tag);
		if(postMap==null)
			postMap = new HashMap<String,Post>();
			postMap.put(post.ID, post);
		
	}

	public  Post readFileToPosts(Post post){

		
		ArrayList<String> orginalList = ReadFileBuffer.readToBuffer(filePath,fileName);

		ArrayList<String> list = scopeList(orginalList);
		String date = this.extractDateFromPost(list);
		String subject = this.extractSubjectFromPost(list);
		String sender = this.extractSenderFromPost(list);
		return new Post(date,subject,sender,tag);

	}


	public ArrayList<String> scopeList(ArrayList<String> list){
		String firstMark = "ceived:";

		int start =  findLastLineOfFirstMark(list,firstMark);
		System.out.println("start: "+start);
		int end = findEndSeachList(list,start);
		ArrayList<String> reversedList = reverseList(list,start,end);
		System.out.println("reversedList.size: "+reversedList.size());
		return reversedList;
	}

	public String extractDateFromPost(ArrayList<String> list){

		String token = "ate: ";
		int position = getLineOfToken(list,token);
		String dateLine = list.get(position);
		dateLine = dateLine.replaceAll("D"+token,"");
		//	System.out.println(dateLine);
		return dateLine;
	}


	private int getLineOfToken(ArrayList<String> list, String token){

		int position=0; 
		for(int i=0;i<list.size();i++){
			String line =  list.get(i); 
			//System.out.println(line);
			if(line.indexOf(token)>0){
				position=i;
				break;
			}
		}
		System.out.println("position:"+ position);
		return position;
	}


	public String extractSubjectFromPost(ArrayList<String> list){

		String token = "ubject:";

		int position=getLineOfToken(list,token);

		String subject = list.get(position);

		String secondLine = checkNextLine(list.get(position-1));
		if(secondLine!=null){
			System.out.println(secondLine);
			StringBuffer buffer =  new StringBuffer();
			buffer.append(subject);
			buffer.append(secondLine); //.toString());//replace("/n","");
			subject = buffer.toString();
			System.out.println(subject);
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
			//System.out.println(line);
			if(line.indexOf(firstMark)>0){
				startPosition = i;
				//System.out.println(line);
			}
		} 
		System.out.println(startPosition);
		return startPosition;
	}


	private String checkNextLine(String line){

		if(line.indexOf(":")>0)
			return null;
		else
			return line;
	}

	private int findEndSeachList(ArrayList<String> list,int start){
		String token = "ontent-Type:";

		int position=0; 
		for(int i=start;i<list.size();i++){
			String line =  list.get(i); 
			//System.out.println(line);
			if(line.indexOf(token)>0){
				position=i;
				break;
			}
		}
		System.out.println("position End:"+ position);
		return position;
	}

	public String extractSenderFromPost(ArrayList<String> list){

		String token = "rom:"; //This email address appears before the sender address

		int position = getLineOfToken(list,token);
		String emailLine = list.get(position);

		return extractEmail(emailLine);
	}


	public String extractEmail(String line){

		int middle = line.indexOf("@");
		int end = line.indexOf(">", middle); //find next blank space after the @

		int i=0;
		while(line.charAt(middle-i)!='<'){//backtracks until it finds the beginning of the email
			i++;
			System.out.print(line.charAt(i));
		}

		return line.substring(middle-i+1,end).trim();
	}

	public String extractEmailBodyFromPost(String line){

		String[] lineParts = line.split(",");
		return lineParts[5];
	}



	//----------------------------





}
