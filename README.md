# Textmining-Projekt

##Beschreibung des Projekts:

Ziel ist es eine Java Applikation zu programmieren, welche Filmreviews mit
Hilfe der Watson Tone Analyzer API auswertet und die Ergebnisse dieser 
Auswertung in eine CSV Datei schreibt.
Diese CSV Datei soll mit dem DataMining Tool RapidMiner eingelesen werden und 
ein Maschinenlernverfahren angewendet werden.
Verwendet wurde das überwachte Lernverfahren Support Vector Machine.

##Verwendete Tools:

- Eclipse
- IBM Bluemix Watson Tone Analyzer
- RapidMiner
## Config für ibm bluemix hinzufügen:

1. Eine Datei namens "config.properties" im /src/ Verzeichnis erstellen
2. Die unten aufgeführten Variablen in die config kopieren und sie ohne Anführungszeichen setzen 

>`filepathToReviews=`

>`password=`

>`username=`

