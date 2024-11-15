package app.tozzi.util;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Part;
import jakarta.mail.internet.MimePart;
import jakarta.mail.util.ByteArrayDataSource;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class IOUtilsTest {

    private static final String SAMPLE_CONTENT = "Sample Content";
    private static final String SAMPLE_FILE_NAME = "test.txt";

    @Test
    void testCreateDataSourceFromInputStream() throws Exception {
        try (var inputStream = new ByteArrayInputStream(SAMPLE_CONTENT.getBytes())) {
            var dataSource = IOUtils.createDataSource(inputStream, SAMPLE_FILE_NAME);
            assertNotNull(dataSource);
            assertEquals(SAMPLE_FILE_NAME, dataSource.getName());
            assertEquals(MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN, dataSource.getContentType());
            assertEquals(SAMPLE_CONTENT, new String(dataSource.getInputStream().readAllBytes()));
        }
    }

    @Test
    void testCreateDataSourceFromMimePart() throws Exception {
        var part = mock(MimePart.class);

        try (var partInputStream = new ByteArrayInputStream(SAMPLE_CONTENT.getBytes())) {
            try (MockedStatic<MimeMessageUtils> utilsMock = mockStatic(MimeMessageUtils.class)) {
                var ds = new ByteArrayDataSource(partInputStream, MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN);
                ds.setName(SAMPLE_FILE_NAME);
                when(MimeMessageUtils.getDataHandler(part)).thenReturn(new DataHandler(ds));
                when(MimeMessageUtils.getFileName(part)).thenReturn(SAMPLE_FILE_NAME);
                var dataSource = IOUtils.createDataSource(part, SAMPLE_FILE_NAME);
                assertNotNull(dataSource);
                assertEquals(SAMPLE_FILE_NAME, dataSource.getName());
                assertEquals(MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN, dataSource.getContentType());
                assertEquals(SAMPLE_CONTENT, new String(dataSource.getInputStream().readAllBytes()));
            }
        }
    }

    @Test
    void testFastCopy() throws Exception {
        try (var inputStream = new ByteArrayInputStream(SAMPLE_CONTENT.getBytes()); var outputStream = new ByteArrayOutputStream()) {
            IOUtils.fastCopy(inputStream, outputStream);
            assertEquals(SAMPLE_CONTENT, outputStream.toString());
        }
    }

    @Test
    void testGetFileMimeType() {
        try (MockedStatic<MimeTypesUtil> utilsMock = mockStatic(MimeTypesUtil.class)) {
            when(MimeTypesUtil.guessMimeType("txt")).thenReturn(MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN);
            var mimeType = IOUtils.getFileMimeType(SAMPLE_FILE_NAME);
            assertEquals(MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN, mimeType);
        }
    }

    @Test
    void testGetName() {
        try (MockedStatic<MimeMessageUtils> utilsMock = mockStatic(MimeMessageUtils.class)) {
            var part = mock(Part.class);
            when(MimeMessageUtils.getFileName(part)).thenReturn(SAMPLE_FILE_NAME);
            when(MimeMessageUtils.decodeText(anyString())).thenReturn(SAMPLE_FILE_NAME);
            var name = IOUtils.getName(part);
            assertEquals(SAMPLE_FILE_NAME, name);
        }
    }

    @Test
    void testGetContent() throws Exception {
        try (var inputStream = new ByteArrayInputStream(SAMPLE_CONTENT.getBytes())) {
            var content = IOUtils.getContent(inputStream);
            assertNotNull(content);
            assertEquals(SAMPLE_CONTENT, new String(content));
        }
    }

    @Test
    void testCreateDataSourceWithFallbackName() throws Exception {
        try (var inputStream = new ByteArrayInputStream(SAMPLE_CONTENT.getBytes())) {
            DataSource dataSource = IOUtils.createDataSource(inputStream, null);
            assertNotNull(dataSource);
            assertEquals("unknown_name", dataSource.getName());
            assertEquals(MimeTypesUtil.CONTENT_TYPE_OCTETSTREAM, dataSource.getContentType());
            assertEquals(SAMPLE_CONTENT, new String(dataSource.getInputStream().readAllBytes()));
        }
    }

    @Test
    void testCreateDataSource() throws Exception {
        try (var inputStream = new ByteArrayInputStream(SAMPLE_CONTENT.getBytes())) {
            DataSource dataSource = IOUtils.createDataSource(inputStream, SAMPLE_FILE_NAME);
            assertNotNull(dataSource);
            assertEquals(SAMPLE_FILE_NAME, dataSource.getName());
            assertEquals(MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN, dataSource.getContentType());
            assertEquals(SAMPLE_CONTENT, new String(dataSource.getInputStream().readAllBytes()));
        }
    }
}
