package it.tozzi.mail.pec.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author biagio.tozzi
 *
 */
@AllArgsConstructor
@Getter
public enum TipoRicevuta {
	COMPLETA("completa"), BREVE("breve"), SINTETICA("sintetica");

	private final String descrizione;
	
	public static TipoRicevuta from(String descrizione) {
		for (TipoRicevuta tr : TipoRicevuta.values()) {
			if (tr.getDescrizione().equals(descrizione))
				return tr;
		}
		
		return null;
	}
}
