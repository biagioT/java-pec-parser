package app.tozzi.core;

import app.tozzi.model.DeliveryStatus;
import app.tozzi.model.exception.MailParserException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimePart;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DeliveryStatusHandlerTest {

    @Test
    void testLoadDeliveryStatus_withValidData() throws Exception {
        var sampleContent = "Action: failed\n" +
                "Status: 5.1.1\n" +
                "Diagnostic-Code: smtp; 550 5.1.1 Recipient address rejected\n" +
                "Remote-MTA: dns; mail.example.com\n" +
                "Reporting-MTA: dns; mailserver.example.com\n" +
                "Received-From-MTA: dns; smtp.example.com\n" +
                "Final-Recipient: rfc822; user@example.com\n";

        var part = Mockito.mock(MimePart.class);
        try (var inputStream = new ByteArrayInputStream(sampleContent.getBytes())) {
            Mockito.when(part.getInputStream()).thenReturn(inputStream);
            var deliveryStatus = DeliveryStatusHandler.loadDeliveryStatus(part);
            assertEquals(DeliveryStatus.Action.FAILED, deliveryStatus.getAction());
            assertEquals("5.1.1", deliveryStatus.getStatus());
            assertEquals(DeliveryStatus.StatusType.PERM_FAILURE, deliveryStatus.getStatusType());
            assertEquals("smtp", deliveryStatus.getDiagnosticCode().getType());
            assertEquals("550 5.1.1 Recipient address rejected", deliveryStatus.getDiagnosticCode().getDescription());
            assertEquals("dns", deliveryStatus.getRemoteMTA().getType());
            assertEquals("mail.example.com", deliveryStatus.getRemoteMTA().getAddress());
            assertEquals("dns", deliveryStatus.getReportingMTA().getType());
            assertEquals("mailserver.example.com", deliveryStatus.getReportingMTA().getAddress());
            assertEquals("dns", deliveryStatus.getReceivedFromMTA().getType());
            assertEquals("smtp.example.com", deliveryStatus.getReceivedFromMTA().getName());
            assertEquals("rfc822", deliveryStatus.getFinalRecipient().getType());
            assertEquals("user@example.com", deliveryStatus.getFinalRecipient().getAddress());
        }
    }

    @Test
    void testLoadDeliveryStatus_withMissingFields() throws Exception {
        var sampleContent = "Action: delayed\n" +
                "Status: 4.4.1\n" +
                "Diagnostic-Code: smtp; 421 4.4.1 Timeout\n" +
                "Final-Recipient: rfc822; admin@example.com\n";

        var part = Mockito.mock(MimePart.class);
        try (var inputStream = new ByteArrayInputStream(sampleContent.getBytes())) {
            Mockito.when(part.getInputStream()).thenReturn(inputStream);
            var deliveryStatus = DeliveryStatusHandler.loadDeliveryStatus(part);
            assertEquals(DeliveryStatus.Action.DELAYED, deliveryStatus.getAction());
            assertEquals("4.4.1", deliveryStatus.getStatus());
            assertEquals(DeliveryStatus.StatusType.TEMP_FAILURE, deliveryStatus.getStatusType());
            assertEquals("smtp", deliveryStatus.getDiagnosticCode().getType());
            assertEquals("421 4.4.1 Timeout", deliveryStatus.getDiagnosticCode().getDescription());
            assertEquals("rfc822", deliveryStatus.getFinalRecipient().getType());
            assertEquals("admin@example.com", deliveryStatus.getFinalRecipient().getAddress());
            assertNull(deliveryStatus.getRemoteMTA());
            assertNull(deliveryStatus.getReportingMTA());
            assertNull(deliveryStatus.getReceivedFromMTA());
        }
    }

    @Test
    void testLoadDeliveryStatus_withInvalidData() throws Exception {
        var sampleContent = "InvalidField: some_value\n";
        var part = Mockito.mock(MimePart.class);
        try (var inputStream = new ByteArrayInputStream(sampleContent.getBytes())) {
            Mockito.when(part.getInputStream()).thenReturn(inputStream);
            var deliveryStatus = DeliveryStatusHandler.loadDeliveryStatus(part);
            assertNotNull(deliveryStatus);
            assertNull(deliveryStatus.getAction());
            assertNull(deliveryStatus.getStatus());
            assertNull(deliveryStatus.getDiagnosticCode());
            assertNull(deliveryStatus.getRemoteMTA());
            assertNull(deliveryStatus.getReportingMTA());
            assertNull(deliveryStatus.getReceivedFromMTA());
            assertNull(deliveryStatus.getFinalRecipient());
        }
    }

    @Test
    void testLoadDeliveryStatus_withEmptyInput() throws Exception {
        String sampleContent = "";
        var part = Mockito.mock(MimePart.class);
        try (var inputStream = new ByteArrayInputStream(sampleContent.getBytes())) {
            Mockito.when(part.getInputStream()).thenReturn(inputStream);
            var deliveryStatus = DeliveryStatusHandler.loadDeliveryStatus(part);
            assertNotNull(deliveryStatus);
            assertNull(deliveryStatus.getAction());
            assertNull(deliveryStatus.getStatus());
            assertNull(deliveryStatus.getDiagnosticCode());
            assertNull(deliveryStatus.getRemoteMTA());
            assertNull(deliveryStatus.getReportingMTA());
            assertNull(deliveryStatus.getReceivedFromMTA());
            assertNull(deliveryStatus.getFinalRecipient());
        }
    }

    @Test
    void testLoadDeliveryStatus_withException() throws MessagingException, IOException {
        var part = Mockito.mock(MimePart.class);
        Mockito.when(part.getInputStream()).thenThrow(new RuntimeException("Simulated error"));
        assertThrows(MailParserException.class, () -> DeliveryStatusHandler.loadDeliveryStatus(part));
    }
}
