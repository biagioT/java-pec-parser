package app.tozzi.mail.pec.model;

import javax.activation.DataSource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @author biagio.tozzi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper =  true)
public class Busta extends Mail {

	private DataSource datiCert;
	private DataSource postaCert;
	
	private String xTrasporto;
	private String xRicevuta;
	private String xRiferimentoMessageID;
	private String xTipoRicevuta;
	private String xVerificaSicurezza;
	
}
