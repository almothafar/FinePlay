name := """fineplay"""

version := "2.6.3-α1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  javaJdbc,
  ehcache,
  jcache,
  "org.jsr107.ri" % "cache-annotations-ri-guice" % "1.0.0",
  javaWs,
  javaJpa,
  filters,
//  openId,
//  evolutions,
  "com.typesafe.play" %% "play-mailer" % "6.0.0",						// Apache 6.0.1
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.0",
  guice,
  "com.typesafe.play" %% "play-json" % "2.6.0",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.0.0",
  "com.h2database" % "h2" % "1.4.196",
//  "com.h2database" % "h2" % "1.4.196" % Test,
//  "com.typesafe.play" %% "play-ahc-ws-standalone-json" % "1.0.0",
//  "com.typesafe.play" %% "play-ahc-ws-standalone-xml" % "1.0.0",
//  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
//  "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1",
//  "xerces" % "xercesImpl" % "2.11.0",
//  "org.apache.tomcat" % "tomcat-servlet-api" % "8.0.33",
  "org.hibernate" % "hibernate-core" % "5.2.10.Final",					// LGPL 5.2.10
  "org.hibernate" % "hibernate-jpamodelgen" % "5.2.10.Final",			//
  "org.hibernate.validator" % "hibernate-validator" % "6.0.1.Final",	// Apache 6.0.1 // into play include
  "org.glassfish" % "javax.el" % "3.0.1-b08",
  "org.dom4j" % "dom4j" % "2.0.1",
  "javax.json" % "javax.json-api" % "1.1",
  "org.glassfish" % "javax.json" % "1.1",
  "org.apache.commons" % "commons-text" % "1.1",						// Apache 1.1
  "org.postgresql" % "postgresql" % "42.1.1",							// BSD 2-clause 4.2.14
  "org.mockito" % "mockito-inline" % "2.8.47",
//  "org.mockito" % "mockito-core" % "2.8.47",							// MIT 2.8.54
  "net.sf.supercsv" % "super-csv" % "2.4.0",							// Apache 3.16
  "net.sf.supercsv" % "super-csv-java8" % "2.4.0",
  "org.apache.poi" % "poi" % "3.16",									// Apache 3.16
  "org.apache.poi" % "poi-scratchpad" % "3.16",
  "org.apache.poi" % "poi-ooxml" % "3.16",
  "com.google.zxing" % "javase" % "3.3.0",								// Apache 3.3.0
  "org.apache.pdfbox" % "pdfbox" % "2.0.7",								// Apache 2.0.7
  "org.jsoup" % "jsoup" % "1.10.3",										// MIT 1.10.3
  "com.squareup" % "javapoet" % "1.9.0",								// Apache 1.9.0
  "org.mapstruct" % "mapstruct-jdk8" % "1.2.0.CR1",						// Apache 1.2.0.CR1
  "org.mapstruct" % "mapstruct-processor" % "1.2.0.CR1",				//
  "com.github.spullara.mustache.java" % "compiler" % "0.9.5",			// Apache 0.9.5
  "org.webjars.npm" % "jquery" % "3.2.1",								// MIT 3.2.1
  "org.webjars.npm" % "bootstrap" % "4.0.0-beta",						// MIT 4.0.0-alpha.6
  "org.webjars.npm" % "bootbox" % "4.4.0",								// MIT 4.4.0
  "org.webjars" % "pickadate.js" % "3.5.6",								// MIT 3.5.6
  "org.webjars.bower" % "seiyria-bootstrap-slider" % "9.7.2",			// MIT 9.8.1
  "org.webjars" % "jquery-ui-touch-punch" % "0.2.3-2",					// MIT/GPL V2 0.2.3
  "org.webjars.npm" % "tether" % "1.4.0",								// MIT 1.4.0
  "org.webjars.bower" % "holderjs" % "2.8.2",							// MIT 2.9.4
  "org.webjars.bower" % "font-awesome" % "4.7.0",						// SIL OFL 4.7.0
  "org.webjars" % "jquery-ui" % "1.12.1",								// MIT 1.12.1
  "org.webjars.bower" % "select2" % "4.0.3",							// MIT 4.0.3
  "org.webjars.bower" % "chartjs" % "2.6.0",							// MIT 2.6.0
  "org.webjars.bower" % "moment" % "2.18.1",							// MIT 2.18.1
  "org.webjars.bower" % "Snap.svg" % "0.5.1",							// Apache 0.5.1
  "org.webjars.bower" % "fullcalendar" % "3.4.0",						// MIT 3.4.0
  "org.webjars" % "openlayers" % "4.2.0",								// 2-Clause BSD 4.2.0
