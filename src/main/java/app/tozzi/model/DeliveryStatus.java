package app.tozzi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * Delivery Status info
 *
 * @author Biagio Tozzi
 */
@Data
public class DeliveryStatus {

    private String status;
    private DiagnosticCode diagnosticCode;
    private Action action;
    private ReportingMTA reportingMTA;
    private ReceivedFromMTA receivedFromMTA;
    private RemoteMTA remoteMTA;
    private FinalRecipient finalRecipient;
    private StatusType statusType;

    @Data
    public static class ReceivedFromMTA {
        private String type;
        private String name;
    }

    @Data
    public static class ReportingMTA {
        private String type;
        private String address;
    }

    @Data
    public static class RemoteMTA {
        private String type;
        private String address;
    }

    @Data
    public static class FinalRecipient {
        private String type;
        private String address;
    }

    @Data
    public static class DiagnosticCode {
        private String type;
        private String description;
    }

    @AllArgsConstructor
    public enum Action {
        FAILED("failed"), FAILURE("failure"), DELAYED("delayed"), DELIVERED("delivered"), RELAYED("relayed"), EXPANDED("expanded"), UNKNOWN("unknown");

        @Getter
        private final String action;

        public static Action from(String action) {
            return Stream.of(Action.values()).filter(t -> t.getAction().equals(action)).findAny().orElse(null);
        }
    }

    @AllArgsConstructor
    public enum StatusType {
        INFO(2), TEMP_FAILURE(4), PERM_FAILURE(5);

        @Getter
        private final int prefix;

        public static StatusType from(int prefix) {
            return Stream.of(StatusType.values()).filter(t -> t.getPrefix() == prefix).findAny().orElse(null);
        }
    }
}
