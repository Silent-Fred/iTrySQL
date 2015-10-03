# iTrySQL

Abhängigkeiten
--------------

- HSQLB (http://www.hsqldb.org)
- Apache commons-lang3 (http://commons.apache.org)

Buildfile
---------

In der Datei _build.xml_ muss das Property `ReferencedLibrariesPath`
auf den Pfad gesetzt werden, unter dem die oben genannten JAR-Dateien abgelegt sind.

Unter `fx:application` sollte die passende Versionsnummer eingetragen werden.
Gleiches gilt für das Attribut `Implementation-Version` im `<manifest>` Bereich des Buildfiles.

Im Tag `fx:deploy` muss je nach Plattform das Attribut `nativeBundles` auf einen anderen Wert als `dmg`
gesetzt werden (siehe dazu die entsprechende Dokumentation z.B. bei Oracle).
