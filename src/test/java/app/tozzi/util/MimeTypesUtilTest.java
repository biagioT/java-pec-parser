package app.tozzi.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MimeTypesUtilTest {

    @Test
    void testGuessMimeType_withValidExtension() {
        assertEquals("video/x-ms-wmv", MimeTypesUtil.guessMimeType("wmv"));
        assertEquals("video/x-msvideo", MimeTypesUtil.guessMimeType("avi"));
        assertEquals("application/andrew-inset", MimeTypesUtil.guessMimeType("ez"));
        assertEquals("application/octet-stream", MimeTypesUtil.guessMimeType("unknown")); // Estensione sconosciuta
    }

    @Test
    void testGuessMimeType_withMultipleMatches() {
        assertEquals("audio/midi", MimeTypesUtil.guessMimeType("midi"));
    }

    @Test
    void testGuessMimeType_withNullExtension() {
        assertNull(MimeTypesUtil.guessMimeType(null));
    }

    @Test
    void testGuessExtension_withValidMimeType() {
        assertEquals("wm", MimeTypesUtil.guessExtension("video/x-ms-wmv"));
        assertEquals("avi", MimeTypesUtil.guessExtension("video/x-msvideo"));
        assertEquals("ez", MimeTypesUtil.guessExtension("application/andrew-inset"));
    }

    @Test
    void testGuessExtension_withUnknownMimeType() {
        assertNull(MimeTypesUtil.guessExtension("application/unknown"));
    }

    @Test
    void testExcludeMimeTypes_withMatchingPattern() {
        var mimeTypes = List.of("video/x-ms-wmv", "video/x-msvideo", "application/andrew-inset");
        assertEquals("video/x-ms-wmv", MimeTypesUtil.excludeMimeTypes(List.of("/vnd."), mimeTypes));
    }

    @Test
    void testExcludeMimeTypes_withMultiplePatterns() {
        var mimeTypes = List.of("video/x-ms-wmv", "video/x-msvideo", "application/andrew-inset");
        assertEquals("application/andrew-inset", MimeTypesUtil.excludeMimeTypes(List.of("/x-"), mimeTypes));
    }

    @Test
    void testExcludeMimeTypes_withNoExclusion() {
        var mimeTypes = List.of("video/x-ms-wmv", "video/x-msvideo", "application/andrew-inset");
        assertEquals("video/x-ms-wmv", MimeTypesUtil.excludeMimeTypes(List.of(), mimeTypes));
    }
}

