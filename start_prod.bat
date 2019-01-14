sbt dist
cd target\universal
powershell Expand-Archive fineplay-2.7.0-RC9-βc11-SNAPSHOT.zip
cd fineplay-2.7.0-RC9-βc11-SNAPSHOT
bin\fineplay -Dconfig.resource=application_prod.conf -Dlogger.resource=logback_prod.xml
