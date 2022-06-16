package app.tozzi.mail.pec.model;

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
public enum ErrorePEC {
	NESSUNO("nessuno"), //
	NO_DEST("no-dest"), //
	NO_DOMINIO("no-dominio"), //
	VIRUS("virus"), //
	ALTRO("altro"); //
	
	private final String descrizione;
	
	public static ErrorePEC from(String descrizione) {
		return Stream.of(ErrorePEC.values()).filter(t -> t.getDescrizione().equals(descrizione)).findAny().orElse(null);			
	}
}
