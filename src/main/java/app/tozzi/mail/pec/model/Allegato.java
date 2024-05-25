package app.tozzi.mail.pec.model;

import jakarta.activation.DataSource;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author biagio.tozzi
 */
@Data
@AllArgsConstructor
public class Allegato {

    private String nome;
    private DataSource dataSource;
    private String attachmentContentId;
    private String xAttachmentId;
    private boolean embedded;

}
