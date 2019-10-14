LANG=en_US.UTF-8
java -jar ./misc/schemaspy-6.1.0.jar -t pgsql -dp ./misc/postgresql-42.2.8.jar -db fineplay -host localhost -port 5432 -s public -u postgres -o ./document/tabledef_prod -vizjs -cat %
