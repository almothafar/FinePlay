LANG=en_US.UTF-8
./sbt dist
cd ./target/universal/
unzip ./fineplay-2.7.0-M1-α1-SNAPSHOT.zip
cd fineplay-2.7.0-M1-α1-SNAPSHOT
./bin/fineplay -Dconfig.resource=application_prod.conf -Dlogger.resource=logback_prod.xml
