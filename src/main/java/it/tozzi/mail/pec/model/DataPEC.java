package it.tozzi.mail.pec.model;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author biagio.tozzi
 *
 */
@Data
@AllArgsConstructor
public class DataPEC {
	
	private static final String DATA_PEC_PATTERN = "dd/MM/yyyy HH:mm:ss";

	private String zona;
	private String giorno;
	private String ora;
	
	public Date getDate() {
		return Date.from(ZonedDateTime.parse(this.giorno + " " + this.ora, DateTimeFormatter.ofPattern(DATA_PEC_PATTERN).withZone(ZoneOffset.of(this.zona))).toInstant());
	}
}
