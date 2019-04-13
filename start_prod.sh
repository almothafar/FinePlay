LANG=en_US.UTF-8
./sbt -J-Xmx4g dist
cd ./target/universal/
unzip ./fineplay-2.7.0-b5-SNAPSHOT.zip
cd fineplay-2.7.0-b5-SNAPSHOT
./bin/fineplay -Dconfig.resource=application_prod.conf -Dlogger.resource=logback_prod.xml
