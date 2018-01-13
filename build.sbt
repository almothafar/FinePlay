name := """fineplay"""

organization := "hiro20v"

version := "2.6.11-α3-SNAPSHOT"

scalaVersion := "2.12.4"

//lazy val fineplaySub = (project in file("sub"))
//    .enablePlugins(PlayJava)

lazy val root = (project in file("."))
    .enablePlugins(PlayJava)
//    .aggregate(fineplaySub)
//    .dependsOn(fineplaySub)

// Another Repository
resolvers += "jasperreports" at "http://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/"

libraryDependencies ++= Seq(
//  "hiro20v" %% "fineplay-sub" % "2.6.11-α3-SNAPSHOT",
  javaJdbc,
  ehcache,
  jcache,
  "org.jsr107.ri" % "cache-annotations-ri-guice" % "1.1.0",
  javaWs,
  javaJpa,
  filters,
//  openId,
//  evolutions,
  "com.typesafe.play" %% "play-mailer" % "6.0.1",							// Apache 6.0.1
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  guice,
  "com.typesafe.play" %% "play-json" % "2.6.8",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.1.3",
  "com.h2database" % "h2" % "1.4.196",
//  "com.h2database" % "h2" % "1.4.196" % Test,
//  "com.typesafe.play" %% "play-ahc-ws-standalone-json" % "1.0.0",
//  "com.typesafe.play" %% "play-ahc-ws-standalone-xml" % "1.0.0",
//  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
//  "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1",
  "org.hibernate" % "hibernate-core" % "5.2.12.Final",					// LGPL 5.2.10
  "org.hibernate" % "hibernate-jpamodelgen" % "5.2.12.Final",				//
  "org.hibernate.validator" % "hibernate-validator" % "6.0.7.Final",		// Apache 6.0.2 // into play include
  "org.glassfish" % "javax.el" % "3.0.1-b09",
  "org.dom4j" % "dom4j" % "2.1.0",										// Origin 2.1.0
  "javax.json" % "javax.json-api" % "1.1.2",
  "org.glassfish" % "javax.json" % "1.1.2",
  "org.apache.commons" % "commons-text" % "1.2",							// Apache 1.1
  "org.postgresql" % "postgresql" % "42.1.4",							// BSD 2-clause 4.2.14
  "org.mockito" % "mockito-inline" % "2.13.0",
//  "org.mockito" % "mockito-core" % "2.13.0",							// MIT 2.13.0
  "net.sf.supercsv" % "super-csv" % "2.4.0",								// Apache 3.16
  "net.sf.supercsv" % "super-csv-java8" % "2.4.0",
  "org.apache.poi" % "poi" % "3.17",										// Apache 3.16
  "org.apache.poi" % "poi-scratchpad" % "3.17",
  "org.apache.poi" % "poi-ooxml" % "3.17",
  "com.google.zxing" % "javase" % "3.3.1",								// Apache 3.3.0
  "org.apache.pdfbox" % "pdfbox" % "2.0.8",								// Apache 2.0.7
  "org.jsoup" % "jsoup" % "1.11.2",										// MIT 1.11.2
  "com.squareup" % "javapoet" % "1.9.0",									// Apache 1.9.0
  "org.mapstruct" % "mapstruct-jdk8" % "1.2.0.Final",						// Apache 1.2.0.CR1
  "org.mapstruct" % "mapstruct-processor" % "1.2.0.Final",				//
  "com.github.spullara.mustache.java" % "compiler" % "0.9.5",				// Apache 0.9.5
  "net.lingala.zip4j" % "zip4j" % "1.3.2",								// Apache 1.3.2
  "org.webjars.npm" % "jquery" % "3.2.1",								// MIT 3.2.1
  "org.webjars.npm" % "bootstrap" % "4.0.0-beta.3",						// MIT 4.0.0-alpha.6
//  "org.webjars.bower" % "clipboard" % "1.7.1",							// MIT 1.7.1
//  "org.webjars.bower" % "holderjs" % "2.8.2",							// MIT 2.9.4 +patch
//  "org.webjars.npm" % "popper.js" % "1.13.0",							// MIT 1.13.0
  "org.webjars.npm" % "bootbox" % "4.4.0",								// MIT 4.4.0
  "org.webjars" % "pickadate.js" % "3.5.6",								// MIT 3.5.6
  "org.webjars.bower" % "seiyria-bootstrap-slider" % "9.7.2",				// MIT 9.8.1
  "org.webjars" % "jquery-ui-touch-punch" % "0.2.3-2",					// MIT/GPL V2 0.2.3
  "org.webjars.npm" % "tether" % "1.4.0",								// MIT 1.4.0
  "org.webjars" % "jquery-ui" % "1.12.1",								// MIT 1.12.1
  "org.webjars.bower" % "select2" % "4.0.5",								// MIT 4.0.4
  "org.webjars.npm" % "chart.js" % "2.7.1",								// MIT 2.7.1 +patch
  "org.webjars.bower" % "moment" % "2.20.1",								// MIT 2.19.3
  "org.webjars.bower" % "Snap.svg" % "0.5.1",							// Apache 0.5.1
  "org.webjars.bower" % "fullcalendar" % "3.8.0",							// MIT 3.8.0
  "org.webjars" % "openlayers" % "4.5.0",								// 2-Clause BSD 4.6.3
  "org.webjars.bower" % "datatables" % "1.10.16",							// MIT 1.10.16
  "org.webjars.bower" % "datatables.net-plugins" % "1.10.16",				//
  "org.webjars.bower" % "datatables.net-select" % "1.2.3",				//
//  "org.webjars.bower" % "summernote" % "0.8.9",							// MIT 0.8.9
  "org.webjars.bower" % "handsontable" % "0.34.0",						// MIT 0.34.0
  "org.webjars.bower" % "slick-carousel" % "1.8.1",						// MIT 1.8.1
  "org.webjars.npm" % "cropperjs" % "1.1.3",								// MIT 1.1.3
//  "org.webjars.npm" % "d3" % "4.10.2",									// BSD-3-Clause license 4.10.2
//  "org.webjars.npm" % "d3-geo-projection" % "1.2.1",					// BSD-3-Clause license 2.3.1
  "org.webjars.bower" % "highlightjs" % "9.12.0",							// BSD-3-Clause license 9.12.0
  "org.webjars.bower" % "diff2html" % "2.3.0",							// MIT 2.3.0
  "org.webjars.npm" % "marked" % "0.3.6",								// MIT 0.3.6
  "org.webjars.bower" % "parsleyjs" % "2.8.0",							// MIT 2.7.2
  "org.webjars.bower" % "tether-shepherd" % "1.8.1",						// MIT 1.8.1
  "org.webjars.bower" % "github-com-farbelous-bootstrap-colorpicker" % "2.5.1",	// Apache 2.5.1
  "org.webjars.npm" % "jqtree" % "1.4.2",								// Apache 1.4.2
  "org.webjars" % "pdf-js" % "1.9.426",									// Apache 1.9.426 +patch
  "org.webjars.bower" % "mocha" % "3.0.2",								// MIT 3.5.0
  "org.webjars.bower" % "chai" % "4.1.1"									// MIT 4.1.1
)
// TwentyTwenty															// MIT 2017/11/11 master
// Frappé Gantt															// MIT build version 0.0.7 master
// 3Dmol.js																// BSD-3-Clause license 1.1.1
// geckodriver															// ? 0.18.0
// LibreOffice															// Mozilla Public License
// wkhtmltopdf															// GNU Lesser General Public License v3.0 0.12.4

