LANG=en_US.UTF-8
./sbt dist
cd ./target/universal/
unzip ./fineplay-2.6.9-α3-SNAPSHOT.zip
cd fineplay-2.6.9-α3-SNAPSHOT
./bin/fineplay -Dconfig.resource=application_prod.conf -Dlogger.resource=logback_prod.xml
