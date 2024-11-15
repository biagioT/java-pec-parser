package app.tozzi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PECReceipt implements ParsedEntity {

    private PEC pec;
    private CertificateData certificateData;

    @Override
    public ParsedEntityType getType() {
        return ParsedEntityType.PEC_RECEIPT;
    }
}
