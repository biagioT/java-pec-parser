package app.tozzi.util;

import jakarta.activation.DataSource;
import jakarta.activation.FileTypeMap;
import jakarta.mail.Part;
import jakarta.mail.internet.MimePart;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * IO Utilities
 *
 * @author Biagio Tozzi
 */
public class IOUtils {

    /**
     * Creates a {@link DataSource} from {@link MimePart}
     *
     * @param part {@link MimePart}
     * @return {@link DataSource}
     * @throws IOException
     */
    public static DataSource createDataSource(MimePart part) throws IOException {
        return createDataSource(part, null);
    }

    /**
     * Creates a {@link DataSource} from {@link InputStream} and file name
     *
     * @param inputStream {@link InputStream}
     * @param name File name
     * @return {@link DataSource}
     * @throws IOException
     */
    public static DataSource createDataSource(InputStream inputStream, String name) throws IOException {
        byte[] content = getContent(inputStream);
        name = name == null ? "unknown_name" : name;
        ByteArrayDataSource result = new ByteArrayDataSource(content, getFileMimeType(name));
        result.setName(name);
        return result;
    }

    /**
     * Creates a {@link DataSource} from {@link MimePart} and file name
     *
     * @param part {@link MimePart}
     * @param name File name
     * @return {@link DataSource}
     * @throws IOException
     */
    public static DataSource createDataSource(MimePart part, String name) throws IOException {
        var dataSource = MimeMessageUtils.getDataHandler(part).getDataSource();
        byte[] content = getContent(dataSource.getInputStream());
        var fileName = name != null ? name : loadNameForDataSource(part);
        var result = new ByteArrayDataSource(content, getBaseMimeType(dataSource, fileName));
        result.setName(fileName);
        return result;
    }

    /**
     * Copy {@link InputStream} into {@link OutputStream} with buffer of 16KB
     *
     * @param src {@link InputStream}
     * @param dest {@link OutputStream}
     * @throws IOException
     */
    public static void fastCopy(InputStream src, OutputStream dest) throws IOException {
        var inputChannel = Channels.newChannel(src);
        var outputChannel = Channels.newChannel(dest);
        fastCopy(inputChannel, outputChannel);
        inputChannel.close();
        outputChannel.close();
    }

    private static String loadNameForDataSource(MimePart part) {
        var fileName = MimeMessageUtils.getFileName(part);
        return fileName != null && !fileName.trim().isEmpty() ? MimeMessageUtils.decodeText(fileName) : getName(part);
    }

    static String getName(Part part) {

        var fileName = MimeMessageUtils.getFileName(part);

        if (fileName != null && !fileName.trim().isEmpty())
            return MimeMessageUtils.decodeText(fileName);

        var extension = getExtension(part);
        extension = extension != null && !extension.trim().isEmpty() ? ("." + extension) : "";

        if (Part.INLINE.equals(MimeMessageUtils.getDisposition(part))) {
            return "unknown_name_inline" + extension;
        }

        return "unknown_name" + extension;
    }

    private static String getExtension(Part part) {

        var fullMimeType = MimeMessageUtils.getContentType(part);

        if (fullMimeType != null && !fullMimeType.trim().isEmpty()) {
            return MimeTypesUtil.guessExtension(fullMimeType);
        }

        return null;
    }

    private static String getBaseMimeType(DataSource dataSource, String fileName) {
        var fullMimeType = dataSource.getContentType();

        if (fullMimeType == null || fullMimeType.trim().isEmpty()) {
            return getFileMimeType(fileName);
        }

        var pos = fullMimeType.indexOf(';');
        if (pos >= 0) {
            return fullMimeType.substring(0, pos);
        }
        return fullMimeType;
    }

    static byte[] getContent(InputStream is) throws IOException {
        byte[] result;
        var os = new ByteArrayOutputStream();
        fastCopy(is, os);
        result = os.toByteArray();
        return result;
    }

    private static void fastCopy(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        var buffer = ByteBuffer.allocateDirect(16 * 1024);

        while (src.read(buffer) != -1) {
            ((Buffer) buffer).flip();
            dest.write(buffer);
            buffer.compact();
        }

        ((Buffer) buffer).flip();

        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }

    static String getFileMimeType(String fileName) {

        String result;
        var dotPos = fileName.lastIndexOf(".");

        if (dotPos < 0) {
            result = MimeTypesUtil.CONTENT_TYPE_OCTETSTREAM;

        } else {
            var fileExt = fileName.substring(dotPos + 1);

            if (fileExt.isEmpty()) {
                result = MimeTypesUtil.CONTENT_TYPE_OCTETSTREAM;

            } else {
                result = MimeTypesUtil.guessMimeType(fileExt);
            }
        }

        if (MimeTypesUtil.CONTENT_TYPE_OCTETSTREAM.equals(result)) {
            result = FileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
        }

        return result != null ? result : MimeTypesUtil.CONTENT_TYPE_OCTETSTREAM;
    }

}
