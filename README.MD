# PEC/Mail Parser
Libreria utility per l'elaborazione di messaggi di [Posta Elettronica Certificata](https://www.agid.gov.it/it/piattaforme/posta-elettronica-certificata) (PEC) e messaggi di posta ordinaria.

### Dipendenza progetto
Attenzione: le versioni precedenti alla 5.0.0 sono state rilasciato con groupId `app.tozzi.mail`

##### Maven
```
<dependency>
	<groupId>app.tozzi</groupId>
	<artifactId>pec-parser</artifactId>
	<version>5.0.1</version>
</dependency>
```

##### Gradle
```
implementation 'app.tozzi:pec-parser:5.0.1 
```

### Requisiti

| PEC Parser        | Java      |  
|-------------------|-----------|  
| [v0.0.1 - v4.0.0] | [8 - 22]  |  
| [v5.0.0 - latest] | [17 - 22] |  

### Utilizzo
#### 1. Istanza di `MailParser`
Attraverso uno dei seguenti metodi:
- [ ] `MailParser.getInstance()`
- [ ] `MailParser.getInstance(Properties properties)`
- [ ] `MailParser.getInstance(Properties properties, boolean extractAllHeaders)`

#### 2. Parsing
Attraverso uno dei seguenti metodi:
- [ ] `ParsedEntity parse(MimeMessage mimeMessage)`
- [ ] `ParsedEntity parse(File eml)`
- [ ] `ParsedEntity parse(InputStream inputStream)`
- [ ] `ParsedEntity parse(MimeMessage mimeMessage)`

#### 3. `ParsedEntity`
Può essere di tipo:
- [ ] `PEC`: se il messaggio rappresenta una PEC
- [ ] `PECReceipt`: se il messaggio rappresenta una ricevuta PEC
- [ ] `Mail`: se il messaggio rappresenta una semplice mail di posta ordinaria

### Altro
- [ ] La libreria supporta l'elaborazione di messaggi di posta ordinaria con codifica  [UUencode](https://en.wikipedia.org/wiki/Uuencoding)
- [ ] La libreria è disponibile nel [catalogo Open Source di terze parti di Developers Italia](https://developers.italia.it/it/software/biagiot-java-pec-parser-09abab).

### Specifiche
- [ ]  Regole tecniche del servizio di trasmissione di documenti informatici mediante posta elettronica certificata:  [pec_regole_tecniche_dm_2-nov-2005.pdf](https://www.agid.gov.it/sites/default/files/repository_files/leggi_decreti_direttive/pec_regole_tecniche_dm_2-nov-2005.pdf)

### Licenza

- [ ] La licenza è disponibile [qui](https://github.com/biagioT/java-pec-parser/blob/main/LICENSE).