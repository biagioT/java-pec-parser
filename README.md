# java-pec-parser
Utility per l'elaborazione di messaggi di Posta Elettronica Certificata (e messaggi di posta ordinaria)

## Specifiche
Regole tecniche del servizio di trasmissione di documenti informatici mediante posta el ettronica certificata: 
[pec_regole_tecniche_dm_2-nov-2005.pdf](https://www.agid.gov.it/sites/default/files/repository_files/leggi_decreti_direttive/pec_regole_tecniche_dm_2-nov-2005.pdf)

## Esempio di utilizzo
    MimeMessage mimeMessage = ...;
    Messaggio messaggio = PECMessageParser.getInstance().parse(mimeMessage);
