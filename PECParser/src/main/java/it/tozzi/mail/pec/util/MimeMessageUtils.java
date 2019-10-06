package it.tozzi.mail.pec.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tozzi.mail.pec.exception.PECParserException;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class MimeMessageUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(MimeMessageUtils.class);
	private static final String BODYSTRUCTURE_LOADING_ERROR = "Unable to load BODYSTRUCTURE";
	
	public static MimeMessage createMimeMessage(InputStream inputStream) throws PECParserException {
		
		try {
			return new MimeMessage(Session.getDefaultInstance(System.getProperties()), inputStream);
			
		} catch (MessagingException e) {
			logger.error("Errore durante la creazione del MimeMessage", e);
			throw new PECParserException("Errore durante la creazione del MimeMessage", e);
		}
	}
	
	public static boolean isMimeType(Part part, String mimeType) throws PECParserException {

		try {
			return part.isMimeType(mimeType);

		} catch (MessagingException e) {
			logger.error("Errore durante la verifica del mime type {} per: {}", mimeType, getDescription(part));
			throw new PECParserException(
					"Errore durante la verifica del mime type " + mimeType + " per: " + getDescription(part), e);
		}
	}

	public static String getDescription(Part part) {

		try {
			return part.getDescription();

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione della descrizione", e);
			return "[NON_DISPONIBILE]";
		}
	}

	public static String getDisposition(Part part) throws PECParserException {

		try {
			return part.getDisposition();

		} catch (MessagingException e) {
			logger.error("Errore durante la lettura della disposition per: {}", getDescription(part), e);
			throw new PECParserException("Errore durante la lettura della disposition per: " + getDescription(part), e);
		}
	}

	public static String decodeText(String text) throws PECParserException {

		try {
			return MimeUtility.decodeText(text);

		} catch (UnsupportedEncodingException e) {
			logger.error("Errore durante la decodifica del testo: {}", text, e);
			throw new PECParserException("Errore durante la decodifica del testo: " + text, e);
		}
	}
	
	public static int getCount(Multipart multiPart) throws PECParserException {
		
		try {
			return multiPart.getCount();
			
		} catch (MessagingException e) {
			logger.error("Errore durante la lettura del parametro count", e);
			throw new PECParserException("Errore durante la lettura del parametro count", e);
		}
	}
	
	public static String getFileName(Part part) throws PECParserException {
		try {
			return part.getFileName();

		} catch (MessagingException e) {
			logger.error("Errore durante la lettura del nome del file di: {}", getDescription(part), e);
			throw new PECParserException("Errore durante la lettura del nome del file di: " + getDescription(part), e);
		}
	}
	
	public static BodyPart getBodyPart(Multipart multiPart, int index) throws PECParserException {
		
		try {
			return multiPart.getBodyPart(index);
			
		} catch (MessagingException e) {
			logger.error("Errore durante la lettura della parte numero {}", index, e);
			throw new PECParserException("Errore durante la lettura della parte numero: " + index, e);
		}
		
	}
	
	public static DataHandler getDataHandler(Part part) throws PECParserException {
		
		try {
			return part.getDataHandler();
			
		} catch (MessagingException e) {
			logger.error("Errore durante l'estrazione del data handler per: {}", getDescription(part), e);
			throw new PECParserException("Errore durante l'estrazione del data handler per: " + getDescription(part), e);
		}
		
	}
	
	public static String getContentType(Part part) throws PECParserException {
		
		try {
			return part.getContentType();
			
		} catch (MessagingException e) {
			logger.error("Errore durante l'estrazione del content type: {}", getDescription(part), e);
			throw new PECParserException("Errore durante l'estrazione del content type: " + getDescription(part), e);
		}
		
	}
	
	public static Object getContent(Part part) throws PECParserException {
		Object content;

		try {
			content = part.getContent();

		} catch (IOException | MessagingException e) {
			
			if (part instanceof MimeMessage && BODYSTRUCTURE_LOADING_ERROR.equalsIgnoreCase(e.getMessage())) {
				
				try {
					content = new MimeMessage((MimeMessage) part).getContent();
					
				} catch (IOException | MessagingException e1) {
					logger.error("Errore durante la lettura del contenuto di: {}", getDescription(part), e);
					throw new PECParserException("Errore durante la lettura del contenuto di: " + getDescription(part), e);
				}

			} else {
				logger.error("Errore durante la lettura del contenuto di: {}", getDescription(part), e);
				throw new PECParserException("Errore durante la lettura del contenuto di: " + getDescription(part), e);
			}
		}

		return content;
	}

}
