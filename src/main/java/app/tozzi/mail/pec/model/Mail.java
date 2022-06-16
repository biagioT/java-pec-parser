package app.tozzi.mail.pec.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

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

}
