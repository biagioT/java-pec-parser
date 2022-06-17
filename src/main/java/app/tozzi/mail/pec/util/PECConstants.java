package app.tozzi.mail.pec.util;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class PECConstants {
	
	public static final String X_TRASPORTO = "X-Trasporto";
	public static final String X_RICEVUTA = "X-Ricevuta";
	public static final String X_RIFERIMENTO = "X-Riferimento-Message-ID";
	public static final String X_TIPO_RICEVUTA = "X-TipoRicevuta";
	public static final String X_VERIFICA_SICUREZZA = "X-VerificaSicurezza";
	public static final String POSTACERT_EML_NAME = "postacert.eml";
	public static final String DATICERT_XML_NAME = "daticert.xml";
	public static final String X_TRASPORTO_ERRORE = "errore";
	
	public static final String X_ATTACHMENT_ID = "X-Attachment-Id";
	public static final String CONTENT_ID = "Content-ID";
	
	public static final String IN_REPLY_TO = "In-Reply-To";
	public static final String REFERENCES = "References";
	
	public static final String DELIVERY_ACTION = "action";
	public static final String DELIVERY_STATUS = "status";
	public static final String DELIVERY_DIAGNOSTIC_CODE = "Diagnostic-Code";
	public static final String DELIVERY_REMOTE_MTA = "Remote-MTA";
	public static final String DELIVERY_REPORTING_MTA = "Reporting-MTA";
	public static final String DELIVERY_RECEIVED_FROM_MTA = "Received-From-MTA";
	public static final String DELIVERY_FINAL_RECIPIENT = "Final-Recipient";
	
	public static final String DATICERT_MITTENTE_PATH = "/postacert/intestazione/mittente";
	public static final String DATICERT_DESTINATARI_PATH = "/postacert/intestazione/destinatari";
	public static final String DATICERT_DESTINATARI_TIPO_ATTRIBUTE = "tipo";
	public static final String DATICERT_RISPOSTE_PATH = "/postacert/intestazione/risposte";
	public static final String DATICERT_OGGETTO_PATH = "/postacert/intestazione/oggetto";
	public static final String DATICERT_GESTORE_EMITTENTE_PATH = "/postacert/dati/gestore-emittente";
	public static final String DATICERT_DATA_PATH = "/postacert/dati/data";
	public static final String DATICERT_DATA_ZONA_ATTRIBUTE = "zona";
	public static final String DATICERT_DATA_GIORNO_PATH = "/postacert/dati/data/giorno";
	public static final String DATICERT_DATA_ORA_PATH = "/postacert/dati/data/ora";
	public static final String DATICERT_RICEVUTA_PATH = "/postacert/dati/ricevuta";
	public static final String DATICERT_RICEVUTA_TIPO_ATTRIBUTE = "tipo";
	public static final String DATICERT_POSTACERT_PATH = "/postacert";
	public static final String DATICERT_POSTACERT_ERRORE_ATTRIBUTE = "errore";
	public static final String DATICERT_ERRORE_ESTESO_PATH = "/postacert/dati/errore-esteso";
	public static final String DATICERT_CONSEGNA_PATH = "/postacert/dati/consegna";
	public static final String DATICERT_RICEZIONE_PATH = "/postacert/dati/ricezione";
	public static final String DATICERT_MESSAGE_ID_PATH = "/postacert/dati/msgid";
	public static final String DATICERT_IDENTIFICATIVO_PATH = "/postacert/dati/identificativo";
	public static final String DATICERT_IDENTIFICATIVO_TIPO_ATTRIBUTE = "tipo";
}
