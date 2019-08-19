@echo off
set BASEDIR=%~dp0
cd %BASEDIR%
start "" "%BASEDIR%\bin\javaw" -classpath "%BASEDIR%"\non-modular\* -Ditrysql.external-resources="%BASEDIR%"\external-resources -m app/com.package.Launcher %* && exit 0