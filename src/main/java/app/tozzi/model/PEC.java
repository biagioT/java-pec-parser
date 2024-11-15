package app.tozzi.model;

import jakarta.activation.DataSource;
import lombok.Data;

@Data
public class PEC implements ParsedEntity {

    private DataSource datiCert;
    private DataSource postaCert;
    private Mail envelope;
    private Mail originalMessage;
    private CertificateData certificateData;

    @Override
    public ParsedEntityType getType() {
        return ParsedEntityType.PEC;
    }

}