//  "org.webjars" % "datatables" % "1.10.15",							// MIT 1.10.15
  "org.webjars" % "datatables" % "1.10.13",								//
  "org.webjars" % "datatables-plugins" % "1.10.12",						//
  "org.webjars.bower" % "datatables.net-select" % "1.2.0",				//
  "org.webjars" % "ckeditor" % "4.7.1",									// GPL/LGPL/MPL 4.7.1
  "org.webjars.bower" % "handsontable" % "0.32.0",						// MIT 1.10.15
  "org.webjars.bower" % "slick-carousel" % "1.6.0",						// MIT 1.7.1
//  "org.webjars.npm" % "cropperjs" % "1.0.0",							// MIT 1.0.0-rc.3
  "org.webjars.npm" % "d3" % "4.9.1",									// BSD-3-Clause license 4.10.0
  "org.webjars.npm" % "d3-geo-projection" % "1.2.1",					// BSD-3-Clause license 2.2.0
  "org.webjars.bower" % "highlightjs" % "9.12.0",						// BSD-3-Clause license 9.12.0
  "org.webjars.bower" % "diff2html" % "2.3.0",							// MIT 2.3.0
  "org.webjars.npm" % "marked" % "0.3.6",								// MIT 0.3.6
  "org.webjars.bower" % "parsleyjs" % "2.7.2",							// MIT 2.7.2
  "org.webjars.bower" % "tether-shepherd" % "1.8.1",					// MIT 1.8.1
  "org.webjars.bower" % "github-com-farbelous-bootstrap-colorpicker" % "2.5.1",	// Apache 2.5.1
  "org.webjars.npm" % "jqtree" % "1.3.2",								// Apache 1.4.2
  "org.webjars" % "pdf-js" % "1.5.188",									// Apache 1.8.188
  "org.webjars" % "material-design-icons" % "3.0.1",					// Apache 3.0.1
  "org.webjars.bower" % "clipboard" % "1.7.1",							// MIT 1.7.1
  "org.webjars.npm" % "anchor-js" % "4.0.0",							// MIT 4.0.0
//  "org.webjars.npm" % "github-com-FezVrasta-popper-js" % "1.11.0",	// MIT 1.11.1
  "org.webjars.bower" % "mocha" % "3.0.2",								// MIT 3.5.0
  "org.webjars.bower" % "chai" % "4.1.1"								// MIT 4.1.1
)
// Frappé Gantt															// MIT 2017/8/11 master
// wkhtmltopdf															// GNU Lesser General Public License v3.0 0.12.4
// 3Dmol.js																// BSD-3-Clause license 1.1.1
// IcoFont																// MIT 1.3
// geckodriver															// ? 0.18.0

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
testOptions in jacoco.Config += Tests.Setup( () => {
	System.setProperty("config.file", "conf/application_test.conf");
	System.setProperty("webdriver.gecko.driver", "misc/geckodriver");
} )
jacoco.settings
parallelExecution in jacoco.Config := false

// FindBugs
import de.johoop.findbugs4sbt._
findbugsSettings
findbugsReportPath := Some(crossTarget.value / "findbugs" / "report.html")
findbugsReportType := Some(de.johoop.findbugs4sbt.ReportType.FancyHistHtml)

// PMD/CPD
import de.johoop.cpd4sbt._
enablePlugins(CopyPasteDetector)
cpdReportName:= "cpd.xml"
cpdLanguage := de.johoop.cpd4sbt.Language.Java
cpdReportType := de.johoop.cpd4sbt.ReportType.XML

// CheckStyle
checkstyleConfigLocation := CheckstyleConfigLocation.File("conf/checkstyle-config.xml")
checkstyleXsltTransformations := {
  Some(Set(CheckstyleXSLTSettings(baseDirectory(_ / "conf" / "checkstyle-noframes.xml").value, target(_ / "checkstyle-report.html").value)))
}
