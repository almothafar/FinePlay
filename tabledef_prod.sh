LANG=en_US.UTF-8
#PATH=$PATH:/usr/local/Cellar/graphviz/2.40.1/bin
#PATH=$PATH:/usr/local/bin
#export PATH
#echo $PATH
java -jar ./misc/schemaspy-6.0.0-rc2.jar -t pgsql -dp ./misc/postgresql-42.1.4.jar -db fineplay -host localhost -port 5432 -s public -u postgres -o ./document/tabledef
