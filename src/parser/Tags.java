package parser;

import java.util.HashMap;

/**
 * Keeps the tags used during the moderation process.
 * Each tag name is also used a folder name to extract data from the posts downloaded to local folder.
 * 
 * @author adrianoc
 *
 */
public class Tags {

	//Rejected
	public static String reject_event_scope = "reject_event_scope";
	public static String reject_call_invalid = "reject_call_invalid";
	public static String reject_job_phd = "reject_job_phd";
	public static String reject_job_scope = "reject_job_scope";
	public static String reject_duplicate = "reject_duplicate";
	public static String reject_other = "reject_other";

	//Accepted
	public static String accept_conference = "accept_conference";
	public static String accept_journal = "accept_journal";
	public static String accept_job = "accept_job";
	public static String accept_other = "accept_other";

	public static HashMap<String, String> tagMap= new HashMap<String,String>();
	
	public static void initialize(){
		tagMap.put(reject_event_scope,reject_event_scope);
		tagMap.put(reject_call_invalid,reject_call_invalid);
		tagMap.put(reject_job_phd,reject_job_phd);
		tagMap.put(reject_job_scope,reject_job_scope);
		tagMap.put(reject_duplicate,reject_duplicate);
		tagMap.put(reject_other,reject_other);

		tagMap.put(accept_conference,accept_conference);
		tagMap.put(accept_journal,accept_journal);
		tagMap.put(accept_job,accept_job);
		tagMap.put(accept_other,accept_other);
	}
	
}
