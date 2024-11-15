package app.tozzi.util;

import app.tozzi.model.exception.MailParserException;
import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Part;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MimeMessageUtilsTest {

    @Test
    void testGetMessageID_withValidID() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getMessageID()).thenReturn("<12345@example.com>");
        var messageID = MimeMessageUtils.getMessageID(mimeMessage);
        assertEquals("12345@example.com", messageID);
        verify(mimeMessage, times(1)).getMessageID();
    }

    @Test
    void testGetMessageID_throwsException() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getMessageID()).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getMessageID(mimeMessage));
    }

    @Test
    void testGetSubject() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getSubject()).thenReturn("Test Subject");
        var subject = MimeMessageUtils.getSubject(mimeMessage);
        assertEquals("Test Subject", subject);
        verify(mimeMessage, times(1)).getSubject();
    }

    @Test
    void testGetSubject_throwsException() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getSubject()).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getSubject(mimeMessage));
    }

    @Test
    void testGetTo() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        var recipients = new InternetAddress[]{new InternetAddress("to@example.com")};
        when(mimeMessage.getRecipients(Message.RecipientType.TO)).thenReturn(recipients);
        var toList = MimeMessageUtils.getTo(mimeMessage);
        assertEquals(1, toList.size());
        assertEquals("to@example.com", ((InternetAddress) toList.get(0)).getAddress());
        verify(mimeMessage, times(1)).getRecipients(Message.RecipientType.TO);
    }

    @Test
    void testGetHeader() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getHeader("X-Test-Header", ",")).thenReturn("HeaderValue");
        var header = MimeMessageUtils.getHeader(mimeMessage, "X-Test-Header");
        assertEquals("HeaderValue", header);
        verify(mimeMessage, times(1)).getHeader("X-Test-Header", ",");
    }

    @Test
    void testGetHeader_throwsException() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getHeader("X-Test-Header", ",")).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getHeader(mimeMessage, "X-Test-Header"));
    }

    @Test
    void testDecodeText() {
        var encodedText = "=?UTF-8?B?VGVzdCBUZXh0?=";
        var decodedText = "Test Text";
        var result = MimeMessageUtils.decodeText(encodedText);
        assertEquals(decodedText, result);
    }

    @Test
    void testGetReceivedDate() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        var receivedDate = new Date();
        when(mimeMessage.getReceivedDate()).thenReturn(receivedDate);
        var result = MimeMessageUtils.getReceivedDate(mimeMessage);
        assertEquals(receivedDate, result);
        verify(mimeMessage, times(1)).getReceivedDate();
    }

    @Test
    void testGetReceivedDate_throwsException() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getReceivedDate()).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getReceivedDate(mimeMessage));
    }

    @Test
    void testGetSentDate() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        var sentDate = new Date();
        when(mimeMessage.getSentDate()).thenReturn(sentDate);
        var result = MimeMessageUtils.getSentDate(mimeMessage);
        assertEquals(sentDate, result);
        verify(mimeMessage, times(1)).getSentDate();
    }

    @Test
    void testGetSentDate_throwsException() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getSentDate()).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getSentDate(mimeMessage));
    }

    @Test
    void testGetBCC() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        var bccRecipients = new InternetAddress[]{new InternetAddress("bcc@example.com")};
        when(mimeMessage.getRecipients(Message.RecipientType.BCC)).thenReturn(bccRecipients);
        var bccList = MimeMessageUtils.getBCC(mimeMessage);
        assertEquals(1, bccList.size());
        assertEquals("bcc@example.com", ((InternetAddress) bccList.get(0)).getAddress());
        verify(mimeMessage, times(1)).getRecipients(Message.RecipientType.BCC);
    }

    @Test
    void testGetBCC_empty() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getRecipients(Message.RecipientType.BCC)).thenReturn(null);
        var bccList = MimeMessageUtils.getBCC(mimeMessage);
        assertTrue(bccList.isEmpty());
    }

    @Test
    void testGetCC() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        var ccRecipients = new InternetAddress[]{new InternetAddress("cc@example.com")};
        when(mimeMessage.getRecipients(Message.RecipientType.CC)).thenReturn(ccRecipients);
        var ccList = MimeMessageUtils.getCC(mimeMessage);
        assertEquals(1, ccList.size());
        assertEquals("cc@example.com", ((InternetAddress) ccList.get(0)).getAddress());
        verify(mimeMessage, times(1)).getRecipients(Message.RecipientType.CC);
    }

    @Test
    void testGetCC_empty() throws Exception {
        var mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getRecipients(Message.RecipientType.CC)).thenReturn(null);
        var ccList = MimeMessageUtils.getCC(mimeMessage);
        assertTrue(ccList.isEmpty());
    }

    @Test
    void testGetContent() throws Exception {
        var part = mock(Part.class);
        var content = "Sample content";
        when(part.getContent()).thenReturn(content);
        var result = MimeMessageUtils.getContent(part);
        assertEquals(content, result);
        verify(part, times(1)).getContent();
    }

    @Test
    void testGetContent_throwsException() throws Exception {
        var part = mock(Part.class);
        when(part.getContent()).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getContent(part));
    }

    @Test
    void testGetFileName() throws Exception {
        var part = mock(Part.class);
        var fileName = "test.txt";
        when(part.getFileName()).thenReturn(fileName);
        var result = MimeMessageUtils.getFileName(part);
        assertEquals(fileName, result);
        verify(part, times(1)).getFileName();
    }

    @Test
    void testGetFileName_throwsException() throws Exception {
        var part = mock(Part.class);
        when(part.getFileName()).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getFileName(part));
    }

    @Test
    void testGetDataHandler() throws Exception {
        var part = mock(Part.class);
        var dataHandler = mock(DataHandler.class);
        when(part.getDataHandler()).thenReturn(dataHandler);
        var result = MimeMessageUtils.getDataHandler(part);
        assertEquals(dataHandler, result);
        verify(part, times(1)).getDataHandler();
    }

    @Test
    void testGetDataHandler_throwsException() throws Exception {
        var part = mock(Part.class);
        when(part.getDataHandler()).thenThrow(new MessagingException("Test Exception"));
        assertThrows(MailParserException.class, () -> MimeMessageUtils.getDataHandler(part));
    }


}
