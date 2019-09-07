LANG=en_US.UTF-8

./sbt clean

./sbt compile

#./sbt test
# /fineplay/target/test-reports/*

./sbt jacoco
# /fineplay/target/scala-2.13/jacoco/report/html/index.html

#./sbt cpd
# /fineplay/target/scala-2.13/cpd/cpd.xml

./sbt checkstyle
# /fineplay/target/checkstyle-report.html

./sbt dumpLicenseReport
# /fineplay/target/license-reports/fineplay-licenses.html

./sbt dependencyDot
# /fineplay/target/dependencies-compile.dot
