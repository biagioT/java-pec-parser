package app.tozzi.core;

import app.tozzi.model.CertificateData;
import app.tozzi.model.Mail;
import app.tozzi.model.PEC;
import app.tozzi.model.PECReceipt;
import app.tozzi.model.exception.MailParserException;
import app.tozzi.util.MimeMessageUtils;
import app.tozzi.util.PECConstants;
import app.tozzi.util.XMLUtils;
import jakarta.activation.DataSource;
import jakarta.mail.internet.MimeMessage;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;

/**
 * PEC Management
 *
 * @author Biagio Tozzi
 */
public class PECHandler {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY;

    static {
        DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    }

    /**
     * Extracts PEC receipt from {@link PEC}
     *
     * @param pec {@link PEC}
     * @return {@link PECReceipt}
     */
    public static PECReceipt loadReceipt(PEC pec) {

        try {
            var pecReceipt = new PECReceipt();
            pecReceipt.setPec(pec);
            pecReceipt.setCertificateData(loadCertificateData(pec.getDatiCert().getInputStream()));
            return pecReceipt;

        } catch (Exception e) {
            throw new MailParserException("Error during PEC receipt processing", e);
        }
    }

    /**
     * Extracts a {@link PEC}
     *
     * @param originalMessage original mail message
     * @param envelope        PEC envelope
     * @param postaCert       postaCert.eml stream
     * @param datiCert        datiCert.xml stream
     * @return {@link PEC}
     */
    public static PEC loadPEC(Mail originalMessage, Mail envelope, DataSource postaCert, DataSource datiCert, MimeMessage mimeMessage) {

        try {
            var pec = new PEC();
            pec.setPostaCert(postaCert);
            pec.setDatiCert(datiCert);
            pec.setEnvelope(envelope);
            pec.setOriginalMessage(originalMessage);
            pec.setCertificateData(loadCertificateData(datiCert.getInputStream()));
            pec.setTransportHeaderValue(MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_TRASPORTO));
            pec.setReceiptHeaderValue(MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_RICEVUTA));
            pec.setReceiptTypeHeaderValue(MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_TIPO_RICEVUTA));
            pec.setSecurityCheckHeaderValue(MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_VERIFICA_SICUREZZA));
            pec.setErrorHeaderValue(MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_TRASPORTO_ERRORE));
            pec.setReferenceHeaderValue(MimeMessageUtils.getHeader(mimeMessage, PECConstants.X_RIFERIMENTO));
            return pec;

        } catch (Exception e) {
            throw new MailParserException("Error during PEC processing", e);
        }
    }

    /**
     * Extracts datiCert.xml
     *
     * @param inputStream datiCert.xml stream
     * @return {@link CertificateData}
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    static CertificateData loadCertificateData(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        var document = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(inputStream);
        var certificateData = new CertificateData();

        // Sender
        var sender = XMLUtils.getTextContent(document, PECConstants.DATICERT_MITTENTE_PATH);
        if (sender.isEmpty()) {
            throw new MailParserException("Path " + PECConstants.DATICERT_MITTENTE_PATH + " must be not null");
        }
        certificateData.setSender(sender.get());
        // Answers
        var answers = XMLUtils.getTextContent(document, PECConstants.DATICERT_RISPOSTE_PATH);
        if (answers.isEmpty()) {
            throw new MailParserException("Path " + PECConstants.DATICERT_RISPOSTE_PATH + " must be not null");
        }
        certificateData.setAnswers(answers.get());
        // Answers
        var issuer = XMLUtils.getTextContent(document, PECConstants.DATICERT_GESTORE_EMITTENTE_PATH);
        if (issuer.isEmpty()) {
            throw new MailParserException("Path " + PECConstants.DATICERT_GESTORE_EMITTENTE_PATH + " must be not null");
        }
        certificateData.setIssuer(issuer.get());
        // Subject
        XMLUtils.getTextContent(document, PECConstants.DATICERT_OGGETTO_PATH).ifPresent(certificateData::setSubject);
        // Recipients
        var recipients = XMLUtils.getTextAndAttribute(document, PECConstants.DATICERT_DESTINATARI_PATH, PECConstants.DATICERT_DESTINATARI_TIPO_ATTRIBUTE);
        if (recipients.isEmpty()) {
            throw new MailParserException("Path and attribute " + PECConstants.DATICERT_DESTINATARI_PATH + ";" + PECConstants.DATICERT_DESTINATARI_TIPO_ATTRIBUTE + " must be not null");
        }
        certificateData.setRecipients(recipients.entrySet().stream().map(e -> CertificateData.PECRecipients.builder().address(e.getKey()).type(CertificateData.PECRecipients.PECRecipientType.from(e.getValue())).build()).toList());
        // Date, hour and zone
        var zone = XMLUtils.getAttribute(document, PECConstants.DATICERT_DATA_PATH, PECConstants.DATICERT_DATA_ZONA_ATTRIBUTE);
        if (zone.isEmpty()) {
            throw new MailParserException("Path and attribute " + PECConstants.DATICERT_DATA_PATH + ";" + PECConstants.DATICERT_DATA_ZONA_ATTRIBUTE + " must be not null");
        }
        certificateData.setDate(new CertificateData.PECDate());
        certificateData.getDate().setZone(zone.get());
        var day = XMLUtils.getTextContent(document, PECConstants.DATICERT_DATA_GIORNO_PATH);
        if (day.isEmpty()) {
            throw new MailParserException("Path " + PECConstants.DATICERT_DATA_GIORNO_PATH + " must be not null");
        }
        certificateData.getDate().setDay(day.get());
        var hour = XMLUtils.getTextContent(document, PECConstants.DATICERT_DATA_ORA_PATH);
        if (hour.isEmpty()) {
            throw new MailParserException("Path " + PECConstants.DATICERT_DATA_ORA_PATH + " must be not null");
        }
        certificateData.getDate().setHour(hour.get());
        // Receipt type
        XMLUtils.getAttribute(document, PECConstants.DATICERT_RICEVUTA_PATH, PECConstants.DATICERT_RICEVUTA_TIPO_ATTRIBUTE).ifPresent(recType -> {
            certificateData.setReceiptType(CertificateData.ReceiptType.from(recType));
        });
        // Error
        var error = XMLUtils.getAttribute(document, PECConstants.DATICERT_POSTACERT_PATH, PECConstants.DATICERT_POSTACERT_ERRORE_ATTRIBUTE);
        if (error.isEmpty()) {
            throw new MailParserException("Path and attribute " + PECConstants.DATICERT_POSTACERT_PATH + ";" + PECConstants.DATICERT_POSTACERT_ERRORE_ATTRIBUTE + " must be not null");
        }
        certificateData.setError(CertificateData.PECError.from(error.get()));
        // Extended error
        XMLUtils.getTextContent(document, PECConstants.DATICERT_ERRORE_ESTESO_PATH).ifPresent(certificateData::setExtendedError);
        // Delivery
        XMLUtils.getTextContent(document, PECConstants.DATICERT_CONSEGNA_PATH).ifPresent(certificateData::setDelivery);
        // Receiving
        XMLUtils.getTextContent(document, PECConstants.DATICERT_RICEZIONE_PATH).ifPresent(certificateData::setReceiving);
        // Message ID
        XMLUtils.getTextContent(document, PECConstants.DATICERT_MESSAGE_ID_PATH).ifPresent(certificateData::setMessageID);
        // ID
        XMLUtils.getTextContent(document, PECConstants.DATICERT_IDENTIFICATIVO_PATH).ifPresent(certificateData::setId);
        // Type
        var type = XMLUtils.getAttribute(document, PECConstants.DATICERT_POSTACERT_PATH, PECConstants.DATICERT_IDENTIFICATIVO_TIPO_ATTRIBUTE);
        if (type.isEmpty()) {
            throw new MailParserException("Path and attribute " + PECConstants.DATICERT_POSTACERT_PATH + ";" + PECConstants.DATICERT_IDENTIFICATIVO_TIPO_ATTRIBUTE + " must be not null");
        }
        certificateData.setType(CertificateData.PostaCertType.from(type.get()));

        return certificateData;
    }

}
