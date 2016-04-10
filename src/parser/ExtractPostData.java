package parser;

import java.io.File;
import java.util.ArrayList;

import util.ReadFileBuffer;


/**
 * Extract the date of the post
 * 
 * @author adrianoc
 *
 */
public class ExtractPostData {
	
	
	public ArrayList<Post> readFileToPosts(String path, String fileName){
		
		ArrayList<Post> postList = new ArrayList<Post>();
		ArrayList<String> list = ReadFileBuffer.readToBuffer(path,fileName);
		
		for(String line: list){
			String date = this.extractDateFromPost(line);
			//String subject = this.extractSubjectFromPost(line);
			String sender = this.extractSenderFromPost(line);
			postList.add(new Post(date,"subject",sender,"content"));
		}
		
		return postList;
	}
	
	
	public String extractDateFromPost(String line){
		
		String[] lineParts = line.split(",");
		return lineParts[3];
	}
	
	
	public String extractSubjectFromPost(ArrayList<String> list){
		
		String firstMark = "ceived:";
		
		int start =  findLastLineOfFirstMark(list,firstMark);
		System.out.println("start: "+start);
		int end = findEndSeachList(list,start);
		ArrayList<String> reversedList = reverseList(list,start,end);
		System.out.println("reversedList.size: "+reversedList.size());
				
		String token = "ubject:";
		
		int position=0; 
		for(int i=0;i<reversedList.size();i++){
			String line =  reversedList.get(i); 
			//System.out.println(line);
			if(line.indexOf(token)>0){
				position=i;
				break;
			}
		}
		System.out.println("position:"+ position);
		String subject = reversedList.get(position);

		String secondLine = checkNextLine(reversedList.get(position-1));
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
	
	public String extractSenderFromPost(String line){
		
		String senderPrefixToken = "seworld-moderator@SIGSOFT.ORG"; //This email address appears before the sender address
		
		String[] lineParts = line.split(",");
		String content = lineParts[4];
		int start = line.indexOf(senderPrefixToken) + senderPrefixToken.length();

		return extractEmail(content,start);
	}

	
	public String extractEmail(String line, int start){
		
		int middle = line.indexOf("@",start);
		int end = line.indexOf(" ", middle); //find next blank space after the @
		
		int i=0;
		while(line.charAt(middle-i)!=' '){//backtracks until it finds the beginning of the email
			i++;
		}
			
		return line.substring(middle-i,end).trim();
	}
	
	public String extractEmailBodyFromPost(String line){
		
		String[] lineParts = line.split(",");
		return lineParts[5];
	}


	
	//----------------------------
	
	ArrayList<String> fileNameList;
	
	private void listFilesInFolder(final File folder){
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesInFolder(fileEntry);
	        } else {
	            this.fileNameList.add(fileEntry.getName());
	        }
	    }
	}

	
	
}
