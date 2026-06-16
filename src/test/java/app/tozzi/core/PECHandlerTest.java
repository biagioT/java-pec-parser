package app.tozzi.core;

import app.tozzi.model.exception.MailParserException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class PECHandlerTest {

    @Test
    public void testXXEProtection() throws Exception {
        // XML payload con XXE
        String xxePayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE foo [ <!ENTITY xxe SYSTEM \"file:///etc/passwd\"> ]>" +
                "<postacert><intestazione><mittente>&xxe;</mittente></intestazione></postacert>";

        var is = new ByteArrayInputStream(xxePayload.getBytes(StandardCharsets.UTF_8));

        // Deve lanciare un'eccezione a causa della protezione XXE (disallow-doctype-decl)
        assertThrows(org.xml.sax.SAXParseException.class, () -> {
            PECHandler.loadCertificateData(is);
        });
    }

    @Test
    public void testValidXML() throws Exception {
        String validXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<postacert errore=\"nessuno\" tipo=\"accettazione\">" +
                "<intestazione><mittente>test@pec.it</mittente><risposte>test@pec.it</risposte>" +
                "<destinatari tipo=\"certificato\">test2@pec.it</destinatari></intestazione>" +
                "<dati><msgid>12345</msgid><ricevuta tipo=\"accettazione\"/>" +
                "<gestore-emittente>Gestore</gestore-emittente>" +
                "<data zona=\"+0100\"><giorno>2026-06-16</giorno><ora>10:00:00</ora></data>" +
                "</dati></postacert>";

        var is = new ByteArrayInputStream(validXml.getBytes(StandardCharsets.UTF_8));
        var certData = PECHandler.loadCertificateData(is);

        assertNotNull(certData);
        assertEquals("test@pec.it", certData.getSender());
    }
}
