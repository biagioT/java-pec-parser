package app.tozzi.util;

import app.tozzi.model.exception.MailParserException;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * MIME Utilities
 *
 * @author Biagio Tozzi
 */
@Slf4j
public class MimeMessageUtils {

    private static final String DATE_FORMAT = "YYYYMMDDHHMMSS";
    private static final ZoneId ZI = ZoneId.of("Europe/Rome");

    /**
     * Extracts message ID from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return message ID
     */
    public static String getMessageID(MimeMessage mimeMessage) {

        try {
            var messageID = mimeMessage.getMessageID();

            if (messageID != null && !messageID.isBlank()) {
                return messageID.replaceAll("<", "").replaceAll(">", "");
            }

            return getUniqueMessageID(mimeMessage);

        } catch (MessagingException | NoSuchAlgorithmException e) {
            throw new MailParserException("Error reading messageID", e);
        }

    }

    /**
     * Extracts subject from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return subject
     */
    public static String getSubject(MimeMessage mimeMessage) {

        try {
            return mimeMessage.getSubject();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading subject", e);
        }

    }

    /**
     * Extracts received date from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return received date
     */
    public static Date getReceivedDate(MimeMessage mimeMessage) {

        try {
            return mimeMessage.getReceivedDate();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading received date", e);
        }

    }

    /**
     * Extracts sent date from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return sent date
     */
    public static Date getSentDate(MimeMessage mimeMessage) {

        try {
            return mimeMessage.getSentDate();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading sent date", e);
        }

    }

    /**
     * Extracts BCC recipients from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return BCC Recipients
     */
    public static List<Address> getBCC(MimeMessage mimeMessage) {

        try {
            return Stream.ofNullable(mimeMessage.getRecipients(Message.RecipientType.BCC)).flatMap(Stream::of).toList();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading BCC recipients", e);
        }

    }

    /**
     * Extracts CC recipients from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return CC Recipients
     */
    public static List<Address> getCC(MimeMessage mimeMessage) {

        try {
            return Stream.ofNullable(mimeMessage.getRecipients(Message.RecipientType.CC)).flatMap(Stream::of).toList();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading CC recipients", e);
        }

    }

    /**
     * Extracts TO recipients from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return TO Recipients
     */
    public static List<Address> getTo(MimeMessage mimeMessage) {

        try {
            return Stream.ofNullable(mimeMessage.getRecipients(Message.RecipientType.TO)).flatMap(Stream::of).toList();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading TO recipients", e);
        }

    }

    /**
     * Extracts from address from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return from address
     */
    public static List<Address> getFrom(MimeMessage mimeMessage) {

        try {
            return Stream.ofNullable(mimeMessage.getFrom()).flatMap(Stream::of).toList();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading sender", e);
        }

    }

    /**
     * Extracts a {@link MimeMessage} single header value from key headerKey
     *
     * @param mimeMessage {@link MimeMessage}
     * @param headerKey Header key
     * @return Header value
     */
    public static String getHeader(MimeMessage mimeMessage, String headerKey) {

        try {
            return mimeMessage.getHeader(headerKey, ",");

        } catch (MessagingException e) {
            throw new MailParserException("Error reading header " + headerKey, e);
        }
    }

    /**
     * Extracts {@link MimeMessage} header values from key headerKey
     *
     * @param mimeMessage {@link MimeMessage}
     * @param headerKey Header key
     * @return Header values
     */
    public static String[] getHeaders(MimeMessage mimeMessage, String headerKey) {

        try {
            return mimeMessage.getHeader(headerKey);

        } catch (MessagingException e) {
            throw new MailParserException("Error reading header " + headerKey, e);
        }
    }