// Icon library
libraryDependencies ++= Seq(
//  "org.webjars.bower" % "font-awesome" % "5.0.2",						// Icons — CC 5.0.2
																		// Fonts — SIL OFL 5.0.2
																		// Code — MIT 5.0.2
  "org.webjars" % "material-design-icons" % "3.0.1",						// Apache 3.0.1
  "org.webjars.npm" % "emojione" % "3.1.2",								// EmojiOne Artwork Free license 3.1.2 +patch
  																		// EmojiOne Non-Artwork MIT 3.1.2
  "org.webjars.npm" % "emojione-assets" % "3.1.1"							// Free License 3.1.1
)
// Font Awesome															//
// IcoFont																// MIT 1.3

// JasperReports library
libraryDependencies ++= Seq(
  "net.sf.jasperreports" % "jasperreports" % "6.4.3",						// LGPL 6.4.3
  "net.sf.jasperreports" % "jasperreports-fonts" % "6.0.0",
  "net.sf.barcode4j" % "barcode4j" % "2.1",								// Apache 2.1
  "net.sourceforge.barbecue" % "barbecue" % "1.5-beta1",					// BSD-style 1.5-beta1
  "org.apache.xmlgraphics" % "batik-bridge" % "1.9.1"
)

// Batch library
libraryDependencies ++= Seq(
  "javax.enterprise" % "cdi-api" % "2.0",
  "org.jboss.weld.se" % "weld-se" % "2.4.5.Final",						// Apache 2.4.5.Final
  "org.jboss.spec.javax.batch" % "jboss-batch-api_1.0_spec" % "1.0.0.Final",
  "org.jboss.marshalling" % "jboss-marshalling" % "2.0.2.Final",			// Apache 2.0.2.Final
  "org.jboss.logging" % "jboss-logging" % "3.3.1.Final",
  "org.jberet" % "jberet-core" % "1.2.5.Final",							// Eclipse Public 1.2.5.Final
  "org.jberet" % "jberet-support" % "1.2.0.Final",
  "org.jberet" % "jberet-se" % "1.2.0.Final",
  "org.wildfly.security" % "wildfly-security-manager" % "1.1.2.Final"		// ? 1.1.2.Final
)

