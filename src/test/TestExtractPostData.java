package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import parser.ExtractPostData;
import util.ReadFileBuffer;

public class TestExtractPostData {

	@Test
	public void testExtractSenderEmail() {

		String line = "This message  was originally  submitted by Johann.M.Schumann@NASA.GOV  to the";

		String expected= "Johann.M.Schumann@NASA.GOV";

		ExtractPostData data =  new ExtractPostData();
		String actual = data.extractEmail(line, 0);
		assertTrue("Sender emails don't match, expected: "+expected +", actual "+ actual, expected.matches(actual));
	}


	@Test
	public void testExtractReceiveDate() {

		String line = " \"SEWORLD: approval required (672FDD7E)\",\"LISTSERV@LISTSERV.ACM.ORG\",\"SEWORLD Moderator <seworld-moderator@SIGSOFT.ORG>\",3/18/2016 14:39, ,\"Subject: SEWORLD: approval required (672FDD7E) From: LISTSERV@LISTSERV.ACM.ORG Date: 3/18/2016 2:39 PM To: SEWORLD Moderator <seworld-moderator@SIGSOFT.ORG> This message  was originally submitted by  samia.bouzefrane@LECNAM.NET to the This message  was originally  submitted by Johann.M.Schumann@NASA.GOV  to the\"";

		String expected= "3/18/2016 14:39";

		ExtractPostData data =  new ExtractPostData();
		String actual = data.extractDateFromPost(line);
		assertTrue("Sender emails don't match, expected: "+expected +", actual "+ actual, expected.matches(actual));
	}

	@Test
	public void testExtractSubject(){

		String expected = "Deadline Approaching: IEEE International Conference on Cloud and Big Data Computing";
		
		ArrayList<String> list = ReadFileBuffer.readToBuffer("C://Users//adrianoc//Documents//GitHub//SEWORLD-Statistics//data//","SEWORLD  approval required (672FDD7E).eml");
		System.out.println(list.size());
		ExtractPostData data =  new ExtractPostData();
		String actual = data.extractSubjectFromPost(list);
		assertTrue("Sender emails don't match, expected: "+expected +", actual: "+ actual, expected.trim().matches(actual.trim()));

	}
}
