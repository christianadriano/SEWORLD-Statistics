package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * Utility class to read the contents of a file in to an ArrayList of Strings.
 * Also write back to file the content in an ArrayList of Strings.
 * 
 * @author adrianoc
 */
public class ReadFileBuffer {

	
		/** 
		 * Load all lines of file into and Array of Strings 
		 * @return ArrayList where each element has the content of a line in the file
		 */
		public static ArrayList<String> readToBuffer(String filePath){

			ArrayList<String> buffer = new ArrayList<String>();

			BufferedReader log;
			try {
				log = new BufferedReader(new FileReader(filePath));
				String line = null;
				while ((line = log.readLine()) != null) {
					buffer.add(line);
				}
				log.close();
				return buffer;
			} 
			catch (Exception e) {
				System.out.println("ERROR while processing file:" + filePath);
				e.printStackTrace();
				return null;
			}
		}
		
		
	
}
