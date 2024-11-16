package app.tozzi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * datiCert.xml representation
 *
 * @author Biagio Tozzi
 */
@Data
public class CertificateData {

    /**
     * PEC type from {@value app.tozzi.util.PECConstants#DATICERT_POSTACERT_PATH} and {@value app.tozzi.util.PECConstants#DATICERT_IDENTIFICATIVO_TIPO_ATTRIBUTE}
     */
    private PostaCertType type;

    /**
     * Sender field from {@value app.tozzi.util.PECConstants#DATICERT_MITTENTE_PATH}
     */
    private String sender;

    /**
     * Recipients field from {@value app.tozzi.util.PECConstants#DATICERT_DESTINATARI_PATH} and {@value app.tozzi.util.PECConstants#DATICERT_DESTINATARI_TIPO_ATTRIBUTE}
     */
    private List<PECRecipients> recipients;

    /**
     * Message ID field from {@value app.tozzi.util.PECConstants#DATICERT_MESSAGE_ID_PATH}
     */
    private String messageID;

    /**
     * ID field from {@value app.tozzi.util.PECConstants#DATICERT_IDENTIFICATIVO_PATH}
     */
    private String id;

    /**
     * Error field from {@value app.tozzi.util.PECConstants#DATICERT_POSTACERT_PATH} and {@value app.tozzi.util.PECConstants#DATICERT_POSTACERT_ERRORE_ATTRIBUTE}
     */
    private PECError error;

    /**
     * Answers field from {@value app.tozzi.util.PECConstants#DATICERT_RISPOSTE_PATH}
     */
    private String answers;

    /**
     * Subject field from {@value app.tozzi.util.PECConstants#DATICERT_OGGETTO_PATH}
     */
    private String subject;

    /**
     * Issuer field from {@value app.tozzi.util.PECConstants#DATICERT_GESTORE_EMITTENTE_PATH}
     */
    private String issuer;

    /**
     * Delivery field from {@value app.tozzi.util.PECConstants#DATICERT_CONSEGNA_PATH}
     */
    private String delivery;

    /**
     * Receiving field from {@value app.tozzi.util.PECConstants#DATICERT_RICEZIONE_PATH}
     */
    private String receiving;

    /**
     * Extended error field from {@value app.tozzi.util.PECConstants#DATICERT_ERRORE_ESTESO_PATH}
     */
    private String extendedError;

    /**
     * Date field from {@value app.tozzi.util.PECConstants#DATICERT_DATA_PATH}, {@value app.tozzi.util.PECConstants#DATICERT_DATA_ZONA_ATTRIBUTE}, {@value app.tozzi.util.PECConstants#DATICERT_DATA_GIORNO_PATH} and {@value app.tozzi.util.PECConstants#DATICERT_DATA_ORA_PATH}
     */
    private PECDate date;

    /**
     * Receipt type field from {@value app.tozzi.util.PECConstants#DATICERT_RICEVUTA_PATH} and {@value app.tozzi.util.PECConstants#DATICERT_RICEVUTA_TIPO_ATTRIBUTE}
     */
    private ReceiptType receiptType;

    @Data
    @Builder
    public static class PECRecipients {

        private String address;
        private PECRecipientType type;

        @AllArgsConstructor
        @Getter
        public enum PECRecipientType {
            ESTERNO("esterno"), //
            CERTIFICATO("certificato"); //

            private final String description;

            public static PECRecipientType from(String description) {
                return Stream.of(PECRecipientType.values()).filter(t -> t.getDescription().equals(description)).findAny().orElse(null);
            }
        }
    }

    @AllArgsConstructor
    public enum PostaCertType {
        ACCETTAZIONE("accettazione"),
        NON_ACCETTAZIONE("non-accettazione"),
        PRESA_IN_CARICO("presa-in-carico"),
        AVVENUTA_CONSEGNA("avvenuta-consegna"),
        POSTA_CERTIFICATA("posta-certificata"),
        ERRORE("errore"),
        ERRORE_CONSEGNA("errore-consegna"),
        PREAVVISO_ERRORE_CONSEGNA("preavviso-errore-consegna"),
        RILEVAZIONE_VIRUS("rilevazione-virus");

        @Getter
        private final String description;

        public static PostaCertType from(String description) {
            return Stream.of(PostaCertType.values()).filter(t -> t.getDescription().equals(description)).findAny().orElse(null);
        }
    }

    @AllArgsConstructor
    @Getter
    public enum PECError {
        NESSUNO("nessuno"), //
        NO_DEST("no-dest"), //
        NO_DOMINIO("no-dominio"), //
        VIRUS("virus"), //
        ALTRO("altro"); //

        private final String description;

        public static PECError from(String description) {
            return Stream.of(PECError.values()).filter(t -> t.getDescription().equals(description)).findAny().orElse(null);
        }
    }

    @Data
    public static class PECDate {

        private static final String PEC_DATE_PATTERN_1 = "dd/MM/yyyy HH:mm:ss";
        private static final String PEC_DATE_PATTERN_2 = "dd/MM/yyyy";

        private String zone;
        private String day;
        private String hour;

        public Date getDate() {

            if (day != null) {
                return Date.from(ZonedDateTime.parse(this.day + (this.hour != null ? " " + this.hour : ""), DateTimeFormatter.ofPattern(this.hour != null ? PEC_DATE_PATTERN_1 : PEC_DATE_PATTERN_2).withZone(this.zone != null ? ZoneOffset.of(this.zone) : ZoneId.systemDefault())).toInstant());
            }

            return null;
        }
    }

    @AllArgsConstructor
    public enum ReceiptType {
        COMPLETA("completa"), //
        BREVE("breve"), //
        SINTETICA("sintetica"); //

        @Getter
        private final String description;

        public static ReceiptType from(String description) {
            return Stream.of(ReceiptType.values()).filter(t -> t.getDescription().equals(description)).findAny().orElse(null);
        }
    }
}
