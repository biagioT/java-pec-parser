package it.tozzi.mail.pec;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import it.tozzi.mail.pec.exception.PECParserException;
import it.tozzi.mail.pec.model.Messaggio;
import it.tozzi.mail.pec.parser.PECMessageParser;
import it.tozzi.mail.pec.util.MimeMessageUtils;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class PECParserTest {
	
	// @Test
	public void test() throws PECParserException, IOException {
		
		try (InputStream emlInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("message.eml")) {
			MimeMessage mimeMessage = MimeMessageUtils.createMimeMessage(emlInputStream);
			Messaggio messaggio = PECMessageParser.getInstance().parse(mimeMessage);
			System.out.println(messaggio);
			// Test...
		}
		
	}

}
