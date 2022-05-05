package it.tozzi.mail.pec.util;

import it.tozzi.mail.pec.model.Messaggio;
import it.tozzi.mail.pec.model.TipoPostaCert;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class MessageUtils {

	public static boolean isPEC(Messaggio messaggio) {
		return messaggio.getBusta() != null
				? TipoPostaCert.POSTA_CERTIFICATA.getDescrizione().equals(messaggio.getBusta().getXTrasporto())
				: false;
	}

	public static boolean isRicevutaPEC(Messaggio messaggio) {
		return messaggio.getBusta() != null ? messaggio.getBusta().getXRicevuta() != null : false;
	}

	public static boolean isEmailNormale(Messaggio messaggio) {
		return messaggio.getBusta() != null
				? (messaggio.getBusta().getXTrasporto() == null
						|| PECConstants.X_TRASPORTO_ERRORE.equals(messaggio.getBusta().getXTrasporto()))
						&& messaggio.getBusta().getXRicevuta() == null
						&& messaggio.getBusta().getXRiferimentoMessageID() == null
						&& messaggio.getBusta().getXTipoRicevuta() == null
						&& messaggio.getBusta().getXVerificaSicurezza() == null
				: false;
	}

	public static String getMessageID(Messaggio messaggio) {

		if (isPEC(messaggio)) {
			return messaggio.getPec().getMessageID();

		} else if (isRicevutaPEC(messaggio)) {
			return messaggio.getRicevuta().getDatiCertificazione().getMessageID();

		} else if (isEmailNormale(messaggio)) {
			return messaggio.getBusta().getMessageID();
		}
		
		throw new IllegalArgumentException("Tipo email non valida");
	}

}
