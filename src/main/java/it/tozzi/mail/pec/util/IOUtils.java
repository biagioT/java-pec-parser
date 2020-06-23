package it.tozzi.mail.pec.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.Part;
import javax.mail.internet.MimePart;
import javax.mail.util.ByteArrayDataSource;

import it.tozzi.mail.pec.exception.PECParserException;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class IOUtils {
	
	private static final String UNNAMED_FILE_NAME = "unnamed_file";
	private static final String UNNAMED_EMBEDDED_FILE_NAME = "embedded_unnamed_file";

	public static DataSource createDataSource(MimePart part) throws IOException, PECParserException {
		DataHandler dataHandler = MimeMessageUtils.getDataHandler(part);		
		DataSource dataSource = dataHandler.getDataSource();
		String fileName = MimeMessageUtils.getFileName(part);
		fileName = fileName != null && !fileName.trim().isEmpty() ? MimeMessageUtils.decodeText(fileName) : getName(part);
		String contentType = getBaseMimeType(dataSource, fileName);
		byte[] content = getContent(dataSource.getInputStream());
		ByteArrayDataSource result = new ByteArrayDataSource(content, contentType);
		result.setName(fileName);
		return result;
	}

	private static String getBaseMimeType(DataSource dataSource, String fileName) {
		String fullMimeType = dataSource.getContentType();

		if (fullMimeType == null || fullMimeType.trim().isEmpty()) {
			return getFileMimeType(fileName);
		}

		final int pos = fullMimeType.indexOf(';');
		if (pos >= 0) {
			return fullMimeType.substring(0, pos);
		}
		return fullMimeType;
	}
	
	private static String getFileMimeType(String fileName) {
		
		String result = null;
		int dotPos = fileName.lastIndexOf(".");
		
		if (dotPos < 0) {
			result = MimeTypesUtil.CONTENT_TYPE_OCETSTREAM;
			
		} else {
			String fileExt = fileName.substring(dotPos + 1);
			
			if (fileExt.length() == 0) {
				result = MimeTypesUtil.CONTENT_TYPE_OCETSTREAM;
				
			} else {
				result = MimeTypesUtil.guessMimeType(fileExt);
			}
		}
		
		if (MimeTypesUtil.CONTENT_TYPE_OCETSTREAM.equals(result)) {
			result = FileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
		}
		
		return result != null ? result : MimeTypesUtil.CONTENT_TYPE_OCETSTREAM;
	}

	private static String getName(Part part) throws UnsupportedEncodingException, PECParserException {

		String fileName = MimeMessageUtils.getFileName(part);

		if (fileName != null && !fileName.trim().isEmpty())
			return MimeMessageUtils.decodeText(fileName);

		String extension = getExtension(part);
		extension = extension != null && !extension.trim().isEmpty() ? ("." + extension) : "";
		
		if (Part.INLINE.equals(MimeMessageUtils.getDisposition(part))) {
			return UNNAMED_EMBEDDED_FILE_NAME + extension;
		}

		return UNNAMED_FILE_NAME + extension;
	}

	private static byte[] getContent(final InputStream is) throws IOException {
		byte[] result;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		fastCopy(is, os);
		result = os.toByteArray();
		return result;
	}

	public static void fastCopy(final InputStream src, final OutputStream dest) throws IOException {
		final ReadableByteChannel inputChannel = Channels.newChannel(src);
		final WritableByteChannel outputChannel = Channels.newChannel(dest);
		fastCopy(inputChannel, outputChannel);
		inputChannel.close();
		outputChannel.close();
	}

	private static void fastCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

		while (src.read(buffer) != -1) {
			buffer.flip();
			dest.write(buffer);
			buffer.compact();
		}

		buffer.flip();

		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}
	
	private static String getExtension(Part part) {

		String fullMimeType;

		try {
			fullMimeType = MimeMessageUtils.getContentType(part);

		} catch (PECParserException e) {
			return null;
		}
		
		if (fullMimeType != null && !fullMimeType.trim().isEmpty()) {
			return MimeTypesUtil.guessExtension(fullMimeType);
		}

		return null;
	}
}
