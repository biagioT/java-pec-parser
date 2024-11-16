package app.tozzi;

import app.tozzi.core.DeliveryStatusHandler;
import app.tozzi.core.PECHandler;
import app.tozzi.model.*;
import app.tozzi.model.exception.MailParserException;
import app.tozzi.util.*;
import jakarta.activation.DataSource;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimePart;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Main core class that offers email/PEC extraction
 *
 * @author Biagio Tozzi
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MailParser {

    private Properties properties;
    private boolean extractAllHeaders;

    /**
     * Default {@link MailParser} instance.
     * <ul>
     * <li>properties: System.getProperties(). System default properties</li>
     * <li>extractAllHeaders: false. Not all additional headers will be extracted</li>
     * </ul>
     *
     * @return {@link MailParser} instance
     */
    public static MailParser getInstance() {
        return new MailParser(null, false);
    }

    /**
     * {@link MailParser} instance with custom properties
     * <ul>
     * <li>extractAllHeaders: false. Not all additional headers will be extracted</li>
     * </ul>
     *
     * @return {@link MailParser} instance
     */
    public static MailParser getInstance(Properties properties) {
        return new MailParser(properties, false);
    }

    /**
     * {@link MailParser} instance with the extraction of all the headers
     *
     * @return {@link MailParser} instance
     */
    public static MailParser getInstance(boolean extractAllHeaders) {
        return new MailParser(null, extractAllHeaders);
    }

    /**
     * {@link MailParser} instance with the extraction of all the headers and custom properties
     *
     * @return {@link MailParser} instance
     */
    public static MailParser getInstance(Properties properties, boolean extractAllHeaders) {
        return new MailParser(properties, extractAllHeaders);
    }

    /**
     * Extracts a {@link ParsedEntity} from a mail MIME message.<br>
     * ParsedEntity can be:
     * <ul>
     * <li>{@link Mail}: simple mail</li>
     * <li>{@link PEC}: Posta Elettronica Certificata - Italian certified electronic mail </li>
     * <li>{@link PECReceipt}: Italian certified electronic mail receipt</li>
     * </ul>
     *
     * @param mimeMessage {@link MimeMessage} mail MIME message
     * @return {@link ParsedEntity}
     */
    public ParsedEntity parse(MimeMessage mimeMessage) {

        var xTranspHeader = MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_TRASPORTO);
        var xReceiptHeader = MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_RICEVUTA);
        var parsedEntity = extract(mimeMessage, xTranspHeader != null, xReceiptHeader != null, this.properties, this.extractAllHeaders);

        if (xReceiptHeader != null) {
            return PECHandler.loadReceipt((PEC) parsedEntity);
        }

        return parsedEntity;
    }

    /**
     * Extracts a {@link ParsedEntity} from a mail message.<br>
     * ParsedEntity can be:
     * <ul>
     * <li>{@link Mail}: simple mail</li>
     * <li>{@link PEC}: Posta Elettronica Certificata - Italian certified electronic mail </li>
     * <li>{@link PECReceipt}: Italian certified electronic mail receipt</li>
     * </ul>
     *
     * @param eml {@link File} mail eml MIME message
     * @return {@link ParsedEntity}
     */
    public ParsedEntity parse(File eml) {

        if (eml == null || !eml.exists()) {
            throw new MailParserException("Invalid file");
        }

        try (InputStream is = new FileInputStream(eml)) {
            return parse(is);

        } catch (IOException e) {
            throw new MailParserException("Error during parsing", e);
        }
    }

    /**
     * Extracts a {@link ParsedEntity} from a mail message.<br>
     * ParsedEntity can be:
     * <ul>
     * <li>{@link Mail}: simple mail</li>
     * <li>{@link PEC}: Posta Elettronica Certificata - Italian certified electronic mail </li>
     * <li>{@link PECReceipt}: Italian certified electronic mail receipt</li>
     * </ul>
     *
     * @param eml {@link InputStream} eml mail mime message
     * @return {@link ParsedEntity}
     */
    public ParsedEntity parse(InputStream eml) {
        return parse(MimeMessageUtils.createMimeMessage(eml, properties));
    }

    private static ParsedEntity extract(MimeMessage mimeMessage, boolean isPEC, boolean isPECReceipt, Properties properties, boolean extractAllHeaders) {

        var mail = new Mail();

        mail.setMessageID(MimeMessageUtils.getMessageID(mimeMessage));

        var from = MimeMessageUtils.getFrom(mimeMessage);
        mail.setFrom(from.stream().filter(r -> r instanceof InternetAddress).map(r -> {
            var ia = (InternetAddress) r;
            return app.tozzi.model.Address.builder().email(ia.getAddress()).name(ia.getPersonal()).build();
        }).toList());

        var to = MimeMessageUtils.getTo(mimeMessage);
        mail.setTo(to.stream().filter(r -> r instanceof InternetAddress).map(r -> {
            var ia = (InternetAddress) r;
            return app.tozzi.model.Address.builder().email(ia.getAddress()).name(ia.getPersonal()).build();
        }).toList());

        var cc = MimeMessageUtils.getCC(mimeMessage);
        mail.setCc(cc.stream().filter(r -> r instanceof InternetAddress).map(r -> {
            var ia = (InternetAddress) r;
            return app.tozzi.model.Address.builder().email(ia.getAddress()).name(ia.getPersonal()).build();
        }).toList());

        var bcc = MimeMessageUtils.getBCC(mimeMessage);
        mail.setBcc(bcc.stream().filter(r -> r instanceof InternetAddress).map(r -> {
            var ia = (InternetAddress) r;
            return app.tozzi.model.Address.builder().email(ia.getAddress()).name(ia.getPersonal()).build();

        }).toList());

        mail.setSentDate(MimeMessageUtils.getSentDate(mimeMessage));
        mail.setReceivedDate(MimeMessageUtils.getReceivedDate(mimeMessage));
        mail.setSubject(MimeMessageUtils.getSubject(mimeMessage));


        var inReplyTO = MimeMessageUtils.getHeader(mimeMessage, PECConstants.IN_REPLY_TO);
        if (inReplyTO != null) {
            mail.setReplyToMessageID(inReplyTO.replaceAll("<", "").replaceAll(">", ""));
        }

        var referencesHeader = MimeMessageUtils.getHeaders(mimeMessage, PECConstants.REFERENCES);
        if (referencesHeader != null) {
            mail.setReplyToHistoryMessagesID(Stream.of(Stream.of(referencesHeader).toList().get(0).split(" ")).map(r -> r.replaceAll("<", "").replaceAll(">", "")).toList());
        }

        if (extractAllHeaders) {
            mail.setHeaders(StreamSupport.stream(Spliterators.spliteratorUnknownSize(MimeMessageUtils.getAllHeaders(mimeMessage).asIterator(), 0), false)
                    .map(e -> Header.builder().key(e.getName()).value(e.getValue()).build()).toList());

        }

        return extractContent(mail, mimeMessage, isPEC, isPECReceipt, properties, extractAllHeaders);
    }

    private static ParsedEntity extractContent(Mail mail, MimeMessage mimeMessage, boolean isPEC, boolean isPECReceipt, Properties properties, boolean extractAllHeaders) {
        DataSourcePair<DataSource, DataSource> dsp = isPEC || isPECReceipt ? new DataSourcePair<>() : null;
        extractContent(mail, mimeMessage, isPEC, isPECReceipt, dsp);

        if (isPEC || isPECReceipt) {

            if (isPEC && dsp.getElementA() == null) {
                throw new MailParserException("Invalid PEC");
            }

            try {
                var postaCertMimeMessage = dsp.getElementA() != null ? MimeMessageUtils.createMimeMessage(dsp.getElementA().getInputStream(), properties) : null;
                return PECHandler.loadPEC(postaCertMimeMessage != null ? (Mail) extract(postaCertMimeMessage, false, false, properties, extractAllHeaders) : null, mail, dsp.getElementA(), dsp.getElementB(), mimeMessage);

            } catch (IOException e) {
                throw new MailParserException(e);
            }
        }

        return mail;
    }

    private static void extractContent(Mail mail, MimePart part, boolean isPEC, boolean isPECReceipt, DataSourcePair<DataSource, DataSource> pecAttachments) {
        var content = MimeMessageUtils.getContent(part);

        if (MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN)
                && !Part.ATTACHMENT.equalsIgnoreCase(MimeMessageUtils.getDisposition(part))
                && mail.getBodyTXT() == null) {

            var body = content.toString();

            if (UUEncodingUtils.containsEncodedAttachments(body)) {
                body = body.substring(0, UUEncodingUtils.getNextBeginIndex(body));
                mail.getAttachments().addAll(UUEncodingUtils.decodeAttachments(body));
            }

            mail.setBodyTXT(body);

        } else if (MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_TEXT_HTML)
                && !Part.ATTACHMENT.equalsIgnoreCase(MimeMessageUtils.getDisposition(part))) {

            mail.setBodyHTML(mail.getBodyHTML() != null ? mail.getBodyHTML() + content.toString() : content.toString());

        } else if (MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_MULTIPART)) {

            var multipart = (Multipart) content;
            for (int i = 0; i < MimeMessageUtils.getCount(multipart); i++) {
                extractContent(mail, (MimePart) MimeMessageUtils.getBodyPart(multipart, i), isPEC, isPECReceipt, pecAttachments);
            }

        } else if (MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_DELIVERY_STATUS)) {
            mail.setHasDeliveryStatus(true);
            mail.setDeliveryStatus(DeliveryStatusHandler.loadDeliveryStatus(part));

        } else {
            var pec = false;

            if ((isPEC || isPECReceipt) && !pecAttachments.isComplete()) {

                if (pecAttachments.getElementA() == null
                        && (PECConstants.POSTACERT_EML_NAME.equalsIgnoreCase(MimeMessageUtils.decodeText(MimeMessageUtils.getFileName(part))) || PECConstants.POSTACERT_EML_NAME.equals(MimeMessageUtils.getFileName(part)))
                        && MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_MESSAGE_RFC822)) {

                    pec = true;
                    try {
                        pecAttachments.setElementA(IOUtils.createDataSource(part));

                    } catch (IOException e) {
                        throw new MailParserException("Error extracting PEC postaCert.eml attachment", e);
                    }
                } else if (pecAttachments.getElementB() == null
                        && (PECConstants.DATICERT_XML_NAME.equalsIgnoreCase(MimeMessageUtils.decodeText(MimeMessageUtils.getFileName(part))) || PECConstants.DATICERT_XML_NAME.equals(MimeMessageUtils.getFileName(part)))
                        && MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_APPLICATION_XML)) {

                    pec = true;
                    try {
                        pecAttachments.setElementB(IOUtils.createDataSource(part));
                    } catch (IOException e) {
                        throw new MailParserException("Error extracting PEC datiCert.xml attachment", e);
                    }
                }
            }

            if (!pec) {
                DataSource dataSource;

                try {
                    dataSource = IOUtils.createDataSource(part);

                } catch (IOException e) {
                    throw new MailParserException("Error extracting attachment", e);
                }

                mail.getAttachments().add(Attachment.builder()
                        .name(dataSource.getName())
                        .dataSource(dataSource)
                        .contentID(MimeMessageUtils.getHeaderValue(MailConstants.CONTENT_ID, part))
                        .xAttachmentID(MimeMessageUtils.getHeaderValue(MailConstants.X_ATTACHMENT_ID, part))
                        .inline(Part.INLINE.equals(MimeMessageUtils.getDisposition(part))).build()
                );
            }
        }
    }
}