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

##Vorgehen:

Code vom Repository in Entwicklungsumgebung kopieren. Anschließend das Projekt zu einem Maven Projekt formatieren und das JAR für IBM Bluemix einbinden (zu finden unter https://github.com/watson-developer-cloud/java-sdk/?cm_mc_uid=99004903641114662439310&cm_mc_sid_50200000=1468573770)
Benötigt werden nun die auszuwertenden Reviews. In diesem Projekt wurden Reviews aus dieser Bibliothek verwendet: http://ai.stanford.edu/~amaas/data/sentiment/.
Im Anschluss muss die Config Datei ensprechend angepasst werden (siehe nächster Punkt). Die nach Ausführung des Programms erstellte CSV Datei muss nun manuell im RapidMiner importiert werden. In diesem Programm muss nun ein Lernmodell erstellt werden, welches verwendet werden kann um Vorhersagen zu "neuen" Reviews treffen zu können, die bereits durch das Java Programm verarbeitet wurden.

## Config für ibm bluemix hinzufügen:

1. Eine Datei namens "config.properties" im /src/ Verzeichnis erstellen
2. Die unten aufgeführten Variablen in die config kopieren und sie ohne Anführungszeichen setzen 

>`filepathToReviews=`

>`password=`

>`username=`

