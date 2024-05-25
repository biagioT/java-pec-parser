

[![codebeat badge](https://codebeat.co/badges/b5b1b940-a242-4f95-ad63-75283d2ca778)](https://codebeat.co/projects/github-com-biagiot-java-pec-parser-master) [![CodeFactor](https://www.codefactor.io/repository/github/biagiot/java-pec-parser/badge)](https://www.codefactor.io/repository/github/biagiot/java-pec-parser)

# PEC/Mail Parser

Utility per l'elaborazione di messaggi di Posta Elettronica Certificata (e messaggi di posta ordinaria)

### Utilizzo
##### Dipendenza Maven
```
<dependency>
	<groupId>app.tozzi.mail</groupId>
	<artifactId>pec-parser</artifactId>
	<version>4.0.0</version>
</dependency>
```

##### Parsing

La libreria offre tre metodi della classe [PECMessageParser](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/app/tozzi/mail/pec/parser/PECMessageParser.java) per il parsing di mail/PEC:

1.  `Messaggio parse(MimeMessage mimeMessage)`  - elaborazione a partire da un oggetto  _javax.mail.internet.MimeMessage_ 
2.  `Messaggio parse(File emlFile)`  - elaborazione a partire da un oggetto  _java.io.File_  , ovvero il file EML
3.  `Messaggio parse(InputStream emlInputStream)`  - elaborazione a partire dall'oggetto_java.io.InputStream_  rappresentante lo stream EML

##### Istanza PECParser

E' possibile creare una istanza di PECMessageParser in due modi:

1.  `PECMessageParser getInstance(Properties properties)`  - con delle proprietà personalizzate che concorreranno alla creazione e alla elaborazione del  _MimeMessage_
2.  `PECMessageParser getInstance()`  - modalità default, vengono utilizzate le proprietà di sistema (_System.getProperties()_)

##### Messaggio

L'oggetto  [Messaggio](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/app/tozzi/mail/pec/model/Messaggio.java), risultato dell'elaborazione, conterrà:

-   Busta di trasporto ([Busta](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/app/tozzi/mail/pec/model/Busta.java))
-   Eventuale messaggio di Posta Elettronica Certificata ([PEC](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/app/tozzi/mail/pec/model/PEC.java))
-   Eventuale ricevuta ([RicevutaPEC](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/app/tozzi/mail/pec/model/RicevutaPEC.java))

Attraverso i metodi di utility offerti dalla classe  [MessageUtils](https://github.com/biagioT/java-pec-parser/blob/master/src/main/java/app/tozzi/mail/pec/util/MessageUtils.java)  è possibile risalire al tipo di messaggio:

-   PEC (`MessageUtils.isPec(Messaggio messaggio)`)
-   Ricevuta PEC (`MessageUtils.isRicevutaPEC(Messaggio messaggio)`)
-   Posta Ordinaria (`MessageUtils.isEmailNormale(Messaggio messaggio)`)

Il messaggio infatti viene elaborato anche se non PEC:

-   Se la mail viene ricevuta su una casella di Posta Elettronica Certificata: l'oggetto `PEC` conterrà le informazioni del messaggio normale in quanto comunque incapsulato in una busta.
-   Se la mail viene ricevuta su una casella di posta ordinaria: in questo caso l'oggetto `Busta` rappresenta il messaggio normale

### Requisiti
 - [ ] Java 8 (o versioni successive)


### Altro
 - [ ] La libreria supporta l'elaborazione di messaggi di posta ordinaria con codifica  [UUencode](https://en.wikipedia.org/wiki/Uuencoding)
 - [ ] La libreria è disponibile nel [catalogo Open Source di terze parti di Developers Italia](https://developers.italia.it/it/software/biagiot-java-pec-parser-09abab).


### Specifiche
 - [ ]  Regole tecniche del servizio di trasmissione di documenti informatici mediante posta elettronica certificata:  [pec_regole_tecniche_dm_2-nov-2005.pdf](https://www.agid.gov.it/sites/default/files/repository_files/leggi_decreti_direttive/pec_regole_tecniche_dm_2-nov-2005.pdf)

### Licenza

 - [ ] La licenza è disponibile [qui](https://github.com/biagioT/java-pec-parser/blob/master/LICENSE).
