package it.tozzi.mail.pec;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;

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
	
//	@Test
//	public void test1() throws PECParserException, IOException {
//				
//		try (InputStream emlInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("message.eml")) {
//			MimeMessage mimeMessage = MimeMessageUtils.createMimeMessage(emlInputStream, null);
//			Messaggio messaggio = PECMessageParser.getInstance().parse(mimeMessage);
//			System.out.println(messaggio);
//			// Test...
//		}
//		
//	}
//	
//	@Test
//	public void test2() throws PECParserException, IOException {
//				
//		try (InputStream emlInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("message2.eml")) {
//			MimeMessage mimeMessage = MimeMessageUtils.createMimeMessage(emlInputStream, null);
//			Messaggio messaggio = PECMessageParser.getInstance().parse(mimeMessage);
//			System.out.println(messaggio);
//			// Test...
//		}
//		
//	}
//	
//	@Test
//	public void test3() throws PECParserException, IOException {
//				
//		try (InputStream emlInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("message3.eml")) {
//			MimeMessage mimeMessage = MimeMessageUtils.createMimeMessage(emlInputStream, null);
//			Messaggio messaggio = PECMessageParser.getInstance().parse(mimeMessage);
//			System.out.println(messaggio);
//			// Test...
//		}
//		
//	}

}
