package it.tozzi.mail.pec.exception;

import lombok.Getter;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class PECParserException extends Exception {

	private static final long serialVersionUID = -6011391563073344576L;

	@Getter
	private String errore;
	
	public PECParserException(String errore, Exception e) {
		super(e);
		this.errore = errore;
	}
}
