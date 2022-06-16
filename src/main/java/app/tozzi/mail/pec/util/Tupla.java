package app.tozzi.mail.pec.util;

import lombok.Data;

/**
 * 
 * @author biagio.tozzi
 *
 */
@Data
public class Tupla<A, B> {
	
	private A elementA;
	private B elementB;

	public boolean isComplete() {
		return this.elementA != null && this.elementB != null;
	}
}
