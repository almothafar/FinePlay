LANG=en_US.UTF-8
# java -jar ./misc/schemaspy-6.1.0.jar -t mysql -dp ./misc/mysql-connector-java-8.0.19.jar -db fineplay -host localhost -port 3306 -s fineplay -u root -p password -o ./document/tabledef_prod -vizjs -cat %
java -jar ./misc/schemaspy-6.1.0.jar -t pgsql -dp ./misc/postgresql-42.2.12.jar -db fineplay -host localhost -port 5432 -s public -u postgres -o ./document/tabledef_prod -vizjs -cat %
