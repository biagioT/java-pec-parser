package app.tozzi.mail.pec.model;

import lombok.Data;

/**
 * 
 * @author biagio.tozzi
 *
 */
@Data
public class Messaggio {
	
	private Busta busta;
	private PEC pec;
	private RicevutaPEC ricevuta;
	
}
