# iTrySQL

Buildfile
---------

In der Datei _build.xml_ muss das Property `ReferencedLibrariesPath`
auf den Pfad gesetzt werden, unter dem die erforderlichen externen JAR-Dateien abgelegt sind.
(vgl. _Abhängigkeiten_)

Unter `fx:application` sollte die passende Versionsnummer eingetragen werden.
Gleiches gilt für das Attribut `Implementation-Version` im `<manifest>` Bereich des Buildfiles.

Im Tag `fx:deploy` muss je nach Plattform das Attribut `nativeBundles` auf einen anderen Wert als `dmg`
gesetzt werden (z.B. `all` - siehe dazu die entsprechende Dokumentation bei Oracle).

Abhängigkeiten
--------------

- HSQLB (http://www.hsqldb.org)
- Apache commons-lang3 (http://commons.apache.org)

Diese können mit dem Target `get-dependencies` im Buildfile heruntergeladen werden.
Hierzu muss das in `ReferencedLibrariesPath` angegebene Verzeichnis bereits angelegt sein.
(selbstverständlich können die Abhängigkeiten auch auf anderem Weg zur Verfügung gestellt
werden, z.B. über Project Settings / Libraries in IntelliJ IDEA, direkte Downloads beim
Anbieter der Bibliothek o.ä.) 

IDE spezifische Hinweise
------------------------

- Eclipse  
Meldet je nach installierten Plugins eventuell
`Access restriction: [...] restriction on required library [...] jfxrt.jar`, da
diese nicht in allen Java 8 Virtual Machines gleichermaßen verfügbar ist (die IBM Version
scheint hier betroffen zu sein). Hier kann entweder in den Compiler Einstellungen die Warnung
ausgeschaltet werden (da vermutlich sowieso eine "passende" Virtual Machine vorhanden ist
und andernfalls sowieso noch weitere Arbeiten erforderlich wären), oder am besten
installiert man die e(fx)clipse Erweiterung. Damit sind auch gleich noch ein paar
für FX nützliche neue Funktionen in Eclipse vorhanden, z.B. zum Editieren der
FX spezifischen CSS Dateien u.a.
