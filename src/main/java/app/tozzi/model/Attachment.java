package app.tozzi.model;

import jakarta.activation.DataSource;
import lombok.Builder;
import lombok.Data;

/**
 * Mail/PEC Attachment
 *
 * @author Biagio Tozzi
 */
@Data
@Builder
public class Attachment {

    private String name;
    private DataSource dataSource;
    private String contentID;
    private String xAttachmentID;
    private boolean inline;

}
