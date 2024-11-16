package app.tozzi.model;

import jakarta.activation.DataSource;
import lombok.Data;

/**
 * PEC
 *
 * @author Biagio Tozzi
 */
@Data
public class PEC implements ParsedEntity {

    private DataSource datiCert;
    private DataSource postaCert;
    private Mail envelope;
    private Mail originalMessage;
    private CertificateData certificateData;

    private String errorHeaderValue;
    private String transportHeaderValue;
    private String receiptHeaderValue;
    private String securityCheckHeaderValue;
    private String receiptTypeHeaderValue;
    private String referenceHeaderValue;

    @Override
    public ParsedEntityType getType() {
        return ParsedEntityType.PEC;
    }

}
