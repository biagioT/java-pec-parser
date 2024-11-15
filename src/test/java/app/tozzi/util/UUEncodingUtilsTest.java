package app.tozzi.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UUEncodingUtilsTest {

    private static final String UUENCODED_CONTENT = """
            begin 644 dcode_uuencode
            +965E965E965E964`
            `
            end
            """;

    @Test
    public void containsEncodedAttachments() {
        assertTrue(UUEncodingUtils.containsEncodedAttachments(UUENCODED_CONTENT));
    }

    @Test
    public void getNextBeginIndex() {
        var index = UUEncodingUtils.getNextBeginIndex(UUENCODED_CONTENT);
        assertEquals(0, index);
    }

    @Test
    public void decodeAttachments() throws IOException {
        var content = UUEncodingUtils.decodeAttachments(UUENCODED_CONTENT);
        assertNotNull(content);
        assertEquals(1, content.size());
        assertNotNull(content.get(0).getDataSource());
        assertNotNull(content.get(0).getDataSource().getInputStream());
        assertTrue(content.get(0).getDataSource().getInputStream().available() > 0);
        assertEquals("dcode_uuencode", content.get(0).getName());
    }
}
