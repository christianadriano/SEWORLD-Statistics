package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {

	private String propertiesFileName="extractor.properties";
	
	private String propertiesPath="C:/Users/adrianoc/Documents/GitHub/SEWORLD-Statistics/";
	
	public String SEWORLD_FOLDER_NAME =   "C:/seworld/statistics/"; //Default value
	
	public void initialize(){
		
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(propertiesPath+propertiesFileName));
			this.SEWORLD_FOLDER_NAME = properties.getProperty("SEWORLD_FOLDER_NAME");
		} 
		catch (IOException e) {
			System.out.println("Could not load properties. Please be sure that the property file is located at: "+propertiesPath);
			System.out.println("Using defaut value: "+SEWORLD_FOLDER_NAME);
		}
	}
	
	

}