excludeDependencies ++= Seq(
  "org.hibernate" % "hibernate-validator"								// into play include
)

// //////////

// Deploying Play with JPA
//PlayKeys.externalizeResources := false

javaOptions in Test += "-Dconfig.file=conf/application_test.conf"
// Support is best in Firefox 52.0.3 and onwards
javaOptions in Test += "-Dwebdriver.gecko.driver=misc/geckodriver"

// Create JPA metamodel
javacOptions in Compile ++= Seq("-encoding", "UTF-8", "-s", "generate")
managedSourceDirectories in Compile += baseDirectory.value / "generate"
unmanagedSourceDirectories in Test += baseDirectory.value / "generate"

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

// Java project. Don't expect Scala IDE
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
// Use .class files instead of generated .scala files for views and routes
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)

// Create javadoc
sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java"))

// Copy [Folder] on dist
// fineplay/[Folder] -> /fineplay/target/universal/fineplay-[version]-SNAPSHOT/[Folder]

// Copy prod on dist
mappings in Universal ++= {
  val prodFolder = baseDirectory(_ / "prod").value
  val prodFolderLength = prodFolder.getCanonicalPath.length
  (prodFolder ** "*").get.map { f: File =>
    f -> ("prod/" + f.getCanonicalPath.substring(prodFolderLength))
  }
}
// Copy temp on dist
mappings in Universal ++= {
  val tempFolder = baseDirectory(_ / "temp").value
  val tempFolderLength = tempFolder.getCanonicalPath.length
  (tempFolder ** "*").get.map { f: File =>
    f -> ("temp/" + f.getCanonicalPath.substring(tempFolderLength))
  }
}

// Copy public on dist
mappings in Universal ++= {
  val publicFolder = baseDirectory(_ / "public").value
  val publicFolderLength = publicFolder.getCanonicalPath.length
  (publicFolder ** "*").get.map { f: File =>
    f -> ("public/" + f.getCanonicalPath.substring(publicFolderLength))
  }
}

// mappings in Universal ++= directory(baseDirectory.value / "resources")

// playEnhancerEnabled := false

// JaCoCo
testOptions in jacocoReportSettings += Tests.Setup( () => {
	System.setProperty("config.file", "conf/application_test.conf");
	System.setProperty("webdriver.gecko.driver", "misc/geckodriver");
} )
jacocoReportSettings := JacocoReportSettings(
  "Jacoco Coverage Report",
  None,
  JacocoThresholds(),
  Seq(JacocoReportFormats.ScalaHTML),
  "utf-8")

// FindBugs
findbugsReportPath := Some(crossTarget.value / "findbugs" / "report.html")
findbugsReportType := Some(com.github.sbt.findbugs.settings.FindbugsReport.FancyHtml)

// CPD
cpdLanguage := CpdLanguage.Java
cpdReportType := CpdReportType.XML
