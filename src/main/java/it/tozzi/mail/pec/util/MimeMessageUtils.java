package it.tozzi.mail.pec.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
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

	private static final String FORMATO_DATA = "dd-MM-yyyy HH:mm";
	
	private static final Logger logger = LoggerFactory.getLogger(MimeMessageUtils.class);
	private static final String BODYSTRUCTURE_LOADING_ERROR = "Unable to load BODYSTRUCTURE";

	public static MimeMessage createMimeMessage(InputStream inputStream, Properties properties)
			throws PECParserException {

		try {
			return new MimeMessage(Session.getDefaultInstance(properties != null ? properties : System.getProperties()),
					inputStream);

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

	public static String getXAttachmentID(MimePart part) {
		List<String> values;

		try {
			values = MimeMessageUtils.getHeaderValues(PECConstants.X_ATTACHMENT_ID, part);
		} catch (PECParserException e) {
			return null;
		}

		return values.isEmpty() ? null : values.get(0);
	}

	public static String getHeaderValue(String headerKey, Part part) throws PECParserException {

		List<String> values;

		try {
			values = MimeMessageUtils.getHeaderValues(headerKey, part);
		} catch (PECParserException e) {
			return null;
		}

		return values.isEmpty() ? null : values.get(0);

	}

	public static List<String> getHeaderValues(String headerKey, Part part) throws PECParserException {

		try {
			String[] res = part.getHeader(headerKey);
			if (res != null) {
				return Stream.of(res).collect(Collectors.toList());
			}

			return new ArrayList<>();

		} catch (MessagingException e) {
			logger.error("Errore durante l'estrazione dell'header: {} per di {}", headerKey, getDescription(part), e);
			throw new PECParserException("Errore durante l'estrazione del'header " + headerKey + " data handler per: "
					+ getDescription(part), e);
		}

	}

	public static DataHandler getDataHandler(Part part) throws PECParserException {

		try {
			return part.getDataHandler();

		} catch (MessagingException e) {
			logger.error("Errore durante l'estrazione del data handler per: {}", getDescription(part), e);
			throw new PECParserException("Errore durante l'estrazione del data handler per: " + getDescription(part),
					e);
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
					throw new PECParserException("Errore durante la lettura del contenuto di: " + getDescription(part),
							e);
				}

			} else if (isMimeType(part, MimeTypesUtil.CONTENT_TYPE_TEXT)) {

				try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
					IOUtils.fastCopy(part.getInputStream(), os);
					content = new String(os.toByteArray(), StandardCharsets.US_ASCII);

				} catch (IOException | MessagingException e1) {
					throw new PECParserException("Errore durante la lettura del contenuto di: " + getDescription(part),
							e);
				}

			} else {
				logger.error("Errore durante la lettura del contenuto di: {}", getDescription(part), e);
				throw new PECParserException("Errore durante la lettura del contenuto di: " + getDescription(part), e);
			}
		}

		return content;
	}

	public static String getUniqueMessageID(MimeMessage mimeMessage) throws PECParserException {
		
		DateFormat df = new SimpleDateFormat(FORMATO_DATA);
		StringBuilder res = new StringBuilder();
		
		//
		try {
			if (mimeMessage.getSender() != null && mimeMessage.getSender() instanceof InternetAddress) {
				InternetAddress ia = (InternetAddress) mimeMessage.getSender();
				res.append(ia.getAddress()).append("_");
			}
			
		} catch (MessagingException e) {
		}
		
		//
		List<String> destinatari = new ArrayList<>();
		try {
			if (mimeMessage.getAllRecipients() != null) {
				for (Address address : mimeMessage.getAllRecipients()) {

					if (address instanceof InternetAddress) {
						InternetAddress ia = (InternetAddress) address;
						destinatari.add(ia.getAddress());
					}
				}
			}

		} catch (MessagingException e) {
		}
		if (!destinatari.isEmpty()) {
			destinatari = destinatari.stream().sorted(Comparator.comparing(String::toString))
					.collect(Collectors.toList());
			destinatari.forEach(d -> {
				res.append(d).append("_");
			});
		}
		
		//
		try {
			if (mimeMessage.getSentDate() != null) {
				res.append(df.format(mimeMessage.getSentDate())).append("_");
			}
			
		} catch (MessagingException e) {
		}
		
		//
		try {
			if (mimeMessage.getReceivedDate() != null) {
				res.append(df.format(mimeMessage.getReceivedDate())).append("_");
			}
			
		} catch (MessagingException e) {
		}
		
		if (res.isEmpty()) {
			throw new PECParserException("Errore in fase di elaborazione del messageID", null);
		}
		
		try {
			return new String(MessageDigest.getInstance("SHA-256").digest(res.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
			
		} catch (NoSuchAlgorithmException e) {
			throw new PECParserException("Errore in fase di elaborazione del messageID", e);
		}
	}
}
