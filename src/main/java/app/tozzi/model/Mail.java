package app.tozzi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(of = "messageID")
@NoArgsConstructor
public class Mail implements ParsedEntity {

    private String subject;
    private List<Address> from;
    private List<Address> to;
    private List<Address> cc;
    private List<Address> bcc;
    private String messageID;
    private String bodyHTML;
    private String bodyTXT;
    private Date sentDate;
    private Date receivedDate;

    private boolean hasDeliveryStatus;
    private DeliveryStatus deliveryStatus;

    private List<Attachment> attachments = new ArrayList<>();

    private String replyToMessageID;
    private List<String> replyToHistoryMessagesID;

    private List<Header> headers;

    @Override
    public ParsedEntityType getType() {
        return ParsedEntityType.MAIL;
    }
}
