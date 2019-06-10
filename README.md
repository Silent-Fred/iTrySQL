# iTrySQL

Buildfile
---------

In der Datei _build.xml_ muss das Property `ReferencedLibrariesPath`
auf den Pfad gesetzt werden, unter dem die erforderlichen externen JAR-Dateien abgelegt sind.
(vgl. _Abhängigkeiten_)

Im Property `AppVersion` die passende (Marketing-)Versionsnummer eintragen.

Im Tag `fx:deploy` muss je nach Plattform das Attribut `nativeBundles` auf einen anderen Wert als `dmg`
gesetzt werden (z.B. `all` - siehe dazu die entsprechende Dokumentation bei Oracle).

Das Buildskript muss in einer eigenen Runtime gestartet werden.
(Eclipse z.B.: _Run -> External Tools -> External Tools Configurations..._ Für `build.xml` unter _JRE_ _Separate JRE_ auswählen)
Andernfalls ist der Ausgabepfad der durch `fx:deploy` erzeugten Bundles nicht wie erwartet. Alternativ kann das Ant Property
`BundleOutputPath` auch auf einen absolut angegeben Pfad gesetzt werden.


Abhängigkeiten
--------------

- HSQLB (http://www.hsqldb.org)

Diese können mit dem Target `get-dependencies` im Buildfile heruntergeladen werden.
Hierzu muss das in `ReferencedLibrariesPath` angegebene Verzeichnis bereits angelegt sein.
(selbstverständlich können die Abhängigkeiten auch auf anderem Weg zur Verfügung gestellt
werden, z.B. über Project Settings / Libraries in IntelliJ IDEA, direkte Downloads beim
Anbieter der Bibliothek o.ä.) 
