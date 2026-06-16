# Changelog

Tutte le modifiche rilevanti a questo progetto saranno documentate in questo file.
Il formato è basato su [Keep a Changelog](https://keepachangelog.com/it/1.0.0/)
e questo progetto aderisce a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [6.0.0] - 2026-06-16
### Aggiunto
- Supporto a formati multipli e migliorata l'architettura dei parser.
- Protezione contro attacchi XXE injection nel `DocumentBuilderFactory`.
- `CHANGELOG.md` per tracciare le modifiche in italiano.

### Modificato
- Refactoring del parsing del corpo del messaggio (UUEncoding) per evitare la perdita di allegati.
- Ottimizzazione nella copia degli stream in `IOUtils` per gestire `write` parziali.
- Utilizzo di `Session.getInstance` per evitare di ignorare proprietà fornite se la sessione di default è già stata creata.
- Formato `DATE_FORMAT` corretto a `yyyyMMddHHmmss` anziché `YYYYMMDDHHMMSS` (per eventuale generazione di messageID univoco).
- Codifica ID sicura (`SHA-256`) passata a formato Hex anziché decodifica UTF-8 lossy (per eventuale generazione di messageID univoco).


### Risolto
- Risolto StackOverflow ricorsivo in `getVersionedFileName`.
- Bug nell'uso del post-increment che causava l'errore precedente corretto (`i++` -> `i + 1`).
- Gestito il caso base mancante in `isOctal` per valore "0".
- Risolto memory leak e `InputStream` ora correttamente chiuso nella decodifica UUencode.
- Fix per `getHeaderValues` che occasionalmente ritornava `null` causando `NullPointerException`.
