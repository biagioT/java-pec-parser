package app.tozzi.mail.pec.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 
 * @author biagio.tozzi
 *
 */
@Data
public abstract class Mail {
	
	private List<String> mittenti = new ArrayList<>();
	private String oggetto;
	private List<String> destinatari = new ArrayList<>();
	private List<String> destinatariCopiaConoscenza = new ArrayList<>();
	private List<String> destinatariCopiaConoscenzaNascosta = new ArrayList<>();
	private Date dataInvio;
	private Date dataRicezione;
	private String messageID;
	
	private String corpoTesto;
	private String corpoHTML;
	private List<Allegato> allegati = new ArrayList<>();
	
	private String replyToMessageID;
	private List<String> replyToHistoryMessagesID = new ArrayList<>();
	
	private boolean deliveryStatus = false;
	private DeliveryStatus deliveryStatusInfo;
	
	@Data
	public static class DeliveryStatus {
		
		private String status;
		private DiagnosticCode diagnosticCode;
		private Action action;
		private String reportingMTA;
		private String receivedFromMTA;
		private String remoteMTA;
		private String finalRecipient;
		
		private TipoStato tipoStato;

		
		@Data
		public static class DiagnosticCode {
			private String type;
			private String description;
		}
		
		@AllArgsConstructor
		public static enum TipoStato {
			INFORMAZIONE(2), FALLIMENTO_TEMPORANEO(4), FALLIMENTO_PERMANENTE(5);
			
			@Getter
			private int prefissoStato;
			
			public static TipoStato from(int prefissoStato) {
				return Stream.of(TipoStato.values()).filter(t -> t.getPrefissoStato() == prefissoStato).findAny().orElse(null);			
			}
		}
		
		@AllArgsConstructor
		public static enum Action {
			FAILED("failed"), FAILURE("failure"), DELAYED("delayed"), DELIVERED("delivered"), RELAYED("relayed"), EXPANDED("expanded"), UNKNOWN("unknown");
			
			@Getter
			private String action;
			
			public static Action from(String action) {
				return Stream.of(Action.values()).filter(t -> t.getAction().equals(action)).findAny().orElse(null);			
			}
		}
		
		
	}

}
