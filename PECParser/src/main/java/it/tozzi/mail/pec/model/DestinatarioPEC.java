package it.tozzi.mail.pec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 
 * @author biagio.tozzi
 *
 */
@Data
@AllArgsConstructor
public class DestinatarioPEC {

	private String indirizzo;
	private TipoDestinatario tipo;
	
	@AllArgsConstructor
	@Getter
	public static enum TipoDestinatario {
		ESTERNO("esterno"), CERTIFICATO("certificato");
		
		private final String descrizione;
		
		public static TipoDestinatario from(String descrizione) {
			for (TipoDestinatario td : TipoDestinatario.values()) {
				if (td.getDescrizione().equals(descrizione))
					return td;
			}
			
			return null;
		}
	}
}
