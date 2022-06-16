package app.tozzi.mail.pec.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * @author biagio.tozzi
 *
 */
@Data
public class DatiCertificazione {
	
	private TipoPostaCert tipo;
	private String mittente;
	private List<DestinatarioPEC> destinatari = new ArrayList<>();
	private String messageID;
	private String identificativo; 
	private ErrorePEC errore;
	private String risposte;
	private String oggetto;
	private String gestoreEmittente;
	private String consegna;
	private String ricezione;
	private String erroreEsteso;
	private DataPEC data;
	private TipoRicevuta tipoRicevuta;
}



