package app.tozzi.util;

import app.tozzi.model.Attachment;
import app.tozzi.model.exception.MailParserException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UUEncoding/UUDecoding management
 *
 * @author Biagio Tozzi
 */
@Slf4j
public class UUEncodingUtils {

    /**
     * Checks if the content is encoded with uuencode encoding
     *
     * @param content content
     * @return true/false
     */
    public static boolean containsEncodedAttachments(String content) {

        if (content == null)
            return false;

        var beginIndex = content.indexOf("begin ");
        var endIndex = content.indexOf("end");

        if (beginIndex == -1 || endIndex == -1)
            return false;

        if (beginIndex > endIndex) {
            return containsEncodedAttachments(content.substring(beginIndex));
        }

        var subString = content.substring(beginIndex, endIndex + 3);

        if (!internalContainsEncodedAttachments(subString)) {
            return containsEncodedAttachments(content.substring(beginIndex + 6));
        }

        return true;
    }

    /**
     * Decode and extracts uuencoded attachments
     *
     * @param content content
     * @return attachments
     */
    public static List<Attachment> decodeAttachments(String content) {
        var result = new ArrayList<Attachment>();

        try {
            internalDecodeAttachments(content, result);

        } catch (IOException e) {
            throw new MailParserException(e);
        }

        return result;
    }

    /**
     * Calculates uuencoding begin index
     *
     * @param content content
     * @return index
     */
    public static int getNextBeginIndex(String content) {

        if (content == null)
            return -1;

        var beginIndex = content.indexOf("begin ");
        var endIndex = content.indexOf("end");

        if (beginIndex == -1 || endIndex == -1)
            return -1;

        if (beginIndex > endIndex) {
            return getNextBeginIndex(content.substring(endIndex + 3));
        }

        var subString = content.substring(beginIndex, endIndex + 3);

        if (internalContainsEncodedAttachments(subString)) {
            return beginIndex;
        }

        return getNextBeginIndex(content.substring(beginIndex + 6));
    }

    private static void internalDecodeAttachments(String content, List<Attachment> attachments) throws IOException {

        if (content == null)
            return;

        var beginIndex = content.indexOf("begin ");
        var endIndex = content.indexOf("end");

        if (beginIndex == -1 || endIndex == -1)
            return;

        if (beginIndex > endIndex) {
            internalDecodeAttachments(((content.substring(beginIndex))), attachments);

        } else {
            var subString = content.substring(beginIndex, endIndex + 3);

            if (internalContainsEncodedAttachments(subString)) {
                InputStream isDecoded = null;
                String fileName = null;

                try (var is = new ByteArrayInputStream(content.getBytes())) {
                    fileName = getAttachmentName(subString, attachments);
                    isDecoded = MimeMessageUtils.decodeStream(is, "uuencode");
                    attachments.add(Attachment.builder().name(fileName).dataSource(IOUtils.createDataSource(isDecoded, fileName)).build());
                }
            }

            internalDecodeAttachments(((content.substring(beginIndex + 6))), attachments);
        }
    }

    private static boolean internalContainsEncodedAttachments(String content) {

        try (var reader = new BufferedReader(new StringReader(content))) {
            var firstLine = reader.readLine();
            if (firstLine == null || !firstLine.regionMatches(false, 0, "begin ", 0, 6))
                return false;

            var lastLine = reader.lines().reduce((first, second) -> second).orElse(null);
            if (lastLine == null || !lastLine.regionMatches(false, 0, "end", 0, 3))
                return false;

            int mode;

            try {
                mode = Integer.parseInt(firstLine.substring(6, 9));

            } catch (NumberFormatException e) {
                log.warn("Permissions mode not valid", e);
                return false;
            }

            if (!isOctal(mode)) {
                log.warn("Permissions mode not in octal format");
                return false;
            }

            String fileName = firstLine.substring(9);
            if (fileName.isBlank()) {
                log.warn("File name not present");
                return false;
            }

            return true;

        } catch (Exception e) {
            log.error("Error during uuencoded content check", e);
            throw new MailParserException(e);
        }
    }

    private static String getAttachmentName(String content, List<Attachment> attachments) {

        try (var reader = new BufferedReader(new StringReader(content))) {
            return reader.readLine().substring(9).trim();

        } catch (Exception e) {
            log.error("Error during reading file name", e);
            return getVersionedFileName("unnamed", attachments.stream().map(Attachment::getName).toList(), 1);

        }
    }

    private static String getVersionedFileName(String fileName, List<String> files, int i) {

        if (files.contains(fileName)) {
            fileName = fileName + "(" + i + ")";
            return getVersionedFileName(fileName, files, i++);
        }

        return fileName;
    }

    private static boolean isOctal(int number) {
        var isOctal = false;

        while (number > 0) {
            if (number % 10 <= 7) {
                isOctal = true;
            } else {
                isOctal = false;
                break;
            }
            number /= 10;

        }

        return isOctal;
    }


}
