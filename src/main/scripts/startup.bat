echo off
set JAVA_HOME=%CD%/jdk8
copy teclan-desktop-client-1.0-SNAPSHOT.jar "lib/teclan-desktop-client-1.0-SNAPSHOT.jar"
start %JAVA_HOME%/bin/java -cp lib/* com.teclan.desktop.client.Main
exit