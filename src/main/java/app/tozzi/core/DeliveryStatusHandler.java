package app.tozzi.core;

import app.tozzi.model.DeliveryStatus;
import app.tozzi.model.exception.MailParserException;
import app.tozzi.util.MailConstants;
import jakarta.mail.internet.MimePart;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Delivery Status management
 *
 * @author Biagio Tozzi
 */
public class DeliveryStatusHandler {

    /**
     * Extracts a {@link DeliveryStatus} object from {@link MimePart} part
     *
     * @param part
     * @return {@link DeliveryStatus} object with delivery status info
     */
    public static DeliveryStatus loadDeliveryStatus(MimePart part) {

        var deliveryStatus = new DeliveryStatus();

        try (var br = new BufferedReader(new InputStreamReader(part.getInputStream()))) {

            var line = br.readLine();

            while (line != null) {

                if (line.toLowerCase().startsWith(MailConstants.DELIVERY_ACTION.toLowerCase() + ":")) {
                    deliveryStatus.setAction(DeliveryStatus.Action.from(line.substring(MailConstants.DELIVERY_ACTION.length() + 1).trim()));

                } else if (line.toLowerCase().startsWith(MailConstants.DELIVERY_STATUS.toLowerCase() + ":")) {
                    deliveryStatus.setStatus(line.substring(MailConstants.DELIVERY_STATUS.length() + 1).trim());

                    if (!deliveryStatus.getStatus().isEmpty()) {
                        var prefix = Character.getNumericValue(deliveryStatus.getStatus().charAt(0));
                        if (prefix > 0) {
                            deliveryStatus.setStatusType(DeliveryStatus.StatusType.from(prefix));
                        }
                    }

                } else if (line.toLowerCase().startsWith(MailConstants.DELIVERY_DIAGNOSTIC_CODE.toLowerCase() + ":")) {
                    var diagnosticCode = line.substring(MailConstants.DELIVERY_DIAGNOSTIC_CODE.length() + 1).trim();
                    var dc = new DeliveryStatus.DiagnosticCode();
                    if (diagnosticCode.contains(";")) {
                        dc.setType(diagnosticCode.substring(0, diagnosticCode.indexOf(";")).trim());
                        dc.setDescription(diagnosticCode.substring(diagnosticCode.indexOf(";") + 1).trim());

                    } else {
                        dc.setDescription(diagnosticCode.trim());
                    }

                    deliveryStatus.setDiagnosticCode(dc);

                } else if (line.toLowerCase().startsWith(MailConstants.DELIVERY_REMOTE_MTA.toLowerCase() + ":")) {
                    var deliveryRemoteMTA = line.substring(MailConstants.DELIVERY_REMOTE_MTA.length() + 1).trim();
                    var drm = new DeliveryStatus.RemoteMTA();
                    if (deliveryRemoteMTA.contains(";")) {
                        drm.setType(deliveryRemoteMTA.substring(0, deliveryRemoteMTA.indexOf(";")).trim());
                        drm.setAddress(deliveryRemoteMTA.substring(deliveryRemoteMTA.indexOf(";") + 1).trim());
                    }
                    deliveryStatus.setRemoteMTA(drm);

                } else if (line.toLowerCase().startsWith(MailConstants.DELIVERY_REPORTING_MTA.toLowerCase() + ":")) {
                    var deliveryRemoteMTA = line.substring(MailConstants.DELIVERY_REPORTING_MTA.length() + 1).trim();
                    var drm = new DeliveryStatus.ReportingMTA();
                    if (deliveryRemoteMTA.contains(";")) {
                        drm.setType(deliveryRemoteMTA.substring(0, deliveryRemoteMTA.indexOf(";")).trim());
                        drm.setAddress(deliveryRemoteMTA.substring(deliveryRemoteMTA.indexOf(";") + 1).trim());
                    }
                    deliveryStatus.setReportingMTA(drm);

                } else if (line.toLowerCase().startsWith(MailConstants.DELIVERY_RECEIVED_FROM_MTA.toLowerCase() + ":")) {
                    var deliveryRemoteMTA = line.substring(MailConstants.DELIVERY_RECEIVED_FROM_MTA.length() + 1).trim();
                    var drm = new DeliveryStatus.ReceivedFromMTA();
                    if (deliveryRemoteMTA.contains(";")) {
                        drm.setType(deliveryRemoteMTA.substring(0, deliveryRemoteMTA.indexOf(";")).trim());
                        drm.setName(deliveryRemoteMTA.substring(deliveryRemoteMTA.indexOf(";") + 1).trim());
                    }
                    deliveryStatus.setReceivedFromMTA(drm);

                } else if (line.toLowerCase().startsWith(MailConstants.DELIVERY_FINAL_RECIPIENT.toLowerCase() + ":")) {
                    var finalRecipient = line.substring(MailConstants.DELIVERY_FINAL_RECIPIENT.length() + 1).trim();
                    var fr = new DeliveryStatus.FinalRecipient();
                    if (finalRecipient.contains(";")) {
                        fr.setType(finalRecipient.substring(0, finalRecipient.indexOf(";")).trim());
                        fr.setAddress(finalRecipient.substring(finalRecipient.indexOf(";") + 1).trim());
                    }
                    deliveryStatus.setFinalRecipient(fr);
                }

                line = br.readLine();
            }


        } catch (Exception e) {
            throw new MailParserException(e);
        }

        return deliveryStatus;
    }

}
