package it.tozzi.mail.pec.model;

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
public class PEC extends Mail {
		
	private DatiCertificazione datiCertificazione;	
	
}
