[![Codacy Badge](https://app.codacy.com/project/badge/Grade/dbe9e634249f4850a22d9d8dcabee03e)](https://www.codacy.com/manual/biagioT/java-pec-parser?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=biagioT/java-pec-parser&amp;utm_campaign=Badge_Grade)

# PEC/Mail Parser
Utility per l'elaborazione di messaggi di Posta Elettronica Certificata (e messaggi di posta ordinaria)

## Specifiche
Regole tecniche del servizio di trasmissione di documenti informatici mediante posta elettronica certificata: 
[pec_regole_tecniche_dm_2-nov-2005.pdf](https://www.agid.gov.it/sites/default/files/repository_files/leggi_decreti_direttive/pec_regole_tecniche_dm_2-nov-2005.pdf)

## Esempio di utilizzo
Standard:

    MimeMessage mimeMessage = ...;
    Messaggio messaggio = PECMessageParser.getInstance().parse(mimeMessage);

Con proprietà custom per l'elaborazione del MimeMessage (nella modalità standard vengono utilizzate le properties di sistema: <i>System.getProperties()</i>):

    MimeMessage mimeMessage = ...;
    Properties properties = ...;
    Messaggio messaggio = PECMessageParser.getInstance(properties).parse(mimeMessage);

Tramite l'utilizzo della libreria è possibile estrarre, a partire da un oggetto <i>javax.mail.internet.MimeMessage</i>, rappresentante un messaggio PEC:
- Busta di trasporto ([Busta](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/model/Busta.java))
- Eventuale messaggio di Posta Elettronica Certificata ([PEC](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/model/PEC.java))
- Eventuale ricevuta ([RicevutaPEC](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/model/RicevutaPEC.java))

Attraverso i metodi di utility offerti dalla classe [MessageUtils](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/util/MessageUtils.java) è possibile risalire al tipo di messaggio:
- PEC (MessageUtils.isPec(...))
- Ricevuta PEC (MessageUtils.isRicevutaPEC(...))
- Posta Ordinaria (MessageUtils.isEmailNormale(...))

Il messaggio infatti viene elaborato anche se non PEC:
- Messaggio ricevuto su una casella di Posta Elettronica Certificata: l'oggetto PEC conterrà le informazioni del messaggio normale in quanto comunque incapsulato in una busta
- Messaggio ricevuto su una casella di posta ordinaria: in questo caso la Busta rappresenta il messaggio normale

## Requisiti
Java 8 (o versioni successive)

## Altro
- La libreria supporta l'elaborazione di messaggi di posta ordinaria con codifica [UUencode](https://en.wikipedia.org/wiki/Uuencoding)
