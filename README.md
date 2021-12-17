[![Codacy Badge](https://app.codacy.com/project/badge/Grade/dbe9e634249f4850a22d9d8dcabee03e)](https://www.codacy.com/manual/biagioT/java-pec-parser?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=biagioT/java-pec-parser&amp;utm_campaign=Badge_Grade)

# PEC/Mail Parser
Utility per l'elaborazione di messaggi di Posta Elettronica Certificata (e messaggi di posta ordinaria)

### Utilizzo
##### Parsing
La libreria offre tre metodi per il parsing di mail/PEC:
1. `Messaggio parse(MimeMessage mimeMessage)`  - elaborazione a partire da un oggetto *javax.mail.internet.MimeMessage*
2. `Messaggio parse(File emlFile)` - elaborazione a partire dal *File* EML
3. `Messaggio parse(InputStream emlInputStream)` - elaborazione a partire dall'*InputStream* rappresentante l'EML

##### Istanza PECParser
E' possibile creare una istanza di PECParser in due modi:
1. `PECMessageParser getInstance(Properties properties)` - con delle proprietà personalizzate che concorreranno alla creazione e alla elaborazione del *MimeMessage*
2. `PECMessageParser getInstance()` - modalità default, vengono utilizzate le proprietà di sistema (*System.getProperties()*)

##### Messaggio
L'oggetto [Messaggio](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/model/Messaggio.java), risultato dell'elaborazione, conterrà:
- Busta di trasporto ([Busta](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/model/Busta.java))
- Eventuale messaggio di Posta Elettronica Certificata ([PEC](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/model/PEC.java))
- Eventuale ricevuta ([RicevutaPEC](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/model/RicevutaPEC.java))

Attraverso i metodi di utility offerti dalla classe [MessageUtils](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/it/tozzi/mail/pec/util/MessageUtils.java) è possibile risalire al tipo di messaggio:
- PEC (`MessageUtils.isPec(Messaggio messaggio)`)
- Ricevuta PEC (`MessageUtils.isRicevutaPEC(Messaggio messaggio)`)
- Posta Ordinaria (`MessageUtils.isEmailNormale(Messaggio messaggio)`)

Il messaggio infatti viene elaborato anche se non PEC:
- Messaggio ricevuto su una casella di Posta Elettronica Certificata: l'oggetto PEC conterrà le informazioni del messaggio normale in quanto comunque incapsulato in una busta
- Messaggio ricevuto su una casella di posta ordinaria: in questo caso la Busta rappresenta il messaggio normale

### Requisiti
- Java 8 (o versioni successive)
- Libreria OSS java-uudecoder - https://github.com/biagioT/java-uudecoder

### Altro
- La libreria supporta l'elaborazione di messaggi di posta ordinaria con codifica [UUencode](https://en.wikipedia.org/wiki/Uuencoding)

### Specifiche
- Regole tecniche del servizio di trasmissione di documenti informatici mediante posta elettronica certificata: 
[pec_regole_tecniche_dm_2-nov-2005.pdf](https://www.agid.gov.it/sites/default/files/repository_files/leggi_decreti_direttive/pec_regole_tecniche_dm_2-nov-2005.pdf)
