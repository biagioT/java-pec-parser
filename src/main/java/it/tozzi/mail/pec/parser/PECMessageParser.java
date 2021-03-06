package it.tozzi.mail.pec.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import it.tozzi.mail.pec.exception.PECParserException;
import it.tozzi.mail.pec.model.Allegato;
import it.tozzi.mail.pec.model.Busta;
import it.tozzi.mail.pec.model.DataPEC;
import it.tozzi.mail.pec.model.DatiCertificazione;
import it.tozzi.mail.pec.model.DestinatarioPEC;
import it.tozzi.mail.pec.model.DestinatarioPEC.TipoDestinatario;
import it.tozzi.mail.pec.model.ErrorePEC;
import it.tozzi.mail.pec.model.Mail;
import it.tozzi.mail.pec.model.Messaggio;
import it.tozzi.mail.pec.model.PEC;
import it.tozzi.mail.pec.model.RicevutaPEC;
import it.tozzi.mail.pec.model.TipoPostaCert;
import it.tozzi.mail.pec.model.TipoRicevuta;
import it.tozzi.mail.pec.util.IOUtils;
import it.tozzi.mail.pec.util.MimeMessageUtils;
import it.tozzi.mail.pec.util.MimeTypesUtil;
import it.tozzi.mail.pec.util.PECConstants;
import it.tozzi.mail.pec.util.Tupla;
import it.tozzi.mail.pec.util.XMLDocumentUtils;
import it.tozzi.mail.uudecoder.UUDecoder;
import it.tozzi.mail.uudecoder.UUDecoder.UUDecodedAttachment;
import it.tozzi.mail.uudecoder.exception.UUDecoderException;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class PECMessageParser {

	private static final Logger logger = LoggerFactory.getLogger(PECMessageParser.class);
	private DocumentBuilder documentBuilder;
	private Properties properties;
	
	/**
	 * Istanza di PECMessageParser <br>
	 * L'elaborazione degli oggetti {@link MimeMessage} viene eseguita con le properties di sistema (System.getProperties())
	 * 
	 * @return PECMessageParser
	 * @throws PECParserException
	 */
	public static PECMessageParser getInstance() throws PECParserException {
		return new PECMessageParser(null);
	}

	/**
	 * Istanza di PECMessageParser <br>
	 * L'elaborazione degli oggetti {@link MimeMessage} viene eseguita con le properties passate come parametro
	 * 
	 * @param properties
	 * @return PECMessageParser
	 * @throws PECParserException
	 */
	public static PECMessageParser getInstance(Properties properties) throws PECParserException {
		return new PECMessageParser(properties);
	}
	
	private PECMessageParser(Properties properties) throws PECParserException {

		try {
			this.properties = properties;
			this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		} catch (ParserConfigurationException e) {
			logger.error("Errore durante l'inizializzazione del Document Builder", e);
			throw new PECParserException("Errore durante l'inizializzazione", e);
		}
	}

	/**
	 * Estrae, a partire da un {@link MimeMessage} rappresentante un messaggio PEC<i>*</i>: <br>
	 * <ul>
	 * <li> Busta di trasporto: {@link Busta} </li>
	 * <li> Eventuale messaggio di Posta Elettronica Certificata: {@link PEC} </li>
	 * <li> Eventuale ricevuta: {@link RicevutaPEC} </li>
	 * </ul>
	 * 
	 * <i>* Il messaggio viene elaborato anche se non PEC:
	 * <ul>
	 * <li> Messaggio ricevuto su una casella di Posta Elettronica Certificata: l'oggetto {@link PEC} conterrà le informazioni del messaggio normale in quanto comunque incapsulato in una busta</li>
	 * <li> Messaggio ricevuto su una casella di posta ordinaria: in questo caso la {@link Busta} rappresenta il messaggio normale</li>
	 * </ul>
	 * </i>
	 * 
	 * @param mimeMessage
	 * @return
	 * @throws PECParserException
	 * @throws IOException
	 */
	public Messaggio parse(MimeMessage mimeMessage) throws PECParserException, IOException {
		String xTrasporto = null;
		String xRicevuta = null;
		String xRiferimentoMessageID = null;
		String xTipoRicevuta = null;
		String xVerificaSicurezza = null;

		Messaggio messaggio = new Messaggio();
		Busta busta;

		try {
			xTrasporto = mimeMessage.getHeader(PECConstants.X_TRASPORTO, ",");
			xRicevuta = mimeMessage.getHeader(PECConstants.X_RICEVUTA, ",");
			xRiferimentoMessageID = mimeMessage.getHeader(PECConstants.X_RIFERIMENTO, ",");
			xTipoRicevuta = mimeMessage.getHeader(PECConstants.X_TIPO_RICEVUTA, ",");
			xVerificaSicurezza = mimeMessage.getHeader(PECConstants.X_VERIFICA_SICUREZZA, ",");
			
		} catch (MessagingException e) {
			logger.error("Errore durante la lettura degli header di trasporto", e);
			throw new PECParserException("Errore durante la lettura degli header di trasporto", e);
		}

		try {
			busta = estraiBusta(mimeMessage, xTrasporto != null || xRicevuta != null);
			busta.setXRicevuta(xRicevuta);
			busta.setXRiferimentoMessageID(xRiferimentoMessageID);
			busta.setXTrasporto(xTrasporto);
			busta.setXTipoRicevuta(xTipoRicevuta);
			busta.setXVerificaSicurezza(xVerificaSicurezza);

		} catch (PECParserException e) {
			logger.error("Errore durante l'elaborazione della busta");
			throw e;
		}

		messaggio.setBusta(busta);

		if (xTrasporto != null) {

			try {
				messaggio.setPec(estraiPEC(busta, xTrasporto));

			} catch (PECParserException | IOException e) {
				logger.error("Errore durante l'elaborazione della PEC");
				throw e;
			}

		} else if (xRicevuta != null) {

			try {
				messaggio.setRicevuta(estraiRicevuta(busta));

			} catch (PECParserException | IOException e) {
				logger.error("Errore durante l'elaborazione della ricevuta");
				throw e;
			}
		}

		return messaggio;
	}

	private static Busta estraiBusta(MimeMessage mimeMessage, boolean elaboraPEC) throws PECParserException {
		Busta busta = new Busta();
		Tupla<DataSource, DataSource> tupla = new Tupla<DataSource, DataSource>();
		parse(mimeMessage, busta, tupla, elaboraPEC);
		busta.setPostaCert(tupla.getElementA());
		busta.setDatiCert(tupla.getElementB());
		return busta;
	}

	private RicevutaPEC estraiRicevuta(Busta busta) throws PECParserException, IOException {
		RicevutaPEC ricevutaPEC = new RicevutaPEC();
		ricevutaPEC.setDatiCertificazione(estraiDatiCertificazione(busta.getDatiCert().getInputStream()));

		if (busta.getPostaCert() != null) {
			PEC pec = estraiPEC(MimeMessageUtils.createMimeMessage(busta.getPostaCert().getInputStream(), this.properties));
			ricevutaPEC.setMessaggioOriginale(pec);
		}

		return ricevutaPEC;
	}

	private PEC estraiPEC(Busta busta, String xTrasporto) throws PECParserException, IOException {
		MimeMessage postaCertMimeMessage = MimeMessageUtils.createMimeMessage(busta.getPostaCert().getInputStream(), this.properties);
		PEC pec = estraiPEC(postaCertMimeMessage);
		if (TipoPostaCert.POSTA_CERTIFICATA.getDescrizione().equals(xTrasporto))
			pec.setDatiCertificazione(estraiDatiCertificazione(busta.getDatiCert().getInputStream()));
		return pec;
	}

	private static PEC estraiPEC(MimeMessage postaCertMimeMessage) throws PECParserException {
		PEC pec = new PEC();
		parse(postaCertMimeMessage, pec, null, false);
		return pec;
	}

	private DatiCertificazione estraiDatiCertificazione(InputStream inputStream) throws PECParserException {
		Document datiCertDocument;
		try {
			datiCertDocument = documentBuilder.parse(inputStream);

		} catch (SAXException | IOException e) {
			logger.error("Errore durante il parsing del daticert.xml", e);
			throw new PECParserException("Errore durante il parsing del daticert.xml", e);
		}

		DatiCertificazione datiCertificazione = new DatiCertificazione();
		datiCertificazione
				.setMittente(XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_MITTENTE_PATH, false));
		Map<String, String> destinatari = XMLDocumentUtils.getTextAndAttribute(datiCertDocument,
				PECConstants.DATICERT_DESTINATARI_PATH, PECConstants.DATICERT_DESTINATARI_TIPO_ATTRIBUTE, false);
		destinatari.entrySet().stream().forEach(d -> datiCertificazione.getDestinatari()
				.add(new DestinatarioPEC(d.getKey(), TipoDestinatario.from(d.getValue()))));
		datiCertificazione
				.setRisposte(XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_RISPOSTE_PATH, false));
		datiCertificazione
				.setOggetto(XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_OGGETTO_PATH, true));
		datiCertificazione.setGestoreEmittente(
				XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_GESTORE_EMITTENTE_PATH, false));
		datiCertificazione.setData(
				new DataPEC(XMLDocumentUtils.getAttribute(datiCertDocument, PECConstants.DATICERT_DATA_PATH, PECConstants.DATICERT_DATA_ZONA_ATTRIBUTE, false),
						XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_DATA_GIORNO_PATH, false),
						XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_DATA_ORA_PATH, false)));
		datiCertificazione.setTipoRicevuta(TipoRicevuta
				.from(XMLDocumentUtils.getAttribute(datiCertDocument, PECConstants.DATICERT_RICEVUTA_PATH, PECConstants.DATICERT_RICEVUTA_TIPO_ATTRIBUTE, true)));
		datiCertificazione
				.setErrore(ErrorePEC.from(XMLDocumentUtils.getAttribute(datiCertDocument, PECConstants.DATICERT_POSTACERT_PATH, PECConstants.DATICERT_POSTACERT_ERRORE_ATTRIBUTE, false)));
		datiCertificazione
				.setErroreEsteso(XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_ERRORE_ESTESO_PATH, true));
		datiCertificazione
				.setConsegna(XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_CONSEGNA_PATH, true));
		datiCertificazione
				.setRicezione(XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_RICEZIONE_PATH, true));
		datiCertificazione.setMessageID(XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_MESSAGE_ID_PATH, true));
		datiCertificazione.setIdentificativo(
				XMLDocumentUtils.getTextContent(datiCertDocument, PECConstants.DATICERT_IDENTIFICATIVO_PATH, true));
		datiCertificazione
				.setTipo(TipoPostaCert.from(XMLDocumentUtils.getAttribute(datiCertDocument, PECConstants.DATICERT_POSTACERT_PATH, PECConstants.DATICERT_IDENTIFICATIVO_TIPO_ATTRIBUTE, false)));
		return datiCertificazione;
	}

	private static void parse(MimeMessage mimeMessage, Mail mail, Tupla<DataSource, DataSource> tupla,
			boolean elaboraPEC) throws PECParserException {

		try {
			Address[] mittenti = mimeMessage.getFrom();
			if (mittenti != null) {
				for (int i = 0; i < mittenti.length; i++) {
					if (mittenti[i] instanceof InternetAddress) {
						mail.getMittenti().add(((InternetAddress) mittenti[i]).getAddress());
					}
				}
			}

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione dei mittenti della mail", e);
			throw new PECParserException("Errore durante l'elaborazione dei mittenti della mail", e);
		}

		try {
			Address[] destinatari = mimeMessage.getRecipients(RecipientType.TO);
			if (destinatari != null) {
				for (int i = 0; i < destinatari.length; i++) {
					if (destinatari[i] instanceof InternetAddress) {
						mail.getDestinatari().add(((InternetAddress) destinatari[i]).getAddress());
					}
				}
			}

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione dei destinatari della mail", e);
			throw new PECParserException("Errore durante l'elaborazione dei destinatari della mail", e);
		}

		try {
			Address[] destinatari = mimeMessage.getRecipients(RecipientType.CC);
			if (destinatari != null) {
				for (int i = 0; i < destinatari.length; i++) {
					if (destinatari[i] instanceof InternetAddress) {
						mail.getDestinatariCopiaConoscenza().add(((InternetAddress) destinatari[i]).getAddress());
					}
				}
			}

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione dei destinatari in copia conoscenza della mail", e);
			throw new PECParserException("Errore durante l'elaborazione dei destinatari in copia conoscenza della mail",
					e);
		}

		try {
			Address[] destinatari = mimeMessage.getRecipients(RecipientType.BCC);
			if (destinatari != null) {
				for (int i = 0; i < destinatari.length; i++) {
					if (destinatari[i] instanceof InternetAddress) {
						mail.getDestinatariCopiaConoscenzaNascosta()
								.add(((InternetAddress) destinatari[i]).getAddress());
					}
				}
			}

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione dei destinatari in copia conoscenza nascosta della mail", e);
			throw new PECParserException(
					"Errore durante l'elaborazione dei destinatari in copia conoscenza nascosta della mail", e);
		}

		try {
			mail.setDataInvio(mimeMessage.getSentDate());

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione della data di invio della mail", e);
			throw new PECParserException("Errore durante l'elaborazione della data di invio della mail", e);
		}

		try {
			mail.setDataRicezione(mimeMessage.getReceivedDate());

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione della data di ricezione della mail", e);
			throw new PECParserException("Errore durante l'elaborazione della data di ricezione della mail", e);
		}

		try {
			String oggetto = mimeMessage.getSubject();

			if (oggetto != null)
				mail.setOggetto(MimeUtility.decodeText(oggetto));

		} catch (MessagingException | UnsupportedEncodingException e) {
			logger.error("Errore durante l'elaborazione dell'oggetto della mail", e);
			throw new PECParserException("Errore durante l'elaborazione dell'oggetto della mail", e);
		}

		try {
			Address[] destinatari = mimeMessage.getReplyTo();
			if (destinatari != null) {
				for (int i = 0; i < destinatari.length; i++) {
					if (destinatari[i] instanceof InternetAddress) {
						mail.getReplyTo().add(((InternetAddress) destinatari[i]).getAddress());
					}
				}
			}

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione dei destinatari replyTo della mail", e);
			throw new PECParserException("Errore durante l'elaborazione dei destinatari della mail", e);
		}

		try {
			mail.setMessageID(mimeMessage.getMessageID());

		} catch (MessagingException e) {
			logger.error("Errore durante l'elaborazione del messageID", e);
			throw new PECParserException("Errore durante l'elaborazione del messageID della mail", e);
		}

		parseContent(mimeMessage, mail, tupla, elaboraPEC);
	}

	private static void parseContent(MimePart part, Mail mail, Tupla<DataSource, DataSource> tupla, boolean elaboraPEC)
			throws PECParserException {

		Object content = MimeMessageUtils.getContent(part);

		if (MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_TEXT_PLAIN)
				&& !Part.ATTACHMENT.equalsIgnoreCase(MimeMessageUtils.getDisposition(part))
				&& mail.getCorpoTesto() == null) {

			String corpo = content.toString();

			try {
				if (UUDecoder.containsUUEncodedAttachments(content.toString())) {

					corpo = corpo.substring(0, UUDecoder.getNextBeginIndex(corpo));
					List<UUDecodedAttachment> decodedAttachments = null;
					
					try {
						decodedAttachments = UUDecoder.getUUDecodedAttachments(content.toString());
						
					} catch (UUDecoderException e) {
						logger.error("Errore durante l'estrazione degli allegati codificati in uuencoding", e);
						throw new PECParserException("Errore durante l'estrazione degli allegati codificati in uuencoding", e);
					}
					
					for (UUDecodedAttachment uda : decodedAttachments) {
						mail.getAllegati().add(new Allegato(uda.getFileName(), uda.getDataSource(), null, null, false));
					}
				}

			} catch (UUDecoderException e) {
				logger.error("Errore durante la verifica di allegati codificati in uuencoding", e);
				throw new PECParserException("Errore durante la verifica di allegati codificati in uuencoding", e);
			}

			mail.setCorpoTesto(MimeMessageUtils.decodeText(corpo));

		} else {

			if (MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_TEXT_HTML)
					&& !Part.ATTACHMENT.equalsIgnoreCase(MimeMessageUtils.getDisposition(part))) {

				if (mail.getCorpoHTML() == null) {
					mail.setCorpoHTML(MimeMessageUtils.decodeText(content.toString()));

				} else {
					mail.setCorpoHTML(mail.getCorpoHTML() + MimeMessageUtils.decodeText(content.toString()));
				}

			} else {

				if (MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_MULTIPART)) {
					Multipart multipart = (Multipart) content;

					for (int i = 0; i < MimeMessageUtils.getCount(multipart); i++) {
						parseContent(((MimeBodyPart) MimeMessageUtils.getBodyPart(multipart, i)), mail, tupla,
								elaboraPEC);
					}

				} else {

					boolean pec = false;

					if (elaboraPEC && tupla != null && !tupla.isComplete()) {
						if (MimeMessageUtils.getDisposition(part) != null
								&& MimeMessageUtils.getFileName(part) != null) {

							if (tupla.getElementA() == null && (PECConstants.POSTACERT_EML_NAME
									.equalsIgnoreCase(MimeMessageUtils.decodeText(MimeMessageUtils.getFileName(part)))
									|| PECConstants.POSTACERT_EML_NAME.equals(MimeMessageUtils.getFileName(part)))

									&& MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_MESSAGE_RFC822)) {

								pec = true;
								
								try {
									tupla.setElementA(IOUtils.createDataSource(part));
									
								} catch (PECParserException | IOException e) {
									logger.error("Errore durante l'estrazione del file {}", PECConstants.POSTACERT_EML_NAME, e);
									throw new PECParserException("Errore durante l'estrazione del file " + PECConstants.POSTACERT_EML_NAME, e);
								}

							} else if (tupla.getElementB() == null
									&& (PECConstants.DATICERT_XML_NAME
											.equalsIgnoreCase(MimeMessageUtils.decodeText(MimeMessageUtils.getFileName(part)))
											|| PECConstants.DATICERT_XML_NAME.equals(MimeMessageUtils.getFileName(part)))
									&& MimeMessageUtils.isMimeType(part, MimeTypesUtil.CONTENT_TYPE_APPLICATION_XML)) {

								pec = true;
								
								try {
									tupla.setElementB(IOUtils.createDataSource(part));
									
								} catch (PECParserException | IOException e) {
									logger.error("Errore durante l'estrazione del file {}", PECConstants.DATICERT_XML_NAME, e);
									throw new PECParserException("Errore durante l'estrazione del file " + PECConstants.DATICERT_XML_NAME, e);
								}
							}
						}
					}

					if (!pec) {
						DataSource dataSource;
						
						try {
							dataSource = IOUtils.createDataSource(part);
							
						} catch (PECParserException | IOException e) {
							logger.error("Errore durante l'estrazione dell'allegato {}", MimeMessageUtils.getFileName(part), e);
							throw new PECParserException("Errore durante l'estrazione dell'allegato " + MimeMessageUtils.getFileName(part), e);
						}
												
						mail.getAllegati().add(new Allegato(dataSource.getName(), dataSource, MimeMessageUtils.getHeaderValue(PECConstants.CONTENT_ID, part), 
								MimeMessageUtils.getHeaderValue(PECConstants.X_ATTACHMENT_ID, part), Part.INLINE.equals(MimeMessageUtils.getDisposition(part))));
					}
				}
			}
		}
	}

}
