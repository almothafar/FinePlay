LANG=en_US.UTF-8

./sbt clean

#./sbt test
# /fineplay/target/test-reports/*

./sbt jacoco
# /fineplay/target/scala-2.12/jacoco/report/html/index.html

./sbt findbugs
# /fineplay/target/scala-2.11/findbugs/report.html

./sbt cpd
# /fineplay/target/scala-2.11/cpd/cpd.xml

./sbt checkstyle
# /fineplay/target/checkstyle-report.html

./sbt dumpLicenseReport
# /fineplay/target/license-reports/fineplay-licenses.html
