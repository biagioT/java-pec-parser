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

@Data
public class CertificateData {

    private PostaCertType type;
    private String sender;
    private List<PECRecipients> recipients;
    private String messageID;
    private String id;
    private PECError error;
    private String answers;
    private String subject;
    private String issuer;
    private String delivery;
    private String receiving;
    private String extendedError;
    private PECDate date;
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
