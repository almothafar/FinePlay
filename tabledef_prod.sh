LANG=en_US.UTF-8
# graphviz
# brew install graphviz --with-librsvg --with-pango
java -jar ./misc/schemaspy-6.0.0-rc2.jar -t pgsql -dp ./misc/postgresql-42.2.2.jar -db fineplay -host localhost -port 5432 -s public -u postgres -o ./document/tabledef_prod -gv /usr/local/Cellar/graphviz/2.40.1 -cat %
