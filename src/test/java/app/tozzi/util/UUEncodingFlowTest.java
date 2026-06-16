package app.tozzi.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per verificare il flusso UUEncoding come implementato in MailParser.extractContent.
 * Simula esattamente la logica delle righe 231-236 di MailParser.java.
 */
public class UUEncodingFlowTest {

    private static final String BODY_WITH_UU = """
            Questo è il testo della mail.
            Seconda riga di testo.
            begin 644 test_file.txt
            +965E965E965E964`
            `
            end
            """;

    /**
     * Test che simula ESATTAMENTE la logica attuale di MailParser.extractContent (righe 231-236).
     * Verifica se il flusso attuale è corretto o buggato.
     */
    @Test
    public void testCurrentMailParserFlow() {
        // Simula MailParser.java linee 231-236:
        var body = BODY_WITH_UU;

        // Verifica che il body contenga allegati UU
        assertTrue(UUEncodingUtils.containsEncodedAttachments(body),
                "Il body dovrebbe contenere allegati UU-encoded");

        // Il getNextBeginIndex deve trovare l'inizio del blocco UU
        var beginIndex = UUEncodingUtils.getNextBeginIndex(body);
        assertTrue(beginIndex > 0, "L'indice begin dovrebbe essere > 0 (c'è testo prima)");

        // Riga 234: body viene troncato PRIMA del blocco UU
        body = body.substring(0, beginIndex);
        assertEquals("Questo è il testo della mail.\nSeconda riga di testo.\n", body,
                "Il body troncato dovrebbe contenere solo il testo");

        // Riga 235: decodeAttachments viene chiamato con il body TRONCATO
        var attachments = UUEncodingUtils.decodeAttachments(body);

        // ⚠️ QUESTO È IL PUNTO CRITICO: il body troncato NON contiene più i blocchi UU!
        // Se il risultato è vuoto, allora c'è un bug nell'integrazione MailParser.
        System.out.println("Numero allegati trovati con body troncato: " + attachments.size());

        // Verifica: con il body originale (non troncato) gli allegati vengono trovati
        var attachmentsFromOriginal = UUEncodingUtils.decodeAttachments(BODY_WITH_UU);
        System.out.println("Numero allegati trovati con body originale: " + attachmentsFromOriginal.size());

        // Se questo assert fallisce, il codice attuale di MailParser è buggato:
        // passa il body troncato a decodeAttachments, che non troverà mai nulla.
        if (attachments.isEmpty() && !attachmentsFromOriginal.isEmpty()) {
            System.out.println("⚠️ CONFERMATO BUG: MailParser tronca il body PRIMA di passarlo a decodeAttachments");
            System.out.println("   Il body troncato non contiene blocchi UU, quindi decodeAttachments restituisce lista vuota.");
            System.out.println("   Gli allegati vengono persi.");
        }

        // Test espliciti per entrambi i casi
        assertFalse(attachmentsFromOriginal.isEmpty(),
                "decodeAttachments con body originale DEVE trovare allegati");

        // Se questo assert PASSA (lista vuota), allora il bug è confermato
        assertTrue(attachments.isEmpty(),
                "decodeAttachments con body troncato NON trova allegati - BUG CONFERMATO");
    }

    /**
     * Test che verifica che decodeAttachments funziona correttamente
     * quando riceve il body completo (come dovrebbe essere).
     */
    @Test
    public void testCorrectFlow() {
        var body = BODY_WITH_UU;

        assertTrue(UUEncodingUtils.containsEncodedAttachments(body));

        // Salva il body originale PRIMA di troncarlo
        var originalBody = body;

        // Tronca il body per il testo
        body = body.substring(0, UUEncodingUtils.getNextBeginIndex(body));

        // Decodifica gli allegati dal body ORIGINALE (non troncato)
        var attachments = UUEncodingUtils.decodeAttachments(originalBody);

        assertFalse(attachments.isEmpty(), "Con il body originale gli allegati devono essere trovati");
        assertEquals(1, attachments.size());
        assertEquals("test_file.txt", attachments.get(0).getName());
    }
}
