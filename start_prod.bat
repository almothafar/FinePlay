sbt dist
cd target\universal
powershell Expand-Archive fineplay-2.6.6-α2-SNAPSHOT.zip
cd fineplay-2.6.6-α2-SNAPSHOT
bin\fineplay -Dconfig.resource=application_prod.conf -Dlogger.resource=logback_prod.xml
