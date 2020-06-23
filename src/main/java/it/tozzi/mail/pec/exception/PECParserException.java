package it.tozzi.mail.pec.exception;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class PECParserException extends Exception {

	private static final long serialVersionUID = -6011391563073344576L;

	public PECParserException(String errore, Exception e) {
		super(errore, e);
	}
}
