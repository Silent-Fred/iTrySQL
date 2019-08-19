# iTrySQL

What it does
------------

It is a training software. Designed to accompany a training on database and
SQL fundamentals. It doesn't require a very specific infrastructure nor the
installation of a database. Participants are ready to go with their database
and example data in less than 30 seconds.

How to build it
---------------

Either add `jlink` to your path or set the appropriate executable in the `pom.xml`.
See `jlinkExecutable` in the configuration of the JavaFX Maven plugin.

Then...

`mvn clean javafx:jlink prepare-package package`

What will be built?
-------------------

On macOS, an application bundle will be built in the build directory. (`target` folder)

On Windows, the `jlink` result is being used without much further treatment.

This is hopefully only a temporary solution until `jpackage` might be available (again) in
future releases of the JDK.

What to expect?
---------------

Okay, to be honest, the Windows bundle hasn't even been tested yet.
It may require some modifications that I will add once I'll have a
Windows computer at hand.
