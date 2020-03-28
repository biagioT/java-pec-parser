package it.tozzi.mail.pec.model;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author biagio.tozzi
 *
 */
@AllArgsConstructor
@Getter
public enum TipoPostaCert {
	ACCETTAZIONE("accettazione"), NON_ACCETTAZIONE("non-accettazione"), PRESA_IN_CARICO("presa-in-carico"), AVVENUTA_CONSEGNA("avvenuta-consegna"), 
	POSTA_CERTIFICATA("posta-certificata"), ERRORE_CONSEGNA("errore-consegna"), PREAVVISO_ERRORE_CONSEGNA("preavviso-errore-consegna"), RILEVAZIONE_VIRUS("rilevazione-virus");
	
	private final String descrizione;
	
	public static TipoPostaCert from(String descrizione) {
		return Stream.of(TipoPostaCert.values()).filter(t -> t.getDescrizione().equals(descrizione)).findAny().orElse(null);			
	}
}