    /**
     * Extracts a single {@link Part} header value of key headerKey
     *
     * @param part {@link Part}
     * @param headerKey Header key
     * @return Header value
     */
    public static String getHeaderValue(String headerKey, Part part) {
        var values = MimeMessageUtils.getHeaderValues(headerKey, part);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    /**
     * Extracts {@link Part} header values of key headerKey
     *
     * @param part {@link Part}
     * @param headerKey Header key
     * @return Header values
     */
    public static List<String> getHeaderValues(String headerKey, Part part) {

        try {
            String[] res = part.getHeader(headerKey);

            if (res != null) {
                return Stream.of(res).toList();
            }

            return null;

        } catch (MessagingException e) {
            throw new MailParserException("Error reading header " + headerKey + " of part: " + getDescription(part), e);
        }

    }

    /**
     * Retrieve content type of {@link Part}
     *
     * @param part {@link Part}
     * @return Content Type
     */
    public static String getContentType(Part part) {

        try {
            return part.getContentType();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading content type. Part: " + getDescription(part), e);
        }

    }

    /**
     * Retrieve disposition of {@link Part}
     *
     * @param part {@link Part}
     * @return disposition
     */
    public static String getDisposition(Part part) {

        try {
            return part.getDisposition();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading disposition. Part: " + getDescription(part), e);
        }
    }

    /**
     * Retrieve count of {@link Multipart}
     *
     * @param multiPart {@link Multipart}
     * @return count
     */
    public static int getCount(Multipart multiPart) {

        try {
            return multiPart.getCount();

        } catch (MessagingException e) {
            throw new MailParserException("Error during counting parts", e);
        }
    }

    /**
     * Retrieve file name of {@link Part}
     *
     * @param part {@link Part}
     * @return file name
     */
    public static String getFileName(Part part) {
        try {
            return part.getFileName();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading file name. Part: " + getDescription(part), e);
        }
    }

    /**
     * Extracts index-th {@link BodyPart} of {@link Multipart} part
     *
     * @param multiPart {@link Multipart}
     * @param index part index
     * @return {@link BodyPart}
     */
    public static BodyPart getBodyPart(Multipart multiPart, int index) {

        try {
            return multiPart.getBodyPart(index);

        } catch (MessagingException e) {
            throw new MailParserException("Error reading body part n. " + index, e);
        }

    }

    private static String getDescription(Part part) {

        try {
            return part.getDescription();

        } catch (MessagingException e) {
            log.error("Error reading part description", e);
            return null;
        }
    }

    /**
     * Extracts content of {@link Part}
     *
     * @param part {@link Part}
     * @return content
     */
    public static Object getContent(Part part) {

        try {
            return part.getContent();

        } catch (IOException | MessagingException e) {
            throw new MailParserException("Error reading content", e);
        }

    }

    /**
     * Check that the {@link Part} is of the mimeType
     *
     * @param part {@link Part}
     * @param mimeType Mime type
     * @return true/false
     */
    public static boolean isMimeType(Part part, String mimeType) {

        try {
            return part.isMimeType(mimeType);

        } catch (MessagingException e) {
            throw new MailParserException("Error reading mime type of part " + getDescription(part), e);
        }
    }

    /**
     * Extracts {@link DataHandler} from {@link Part}
     *
     * @param part {@link Part}
     * @return {@link DataHandler}
     */
    public static DataHandler getDataHandler(Part part) {

        try {
            return part.getDataHandler();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading data handler of part: " + getDescription(part), e);
        }
    }

    /**
     * Decode text
     *
     * @param text
     * @return decoded text
     */
    public static String decodeText(String text) {

        try {
            return MimeUtility.decodeText(text);

        } catch (UnsupportedEncodingException e) {
            throw new MailParserException("Error decoding text " + text, e);
        }
    }

    /**
     * Decode stream {@link InputStream} type encoded
     *
     * @param inputStream {@link InputStream}
     * @param type encoding type
     * @return Decoded stream
     */
    public static InputStream decodeStream(InputStream inputStream, String type) {

        try {
            return MimeUtility.decode(inputStream, type);

        } catch (MessagingException e) {
            throw new MailParserException("Error decoding stream", e);
        }
    }

    /**
     * Creates {@link MimeMessage} from {@link InputStream}
     *
     * @param inputStream {@link InputStream}
     * @param properties {@link Properties}
     * @return MIME Message
     */
    public static MimeMessage createMimeMessage(InputStream inputStream, Properties properties) {

        try {
            return new MimeMessage(Session.getDefaultInstance(properties != null ? properties : System.getProperties()), inputStream);

        } catch (MessagingException e) {
            throw new MailParserException("Error creating mime message", e);
        }
    }

    /**
     * Extracts all headers from {@link MimeMessage}
     *
     * @param mimeMessage {@link MimeMessage}
     * @return all headers (key, values)
     */
    public static Enumeration<Header> getAllHeaders(MimeMessage mimeMessage) {
        try {
            return mimeMessage.getAllHeaders();

        } catch (MessagingException e) {
            throw new MailParserException("Error reading all headers", e);
        }
    }
    
    private static String getUniqueMessageID(MimeMessage mimeMessage) throws NoSuchAlgorithmException, MessagingException {

        var res = new StringBuilder();
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);

        boolean sd = false;
        boolean rd = false;

        if (mimeMessage.getSentDate() != null) {
            res.append(df.format(mimeMessage.getSentDate())).append("_");
            sd = true;
        }

        if (mimeMessage.getReceivedDate() != null) {
            res.append(df.format(mimeMessage.getReceivedDate())).append("_");
            rd = true;
        }

        if (res.isEmpty() || (!rd && !sd)) {
            return Timestamp.from(ZonedDateTime.now(ZI).toInstant()).hashCode() + UUID.randomUUID().toString();
        }


        if (mimeMessage.getSender() != null && mimeMessage.getSender() instanceof InternetAddress ia) {
            res.append(ia.getAddress()).append("_");
        }

        if (mimeMessage.getAllRecipients() != null) {
            List<String> rec = Stream.of(mimeMessage.getAllRecipients()).filter(r -> r instanceof InternetAddress).map(r -> ((InternetAddress) r).getAddress()).toList();
            if (!rec.isEmpty()) {
                res.append(String.join("_", rec));
            }
        }

        return new String(MessageDigest.getInstance("SHA-256").digest(res.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

    }

}
