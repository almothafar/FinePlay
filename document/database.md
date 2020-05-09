Database
=======

Connection
----------

### Develop ###

	/Users/hiro/github/FinePlay/build.sbt
	"com.h2database" % "h2" % "1.4.199"

	github/FinePlay/conf/application.conf
	db.default.[*]

	github/FinePlay/conf/META-INF/persistence.xml
	<persistence-unit name="defaultPersistenceUnit"...

	github/FinePlay/conf/jberet.properties
	db-[*]

### Production ###

###### The frequency of checking is fairly low. ######

	/Users/hiro/github/FinePlay/build.sbt
	"org.postgresql" % "postgresql" % "42.2.12"

	github/FinePlay/conf/application_prod.conf
	db.default.[*]

	github/FinePlay/conf/META-INF/persistence.xml
	<persistence-unit name="defaultPersistenceUnit_prod"...

	github/FinePlay/conf/jberet_prod.properties
	db-[*]

### Table definition ###

###### The frequency of checking is fairly low. ######

	github/FinePlay/tabledef_prod.sh

	github/FinePlay/misc/postgresql-42.2.12.jar
