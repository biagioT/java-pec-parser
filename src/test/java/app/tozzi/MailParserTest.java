package app.tozzi;

import app.tozzi.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MailParserTest {

    @Test
    public void mailTest_1() throws IOException {

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("Test - Simple Mail.eml")) {
            var pe = MailParser.getInstance(true).parse(inputStream);
            assertNotNull(pe);
            assertInstanceOf(Mail.class, pe);
            assertEquals(ParsedEntityType.MAIL, pe.getType());
            var mail = (Mail) pe;
            assertEquals("EA+dAJ_-oQ-6nyBKVWEFWJyq2Qx_sboF=k9VrediOGf8OO+v7rg@mail.gmail.com", mail.getMessageID());
            assertNotNull(mail.getTo());
            assertEquals(1, mail.getTo().size());
            assertEquals("biagio.tozzi@gmail.com", mail.getTo().get(0).getEmail());
            assertEquals("Biagio Tozzi", mail.getTo().get(0).getName());
            assertNotNull(mail.getFrom());
            assertEquals(1, mail.getFrom().size());
            assertEquals("test@tozzi.app", mail.getFrom().get(0).getEmail());
            assertEquals("Tozzi APP", mail.getFrom().get(0).getName());
            assertNotNull(mail.getCc());
            assertEquals(0, mail.getCc().size());
            assertNotNull(mail.getBcc());
            assertEquals(0, mail.getBcc().size());
            assertNotNull(mail.getBodyHTML());
            assertTrue(mail.getBodyHTML().contains("<div>Simple Mail!</div>"));
            assertNotNull(mail.getBodyTXT());
            assertTrue(mail.getBodyTXT().contains("Simple Mail!"));
            assertNotNull(mail.getSentDate());
            assertNull(mail.getDeliveryStatus());
            assertNotNull(mail.getAttachments());
            assertEquals(1, mail.getAttachments().size());
            assertEquals("640px-Flag_of_Italy.svg.png", mail.getAttachments().get(0).getName());
            assertEquals("ii_m3it0i7x0", mail.getAttachments().get(0).getXAttachmentID());
            assertEquals("<ii_m3it0i7x0>", mail.getAttachments().get(0).getContentID());
            assertTrue(mail.getAttachments().get(0).isInline());
            assertNotNull(mail.getAttachments().get(0).getDataSource());
            assertNotNull(mail.getAttachments().get(0).getDataSource().getInputStream());
            assertTrue(mail.getAttachments().get(0).getDataSource().getInputStream().available() > 0);
            assertNotNull(mail.getHeaders());
            assertFalse(mail.getHeaders().isEmpty());
            assertFalse(mail.isHasDeliveryStatus());
            assertNull(mail.getReplyToMessageID());
            assertNull(mail.getReplyToHistoryMessagesID());
        }
    }

    @Test
    public void mailTest_2() throws IOException {

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("Test - Simple Mail.eml")) {
            var pe = MailParser.getInstance(false).parse(inputStream);
            assertNotNull(pe);
            assertInstanceOf(Mail.class, pe);
            assertEquals(ParsedEntityType.MAIL, pe.getType());
            var mail = (Mail) pe;
            assertEquals("EA+dAJ_-oQ-6nyBKVWEFWJyq2Qx_sboF=k9VrediOGf8OO+v7rg@mail.gmail.com", mail.getMessageID());
            assertNotNull(mail.getTo());
            assertEquals(1, mail.getTo().size());
            assertEquals("biagio.tozzi@gmail.com", mail.getTo().get(0).getEmail());
            assertEquals("Biagio Tozzi", mail.getTo().get(0).getName());
            assertNotNull(mail.getFrom());
            assertEquals(1, mail.getFrom().size());
            assertEquals("test@tozzi.app", mail.getFrom().get(0).getEmail());
            assertEquals("Tozzi APP", mail.getFrom().get(0).getName());
            assertNotNull(mail.getCc());
            assertEquals(0, mail.getCc().size());
            assertNotNull(mail.getBcc());
            assertEquals(0, mail.getBcc().size());
            assertNotNull(mail.getBodyHTML());
            assertTrue(mail.getBodyHTML().contains("<div>Simple Mail!</div>"));
            assertNotNull(mail.getBodyTXT());
            assertTrue(mail.getBodyTXT().contains("Simple Mail!"));
            assertNotNull(mail.getSentDate());
            assertNull(mail.getDeliveryStatus());
            assertNotNull(mail.getAttachments());
            assertEquals(1, mail.getAttachments().size());
            assertEquals("640px-Flag_of_Italy.svg.png", mail.getAttachments().get(0).getName());
            assertEquals("ii_m3it0i7x0", mail.getAttachments().get(0).getXAttachmentID());
            assertEquals("<ii_m3it0i7x0>", mail.getAttachments().get(0).getContentID());
            assertTrue(mail.getAttachments().get(0).isInline());
            assertNotNull(mail.getAttachments().get(0).getDataSource());
            assertNotNull(mail.getAttachments().get(0).getDataSource().getInputStream());
            assertTrue(mail.getAttachments().get(0).getDataSource().getInputStream().available() > 0);
            assertNull(mail.getHeaders());
            assertFalse(mail.isHasDeliveryStatus());
            assertNull(mail.getReplyToMessageID());
            assertNull(mail.getReplyToHistoryMessagesID());
        }
    }

    @Test
    public void mailTest_3() throws IOException {

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("Test - Simple Mail (2).eml")) {
            var pe = MailParser.getInstance(false).parse(inputStream);
            assertNotNull(pe);
            assertInstanceOf(Mail.class, pe);
            assertEquals(ParsedEntityType.MAIL, pe.getType());
            var mail = (Mail) pe;
            assertEquals("EA+dAJ_-Dr55a82Z-wzjQNz=Wku2KADjLWBEOTY2=umsUawTNTQ@mail.gmail.com", mail.getMessageID());
            assertNotNull(mail.getTo());
            assertEquals(1, mail.getTo().size());
            assertEquals("biagio.tozzi@gmail.com", mail.getTo().get(0).getEmail());
            assertEquals("Biagio Tozzi", mail.getTo().get(0).getName());
            assertNotNull(mail.getFrom());
            assertEquals(1, mail.getFrom().size());
            assertEquals("test@tozzi.app", mail.getFrom().get(0).getEmail());
            assertEquals("Tozzi APP", mail.getFrom().get(0).getName());
            assertNotNull(mail.getCc());
            assertEquals(0, mail.getCc().size());
            assertNotNull(mail.getBcc());
            assertEquals(0, mail.getBcc().size());
            assertNotNull(mail.getBodyHTML());
            assertTrue(mail.getBodyHTML().contains("<div>Test - Simple Mail 2!</div>"));
            assertNotNull(mail.getBodyTXT());
            assertTrue(mail.getBodyTXT().contains("Simple Mail 2!"));
            assertNotNull(mail.getSentDate());
            assertNull(mail.getDeliveryStatus());
            assertNotNull(mail.getAttachments());
            assertEquals(1, mail.getAttachments().size());
            assertEquals("640px-Flag_of_Italy.svg.png", mail.getAttachments().get(0).getName());
            assertEquals("f_m3iui2v40", mail.getAttachments().get(0).getXAttachmentID());
            assertEquals("<f_m3iui2v40>", mail.getAttachments().get(0).getContentID());
            assertFalse(mail.getAttachments().get(0).isInline());
            assertNotNull(mail.getAttachments().get(0).getDataSource());
            assertNotNull(mail.getAttachments().get(0).getDataSource().getInputStream());
            assertTrue(mail.getAttachments().get(0).getDataSource().getInputStream().available() > 0);
            assertNull(mail.getHeaders());
            assertFalse(mail.isHasDeliveryStatus());
            assertNull(mail.getReplyToMessageID());
            assertNull(mail.getReplyToHistoryMessagesID());
        }
    }

    @Test
    public void mailTest_4() throws IOException {

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("Test - Simple Mail (3).eml")) {
            var pe = MailParser.getInstance(false).parse(inputStream);
            assertNotNull(pe);
            assertInstanceOf(Mail.class, pe);
            assertEquals(ParsedEntityType.MAIL, pe.getType());
            var mail = (Mail) pe;
            assertEquals("EA+wfBhpup681yEmHDfUjNeBUg-sVGumaX76c6iigenOaT_p_aQ@mail.gmail.com", mail.getMessageID());
            assertNotNull(mail.getTo());
            assertEquals(1, mail.getTo().size());
            assertEquals("test@tozzi.app", mail.getTo().get(0).getEmail());
            assertEquals("Tozzi APP", mail.getTo().get(0).getName());
            assertNotNull(mail.getFrom());
            assertEquals(1, mail.getFrom().size());
            assertEquals("biagio.tozzi@gmail.com", mail.getFrom().get(0).getEmail());
            assertEquals("Biagio Tozzi", mail.getFrom().get(0).getName());
            assertNotNull(mail.getCc());
            assertEquals(1, mail.getCc().size());
            assertNull(mail.getCc().get(0).getName());
            assertEquals("test2@tozzi.app", mail.getCc().get(0).getEmail());
            assertNotNull(mail.getBcc());
            assertEquals(0, mail.getBcc().size());
            assertNotNull(mail.getBodyHTML());
            assertTrue(mail.getBodyHTML().contains("Reply!</div>"));
            assertNotNull(mail.getBodyTXT());
            assertTrue(mail.getBodyTXT().contains("Reply!"));
            assertNotNull(mail.getSentDate());
            assertNull(mail.getDeliveryStatus());
            assertNotNull(mail.getAttachments());
            assertEquals(0, mail.getAttachments().size());
            assertNull(mail.getHeaders());
            assertFalse(mail.isHasDeliveryStatus());
            assertNotNull(mail.getReplyToMessageID());
            assertEquals("EA+dAJ_-Dr55a82Z-wzjQNz=Wku2KADjLWBEOTY2=umsUawTNTQ@mail.gmail.com", mail.getReplyToMessageID());
            assertNotNull(mail.getReplyToHistoryMessagesID());
            assertEquals(1, mail.getReplyToHistoryMessagesID().size());
            assertEquals("EA+dAJ_-Dr55a82Z-wzjQNz=Wku2KADjLWBEOTY2=umsUawTNTQ@mail.gmail.com", mail.getReplyToHistoryMessagesID().get(0));
        }
    }

    @Test
    public void deliveryStatusTest() throws IOException {

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("Delivery Status Notification (Failure).eml")) {
            var pe = MailParser.getInstance(true).parse(inputStream);
            assertNotNull(pe);
            assertInstanceOf(Mail.class, pe);
            assertEquals(ParsedEntityType.MAIL, pe.getType());
            var mail = (Mail) pe;
            assertEquals("773764b2.050a0220.544c6.2d4b.GMR@mx.google.com", mail.getMessageID());
            assertNotNull(mail.getFrom());
            assertEquals(1, mail.getFrom().size());
            assertEquals("mailer-daemon@googlemail.com", mail.getFrom().get(0).getEmail());
            assertNotNull(mail.getFrom());
            assertEquals(1, mail.getTo().size());
            assertEquals("biagio.tozzi@gmail.com", mail.getTo().get(0).getEmail());
            assertNotNull(mail.getCc());
            assertEquals(0, mail.getCc().size());
            assertNotNull(mail.getBcc());
            assertEquals(0, mail.getBcc().size());
            assertTrue(mail.isHasDeliveryStatus());
            assertNotNull(mail.getDeliveryStatus());
            assertEquals("5.7.0", mail.getDeliveryStatus().getStatus());
            assertEquals(DeliveryStatus.Action.FAILED, mail.getDeliveryStatus().getAction());
            assertEquals("dns", mail.getDeliveryStatus().getReportingMTA().getType());
            assertEquals("googlemail.com", mail.getDeliveryStatus().getReportingMTA().getAddress());
            assertEquals("dns", mail.getDeliveryStatus().getRemoteMTA().getType());
            assertEquals("mx01.ionos.it. (217.72.192.00, the server for the domain tozzi.app.)", mail.getDeliveryStatus().getRemoteMTA().getAddress());
            assertEquals("rfc822", mail.getDeliveryStatus().getFinalRecipient().getType());
            assertEquals("test@tozzi.app", mail.getDeliveryStatus().getFinalRecipient().getAddress());
            assertEquals(DeliveryStatus.StatusType.PERM_FAILURE, mail.getDeliveryStatus().getStatusType());
            assertNotNull(mail.getDeliveryStatus().getDiagnosticCode());
            assertEquals("smtp", mail.getDeliveryStatus().getDiagnosticCode().getType());
            assertEquals("550-Requested action not taken: mailbox unavailable", mail.getDeliveryStatus().getDiagnosticCode().getDescription());
            assertNotNull(mail.getReplyToMessageID());
            assertEquals("EA+wfBhrSAT7Jc6+8Y_pi_x6SgRzJO5CT6Mc-uKb57n6aff+u9g@mail.gmail.com", mail.getReplyToMessageID());
            assertNotNull(mail.getReplyToHistoryMessagesID());
            assertEquals(1, mail.getReplyToHistoryMessagesID().size());
            assertEquals("EA+wfBhrSAT7Jc6+8Y_pi_x6SgRzJO5CT6Mc-uKb57n6aff+u9g@mail.gmail.com", mail.getReplyToHistoryMessagesID().get(0));
        }
    }

    @Test
    public void pecReceipt_1() throws IOException {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("accettazione.eml")) {
            var pe = MailParser.getInstance(true).parse(inputStream);
            assertNotNull(pe);
            assertInstanceOf(PECReceipt.class, pe);
            assertEquals(ParsedEntityType.PEC_RECEIPT, pe.getType());
            var receipt = (PECReceipt) pe;
            assertNotNull(receipt.getPec());
            assertNotNull(receipt.getCertificateData());
            assertEquals("sender@fakepec.it", receipt.getCertificateData().getSender());
            assertEquals("rec@fakepec.it", receipt.getCertificateData().getRecipients().get(0).getAddress());
            assertEquals(CertificateData.PECRecipients.PECRecipientType.CERTIFICATO, receipt.getCertificateData().getRecipients().get(0).getType());
            assertEquals("<SN05IE$951DEC16C1CFD3E4FD8FF1B1D24A99AE@fakepec.it>", receipt.getCertificateData().getMessageID());
            assertEquals("opec210312.20241115182038.288127.606.1.53@fakepec.it", receipt.getCertificateData().getId());
            assertEquals(CertificateData.PECError.NESSUNO, receipt.getCertificateData().getError());
            assertNull(receipt.getCertificateData().getExtendedError());
            assertEquals("sender@fakepec.it", receipt.getCertificateData().getAnswers());
            assertEquals("Test PEC", receipt.getCertificateData().getSubject());
            assertEquals("FAKEPEC PEC S.p.A.", receipt.getCertificateData().getIssuer());
            assertEquals(CertificateData.PostaCertType.ACCETTAZIONE, receipt.getCertificateData().getType());
            assertNotNull(receipt.getPec().getCertificateData());
            assertEquals("sender@fakepec.it", receipt.getPec().getCertificateData().getSender());
            assertEquals("rec@fakepec.it", receipt.getPec().getCertificateData().getRecipients().get(0).getAddress());
            assertEquals(CertificateData.PECRecipients.PECRecipientType.CERTIFICATO, receipt.getPec().getCertificateData().getRecipients().get(0).getType());
            assertEquals("<SN05IE$951DEC16C1CFD3E4FD8FF1B1D24A99AE@fakepec.it>", receipt.getPec().getCertificateData().getMessageID());
            assertEquals("opec210312.20241115182038.288127.606.1.53@fakepec.it", receipt.getPec().getCertificateData().getId());
            assertEquals(CertificateData.PECError.NESSUNO, receipt.getPec().getCertificateData().getError());
            assertNull(receipt.getPec().getCertificateData().getExtendedError());
            assertEquals("sender@fakepec.it", receipt.getPec().getCertificateData().getAnswers());
            assertEquals("Test PEC", receipt.getPec().getCertificateData().getSubject());
            assertEquals("FAKEPEC PEC S.p.A.", receipt.getPec().getCertificateData().getIssuer());
            assertEquals(CertificateData.PostaCertType.ACCETTAZIONE, receipt.getPec().getCertificateData().getType());
            assertNotNull(receipt.getPec().getCertificateData().getDate().getDay());
            assertNotNull(receipt.getPec().getCertificateData().getDate().getHour());
            assertNotNull(receipt.getPec().getCertificateData().getDate().getZone());
            assertNull(receipt.getPec().getOriginalMessage());
            assertNotNull(receipt.getPec().getDatiCert());
            assertNull(receipt.getPec().getPostaCert());
            assertNotNull(receipt.getPec().getEnvelope());
            assertEquals("ACCETTAZIONE: Test PEC", receipt.getPec().getEnvelope().getSubject());
            assertEquals("posta-certificata@fakepec.it", receipt.getPec().getEnvelope().getFrom().get(0).getEmail());
            assertEquals("opec210312.20241115182038.288127.606.1.771.53@fakepec.it", receipt.getPec().getEnvelope().getMessageID());
            assertFalse(receipt.getPec().getEnvelope().isHasDeliveryStatus());
            assertEquals("sender@fakepec.it", receipt.getPec().getEnvelope().getTo().get(0).getEmail());
        }
    }

    @Test
    public void pecReceipt_2() throws IOException {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("consegna.eml")) {
            var pe = MailParser.getInstance(true).parse(inputStream);
            assertNotNull(pe);
            assertInstanceOf(PECReceipt.class, pe);
            assertEquals(ParsedEntityType.PEC_RECEIPT, pe.getType());
            var receipt = (PECReceipt) pe;
            assertNotNull(receipt.getPec());
            assertNotNull(receipt.getCertificateData());
            assertEquals("sender@fakepec.it", receipt.getCertificateData().getSender());
            assertEquals("rec@pec.it", receipt.getCertificateData().getRecipients().get(0).getAddress());
            assertEquals(CertificateData.PECRecipients.PECRecipientType.CERTIFICATO, receipt.getCertificateData().getRecipients().get(0).getType());
            assertEquals("<SN05IE$951DEC16C1CFD3E4FD8FF1B1D24A99AE@fakepec.it>", receipt.getCertificateData().getMessageID());
            assertEquals("opec210312.20241115182038.288127.606.1.53@fakepec.it", receipt.getCertificateData().getId());
            assertEquals(CertificateData.PECError.NO_DEST, receipt.getCertificateData().getError());
            assertNotNull(receipt.getCertificateData().getExtendedError());
            assertEquals("5.1.1 - FAKE Pec S.p.A. - indirizzo non valido", receipt.getCertificateData().getExtendedError());
            assertEquals("sender@fakepec.it", receipt.getCertificateData().getAnswers());
            assertEquals("Test PEC", receipt.getCertificateData().getSubject());
            assertEquals("FAKE PEC S.p.A.", receipt.getCertificateData().getIssuer());
            assertEquals(CertificateData.PostaCertType.ERRORE_CONSEGNA, receipt.getCertificateData().getType());
            assertNotNull(receipt.getPec().getCertificateData());
            assertEquals("sender@fakepec.it", receipt.getPec().getCertificateData().getSender());
            assertEquals("rec@pec.it", receipt.getPec().getCertificateData().getRecipients().get(0).getAddress());
            assertEquals(CertificateData.PECRecipients.PECRecipientType.CERTIFICATO, receipt.getPec().getCertificateData().getRecipients().get(0).getType());
            assertEquals("<SN05IE$951DEC16C1CFD3E4FD8FF1B1D24A99AE@fakepec.it>", receipt.getPec().getCertificateData().getMessageID());
            assertEquals("opec210312.20241115182038.288127.606.1.53@fakepec.it", receipt.getPec().getCertificateData().getId());
            assertEquals(CertificateData.PECError.NO_DEST, receipt.getPec().getCertificateData().getError());
            assertNotNull(receipt.getPec().getCertificateData().getExtendedError());
            assertEquals("sender@fakepec.it", receipt.getPec().getCertificateData().getAnswers());
            assertEquals("Test PEC", receipt.getPec().getCertificateData().getSubject());
            assertEquals("FAKE PEC S.p.A.", receipt.getPec().getCertificateData().getIssuer());
            assertEquals(CertificateData.PostaCertType.ERRORE_CONSEGNA, receipt.getPec().getCertificateData().getType());
            assertNotNull(receipt.getPec().getCertificateData().getDate().getDay());
            assertNotNull(receipt.getPec().getCertificateData().getDate().getHour());
            assertNotNull(receipt.getPec().getCertificateData().getDate().getZone());
            assertNull(receipt.getPec().getOriginalMessage());
            assertNotNull(receipt.getPec().getDatiCert());
            assertNull(receipt.getPec().getPostaCert());
            assertNotNull(receipt.getPec().getEnvelope());
            assertEquals("AVVISO DI MANCATA CONSEGNA: Test PEC", receipt.getPec().getEnvelope().getSubject());
            assertEquals("posta-certificata@fakepec.it", receipt.getPec().getEnvelope().getFrom().get(0).getEmail());
            assertEquals("opec210312.20241115182103.186549.961.1.530.42@fakepec.it", receipt.getPec().getEnvelope().getMessageID());
            assertFalse(receipt.getPec().getEnvelope().isHasDeliveryStatus());
            assertEquals("sender@fakepec.it", receipt.getPec().getEnvelope().getTo().get(0).getEmail());
        }
    }
}
