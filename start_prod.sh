LANG=en_US.UTF-8
./sbt -J-Xmx8g dist
cd ./target/universal/
unzip ./fineplay-2.8.0-M6-b2-SNAPSHOT.zip
cd fineplay-2.8.0-M6-b2-SNAPSHOT
./bin/fineplay -Dconfig.resource=application_prod.conf -Dlogger.resource=logback_prod.xml
