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
public enum TipoRicevuta {
	COMPLETA("completa"), BREVE("breve"), SINTETICA("sintetica");

	private final String descrizione;
	
	public static TipoRicevuta from(String descrizione) {
		return Stream.of(TipoRicevuta.values()).filter(t -> t.getDescrizione().equals(descrizione)).findAny().orElse(null);			
	}
}
