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
import java.util.HashMap;
import java.util.Map;

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

	public static Map<String, String> mimeTypesMap = new HashMap<String, String>();
	
	static {
		// Application
		mimeTypesMap.put("application/envoy", "evy");
		mimeTypesMap.put("application/fractals", "fif");
		mimeTypesMap.put("application/futuresplash", "spl");
		mimeTypesMap.put("application/hta", "hta");
		mimeTypesMap.put("application/internet-property-stream", "acx");
		mimeTypesMap.put("application/mac-binhex40", "hqx");
		mimeTypesMap.put("application/msword", "doc");
		mimeTypesMap.put("application/msword", "dot");
		mimeTypesMap.put("application/octet-stream", "bin"); // XXX *; class; dms; exe; lha; lzh
		mimeTypesMap.put("application/oda", "oda");
		mimeTypesMap.put("application/olescript", "axs");
		mimeTypesMap.put("application/pdf", "pdf");
		mimeTypesMap.put("application/pics-rules", "prf");
		mimeTypesMap.put("application/pkcs10", "p10");
		mimeTypesMap.put("application/pkix-crl", "crl");
		mimeTypesMap.put("application/postscript", "ps");// XXX eps; ai
		mimeTypesMap.put("application/rtf", "rtf");
		mimeTypesMap.put("application/set-payment-initiation", "setpay");
		mimeTypesMap.put("application/set-registration-initiation", "setreg");
		mimeTypesMap.put("application/vnd.ms-excel", "xls"); // XXX xla; xlc; xlm, xlt, xlw
		mimeTypesMap.put("application/vnd.ms-outlook", "msg");
		mimeTypesMap.put("application/vnd.ms-pkicertstore", "sst");
		mimeTypesMap.put("application/vnd.ms-pkiseccat", "cat");
		mimeTypesMap.put("application/vnd.ms-pkistl", "stl");
		mimeTypesMap.put("application/vnd.ms-powerpoint", "ppt"); // XXX pps; pot
		mimeTypesMap.put("application/vnd.ms-project", "mpp");
		mimeTypesMap.put("application/vnd.ms-works", "wps"); // XXX wks; wdb; wcm
		mimeTypesMap.put("application/winhlp", "hlp");
		mimeTypesMap.put("application/x-bcpio", "bcpio");
		mimeTypesMap.put("application/x-cdf", "cdf");
		mimeTypesMap.put("application/x-compress", "z");
		mimeTypesMap.put("application/x-compressed", "tgz");
		mimeTypesMap.put("application/x-cpio", "cpio");
		mimeTypesMap.put("application/x-csh", "csh");
		mimeTypesMap.put("application/x-director", "dir"); // XXX: dcr; dxr
		mimeTypesMap.put("application/x-dvi", "dvi");
		mimeTypesMap.put("application/x-gtar", "gtar");
		mimeTypesMap.put("application/x-gzip", "gz");
		mimeTypesMap.put("application/x-hdf", "hdf");
		mimeTypesMap.put("application/x-internet-signup", "ins"); // XXX isp
		mimeTypesMap.put("application/x-iphone", "iii");
		mimeTypesMap.put("application/x-javascript", "js");
		mimeTypesMap.put("application/x-latex", "latex");
		mimeTypesMap.put("application/x-msaccess", "mdb");
		mimeTypesMap.put("application/x-mscardfile", "crd");
		mimeTypesMap.put("application/x-msclip", "clp");
		mimeTypesMap.put("application/x-msdownload", "dll");
		mimeTypesMap.put("application/x-msmediaview", "mvb"); // XXX m13; m14
		mimeTypesMap.put("application/x-msmetafile", "wmf");
		mimeTypesMap.put("application/x-msmoney", "mny");
		mimeTypesMap.put("application/x-mspublisher", "pub");
		mimeTypesMap.put("application/x-msschedule", "scd");
		mimeTypesMap.put("application/x-msterminal", "trm");
		mimeTypesMap.put("application/x-mswrite", "wri");
		mimeTypesMap.put("application/x-netcdf", "cdf"); // XXX nc
		mimeTypesMap.put("application/x-perfmon", "pma"); // XXX pmc; pml; pmr; pmw
		mimeTypesMap.put("application/x-pkcs12", "p12");
		mimeTypesMap.put("application/x-pkcs12", "pfx");
		mimeTypesMap.put("application/x-pkcs7-certificates", "p7b"); // XXX spc
		mimeTypesMap.put("application/x-pkcs7-certreqresp", "p7r");
		mimeTypesMap.put("application/x-pkcs7-mime", "p7m"); // XXX p7c
		mimeTypesMap.put("application/x-pkcs7-signature", "p7s");
		mimeTypesMap.put("application/x-sh", "sh");
		mimeTypesMap.put("application/x-shar", "shar");
		mimeTypesMap.put("application/x-shockwave-flash", "swf");
		mimeTypesMap.put("application/x-stuffit", "sit");
		mimeTypesMap.put("application/x-sv4cpio", "sv4cpio");
		mimeTypesMap.put("application/x-sv4crc", "sv4crc");
		mimeTypesMap.put("application/x-tar", "tar");
		mimeTypesMap.put("application/x-tcl", "tcl");
		mimeTypesMap.put("application/x-tex", "tex");
		mimeTypesMap.put("application/x-texinfo", "texinfo"); // XXX texi
		mimeTypesMap.put("application/x-troff", "roff"); // XXX t; tr
		mimeTypesMap.put("application/x-troff-man", "man");
		mimeTypesMap.put("application/x-troff-me", "me");
		mimeTypesMap.put("application/x-troff-ms", "ms");
		mimeTypesMap.put("application/x-ustar", "ustar");
		mimeTypesMap.put("application/x-wais-source", "src");
		mimeTypesMap.put("application/x-x509-ca-cert", "crt"); // XXX cer; der
		mimeTypesMap.put("application/ynd.ms-pkipko", "pko");
		mimeTypesMap.put("application/zip", "zip");
		// Audio
		mimeTypesMap.put("audio/basic", "au"); // XXX snd
		mimeTypesMap.put("audio/mid", "mid"); // XXX rmi
		mimeTypesMap.put("audio/mpeg", "mp3");
		mimeTypesMap.put("audio/x-aiff", "aif"); // XXX aifc, aiff
		mimeTypesMap.put("audio/x-mpegurl", "m3u");
		mimeTypesMap.put("audio/x-pn-realaudio", "ra"); // XXX ram
		mimeTypesMap.put("audio/x-wav", "wav");
		// Image
		mimeTypesMap.put("image/bmp", "bmp");
		mimeTypesMap.put("image/cis-cod", "cod");
		mimeTypesMap.put("image/gif", "gif");
		mimeTypesMap.put("image/ief", "ief");
		mimeTypesMap.put("image/jpeg", "jpeg"); // XXX jpe; jpg
		mimeTypesMap.put("image/pipeg", "jfif");
		mimeTypesMap.put("image/svg+xml", "svg");
		mimeTypesMap.put("image/tiff", "tif"); // XXX tiff
		mimeTypesMap.put("image/x-cmu-raster", "ras");
		mimeTypesMap.put("image/x-cmx", "cmx");
		mimeTypesMap.put("image/x-icon", "ico");
		mimeTypesMap.put("image/x-portable-anymap", "pnm");
		mimeTypesMap.put("image/x-portable-bitmap", "pbm");
		mimeTypesMap.put("image/x-portable-graymap", "pgm");
		mimeTypesMap.put("image/x-portable-pixmap", "ppm");
		mimeTypesMap.put("image/x-rgb", "rgb");
		mimeTypesMap.put("image/x-xbitmap", "xbm"); 
		mimeTypesMap.put("image/x-xpixmap", "xpm");
		mimeTypesMap.put("image/x-xwindowdump", "xwd");
		// Text
		mimeTypesMap.put("text/css", "css");
		mimeTypesMap.put("text/h323", "323");
		mimeTypesMap.put("text/html", "html"); // XXX htm, stm
		mimeTypesMap.put("text/iuls", "uls");
		mimeTypesMap.put("text/plain", "txt"); // XXX h, c, bas
		mimeTypesMap.put("text/richtext", "rtx");
		mimeTypesMap.put("text/scriptlet", "sct");
		mimeTypesMap.put("text/tab-separated-values", "tsv");
		mimeTypesMap.put("text/webviewhtml", "htt");
		mimeTypesMap.put("text/x-component", "htc");
		mimeTypesMap.put("text/x-setext", "etx");
		mimeTypesMap.put("text/x-vcard", "vcf");
		// Video
		mimeTypesMap.put("video/mpeg", "mpeg"); // XXX mp2, mpa, mpe, mpg, mpv2
		mimeTypesMap.put("video/mp4", "mp4");
		mimeTypesMap.put("video/quicktime", "mov"); // XXX qt
 		mimeTypesMap.put("video/x-la-asf", "lsf"); // XXX lsx
		mimeTypesMap.put("video/x-ms-asf", "asf"); // XXX asr, asx
		mimeTypesMap.put("video/x-msvideo", "avi");
		mimeTypesMap.put("video/x-sgi-movie", "movie");
		// Virtual World
		mimeTypesMap.put("x-world/x-vrml", "vrml"); // XXX flr, wrl, wrz, xaf, xof
	}
	
	public static final String CONTENT_TYPE_OCETSTREAM = "application/octet-stream";
	public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
	public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
	public static final String CONTENT_TYPE_MESSAGE_RFC822 = "message/rfc822";
	public static final String CONTENT_TYPE_APPLICATION_XML = "application/xml";
	public static final String CONTENT_TYPE_MULTIPART = "multipart/*";

	private static final String UNNAMED_FILE_NAME = "PEC_MESSAGE_PARSER_unnamed_file";
	private static final String UNNAMED_EMBEDDED_FILE_NAME = "PEC_MESSAGE_PARSER_embedded_unnamed_file";

	public static DataSource createDataSource(MimePart part) throws IOException, PECParserException {
		DataHandler dataHandler = MimeMessageUtils.getDataHandler(part);		
		DataSource dataSource = dataHandler.getDataSource();
		String fileName = MimeMessageUtils.getFileName(part);
		fileName = fileName != null && !fileName.isBlank() ? MimeMessageUtils.decodeText(fileName) : getName(part);
		String contentType = getBaseMimeType(dataSource, fileName);
		byte[] content = getContent(dataSource.getInputStream());
		ByteArrayDataSource result = new ByteArrayDataSource(content, contentType);
		result.setName(fileName);
		return result;
	}

	private static String getBaseMimeType(DataSource dataSource, String fileName) {
		String fullMimeType = dataSource.getContentType();

		if (fullMimeType == null || fullMimeType.isBlank()) {
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
			result = CONTENT_TYPE_OCETSTREAM;
			
		} else {
			String fileExt = fileName.substring(dotPos + 1);
			
			if (fileExt.length() == 0) {
				result = CONTENT_TYPE_OCETSTREAM;
				
			} else {
				result = mimeTypesMap.entrySet().stream().filter(e -> e.getValue().equals(fileExt)).findAny().map(e -> e.getKey()).orElse(CONTENT_TYPE_OCETSTREAM);
			}
		}
		
		if (CONTENT_TYPE_OCETSTREAM.equals(result)) {
			result = FileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
		}
		
		return result != null ? result : CONTENT_TYPE_OCETSTREAM;
	}

	private static String getName(Part part) throws UnsupportedEncodingException, PECParserException {

		String fileName = MimeMessageUtils.getFileName(part);

		if (fileName != null && !fileName.isBlank())
			return MimeMessageUtils.decodeText(fileName);

		String extension = getExtension(part);
		extension = extension != null && !extension.isBlank() ? ("." + extension) : "";
		
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

	private static void fastCopy(final InputStream src, final OutputStream dest) throws IOException {
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
		
		if (fullMimeType != null && !fullMimeType.isBlank()) {
			return mimeTypesMap.entrySet().stream().filter(e -> fullMimeType.contains(e.getValue())).findAny().map(e -> e.getValue()).orElse(null);
		}

		return null;
	}
}
