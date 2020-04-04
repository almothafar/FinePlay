name := """fineplay"""

organization := "hiro20v"

maintainer := "hiro20v++@icloud.com"

version := "2.8.1-b1-SNAPSHOT"

scalaVersion := "2.13.1"

//lazy val fineplaySub = (project in file("sub"))
//    .enablePlugins(PlayJava)

lazy val root = (project in file("."))
    .enablePlugins(PlayJava)
    .settings(
    )
//    .aggregate(fineplaySub)
//    .dependsOn(fineplaySub)

// Another Repository
resolvers += "jcenter" at "https://jcenter.bintray.com"
resolvers += "jasperreports" at "https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/"

libraryDependencies ++= Seq(
  "org.jacoco" % "org.jacoco.agent" % "0.8.5",							// EPL 0.8.5
//  "hiro20v" %% "fineplay-sub" % "2.8.1-b1-SNAPSHOT",
  javaJdbc,
  caffeine,
  javaWs,
  javaJpa,
  filters,
  ws,
//  openId,
//  evolutions,
  "com.typesafe.play" %% "play-mailer" % "7.0.1",						// Apache 7.0.1
  "com.typesafe.play" %% "play-mailer-guice" % "7.0.1",
  guice,
  "com.h2database" % "h2" % "1.4.199",									// MPL/EPL 1.4.200
//  "com.h2database" % "h2" % "1.4.199" % Test,
  "org.mindrot" % "jbcrypt" % "0.4",									// ISC/BSD 0.4
  "net.jodah" % "failsafe" % "2.3.1",									// Apache 2.3.1
  "net.logstash.logback" % "logstash-logback-encoder" % "6.2",			// Apache 6.2
  "org.hibernate" % "hibernate-core" % "5.4.12.Final",					// LGPL 5.4.12
  "org.hibernate" % "hibernate-jpamodelgen" % "5.4.12.Final",			//
  "org.glassfish" % "javax.el" % "3.0.1-b09",
  "javax.json" % "javax.json-api" % "1.1.4",
  "org.glassfish" % "javax.json" % "1.1.4",
  "javax.money" % "money-api" % "1.0.3",								// Apache 1.0.3
  "org.javamoney" % "moneta" % "1.3",									// Apache 1.3
  "org.apache.commons" % "commons-text" % "1.8",						// Apache 1.8
  "org.postgresql" % "postgresql" % "42.2.10",							// BSD 2-clause 42.2.10
  "org.mockito" % "mockito-core" % "3.2.0",								// MIT 3.2.0
  "net.sf.supercsv" % "super-csv" % "2.4.0",							// Apache 2.4.0
  "net.sf.supercsv" % "super-csv-java8" % "2.4.0",
  "org.apache.poi" % "poi" % "4.1.1",									// Apache 4.1.1
  "org.apache.poi" % "poi-scratchpad" % "4.1.1",
  "org.apache.poi" % "poi-ooxml" % "4.1.1",
  "org.apache.tika" % "tika-core" % "1.23",								// Apache 1.23
  "com.google.zxing" % "javase" % "3.4.0",								// Apache 3.4.0
  "org.webjars.bower" % "quagga" % "0.12.1",							// MIT 0.12.1
  "org.apache.pdfbox" % "pdfbox" % "2.0.17",							// Apache 2.0.17
  "org.jsoup" % "jsoup" % "1.12.1",										// MIT 1.12.1
  "com.squareup" % "javapoet" % "1.11.1",								// Apache 1.11.1
  "org.mapstruct" % "mapstruct" % "1.3.1.Final",						// Apache 1.3.1
  "org.mapstruct" % "mapstruct-processor" % "1.3.1.Final",				//
  "com.github.spullara.mustache.java" % "compiler" % "0.9.6",			// Apache 0.9.6
  "net.lingala.zip4j" % "zip4j" % "2.5.1",								// Apache 2.5.1
//  "org.webjars.npm" % "jquery" % "3.4.1",								// MIT 3.4.1
  "org.webjars.npm" % "bootstrap" % "4.4.1",							// MIT 4.4.1
  "org.webjars.bowergithub.makeusabrew" % "bootbox" % "5.4.0",			// MIT 5.4.0
  "org.webjars.bower" % "pickadate" % "3.6.4",							// MIT 3.6.4
  "org.webjars.npm" % "bootstrap-slider" % "10.6.2",					// MIT 10.6.2
  "org.webjars.bower" % "hammerjs" % "2.0.8",							// MIT 2.0.8
  "org.webjars" % "jquery-ui-touch-punch" % "0.2.3-2",					// MIT/GPL V2 0.2.3
  "org.webjars" % "jquery-ui" % "1.12.1",								// MIT 1.12.1
  "org.webjars.bower" % "Split.js" % "1.5.9",							// MIT 1.5.9
  "org.webjars.bower" % "select2" % "4.0.10",							// MIT 4.0.10
  "org.webjars.npm" % "chart.js" % "2.9.3",								// MIT 2.9.3
  "org.webjars.bower" % "moment" % "2.24.0",							// MIT 2.24.0
  "org.webjars.npm" % "moment-timezone" % "0.5.27",						// MIT 0.5.27
  "org.webjars.bower" % "Snap.svg" % "0.5.1",							// Apache 0.5.1
  "org.webjars.bower" % "fullcalendar" % "3.10.0",						// MIT 3.10.0
  "org.webjars" % "openlayers" % "5.2.0",								// 2-Clause BSD 5.2.0
  "org.webjars.bower" % "datatables" % "1.10.19",						// MIT 1.10.19
  "org.webjars.bower" % "datatables.net-plugins" % "1.10.19",			//
  "org.webjars.bower" % "datatables.net-select" % "1.3.0",				//
  "org.webjars.bower" % "summernote" % "0.8.12",						// MIT 0.8.12
  "org.webjars.npm" % "handsontable" % "6.2.0",							// MIT/Pro 6.2.0
  "org.webjars.bower" % "slick-carousel" % "1.8.1",						// MIT 1.8.1
  "org.webjars.npm" % "cropperjs" % "1.5.1",							// MIT 1.5.1
  "org.webjars.npm" % "d3" % "5.11.0",									// BSD-3-Clause license 5.11.0
  "org.webjars.npm" % "d3-geo-projection" % "2.5.1",					// MIT 2.5.1
  "org.webjars.npm" % "jqvmap" % "1.5.1",								// MIT/GPL 1.5.1
  "org.webjars.npm" % "prettier" % "1.18.2",							// MIT 1.18.2
  "org.webjars.bower" % "highlightjs" % "9.12.0",						// BSD-3-Clause license 9.12.0
  "org.webjars.npm" % "signature_pad" % "3.0.0-beta.3",					// MIT 3.0.0-beta.3
  "org.webjars.npm" % "diff" % "4.0.1",									// BSD 4.0.1
  "org.webjars.npm" % "diff2html" % "2.9.0",							// MIT 2.9.0
//  "org.webjars.npm" % "marked" % "0.5.2",								// MIT 0.5.2
  "org.webjars.npm" % "easymde" % "2.8.0",								// MIT 2.8.0
//  "org.webjars.npm" % "typo-js" % "1.1.0",							// Modified BSD 1.1.0
  "org.webjars.npm" % "viz.js" % "2.1.2",								// MIT 2.1.2
  "org.webjars.bowergithub.hakimel" % "reveal.js" % "3.7.0",			// MIT 3.7.0
//  "org.webjars.npm" % "paper-css" % "0.4.1",							// MIT 0.4.1
  "org.webjars.bower" % "echarts" % "4.7.0",							// Apache 4.7.0
  "org.webjars.npm" % "lightweight-charts" % "1.0.2",					// Apache 1.0.2
  "org.webjars.npm" % "mermaid" % "8.4.0",								// MIT 8.4.0
  "org.webjars.bower" % "parsleyjs" % "2.8.1",							// MIT 2.8.1
//  "org.webjars.npm" % "shepherd.js" % "2.0.1",						// MIT 2.4.0
  "org.webjars.npm" % "bootstrap-colorpicker" % "3.1.2",				// MIT 3.1.2
  "org.webjars.npm" % "jqtree" % "1.4.9",								// Apache 1.4.9
//  "org.webjars.npm" % "pdfjs-dist" % "2.0.550",						// Apache 2.0.550 +patch
  "org.webjars.npm" % "html2canvas" % "1.0.0-rc.5",						// 1.0.0-rc.5
//  "org.webjars.npm" % "jspdf" % "1.5.3",								// MIT 1.5.3
  "org.webjars.npm" % "jsqr" % "1.2.0",									// Apache 1.2.0
  "org.webjars.npm" % "holderjs" % "2.9.4",								// MIT 2.9.4
  "org.webjars.npm" % "3dmol" % "1.3.7",								// BSD-3-Clause license 1.3.7
  "org.webjars.npm" % "github-com-Tencent-vConsole" % "3.3.0",			// MIT 3.3.0
  "org.webjars.bower" % "mocha" % "3.0.2",								// MIT 3.5.0
  "org.webjars.bower" % "chai" % "4.1.1"								// MIT 4.1.1
)
// Mapbox GL JS															// BSD-3-Clause 1.4.1
// Mapbox GL Language													// BSD-3-Clause 0.10.1
// jQuery RTL Scroll Type Detector										// MIT Apr 26, 2017 master
// TwentyTwenty															// ISC? MIT? Aug 7, 2018 master
// markdeep																// BSD archive master(Sep 10, 2018)
// Frappé Gantt															// MIT build version 0.0.7 master
// A-Frame																// MIT 1.0.3/0.9.2
// geckodriver															// Mozilla Public License 0.18.0

// Use bootstrap
libraryDependencies ++= Seq(
//  "org.webjars.bower" % "clipboard" % "2.0.4",						// MIT 2.0.0
  "org.webjars.npm" % "popper.js" % "1.16.0"							// MIT 1.16.0
)

// Polyfill
libraryDependencies ++= Seq(
  "org.webjars.npm" % "core-js-bundle" % "3.6.1",						// MIT 3.6.1
  "org.webjars.bower" % "fetch" % "2.0.4"								// MIT 2.0.4
)

// Icon library
libraryDependencies ++= Seq(
  "org.webjars" % "font-awesome" % "5.13.0",							// Icons:CC, Fonts:SIL OFL, Code: MIT/Pro 5.13.0
  "org.webjars" % "material-design-icons" % "3.0.1",					// Apache 3.0.1
  "org.webjars.npm" % "ionicons" % "4.6.3",								// MIT 4.6.3
  "org.webjars.npm" % "twemoji" % "12.0.0" exclude("org.webjars.npm", "twemoji-parser")		// Code:MIT, Graphics:CC 12.0.0
)
// IcoFont																// MIT 1.3

// JasperReports library
libraryDependencies ++= Seq(
  "net.sf.jasperreports" % "jasperreports" % "6.10.0",					// LGPL 6.10.0
  "net.sf.jasperreports" % "jasperreports-fonts" % "6.10.0",
  "net.sf.barcode4j" % "barcode4j" % "2.1",								// Apache 2.1
  "net.sourceforge.barbecue" % "barbecue" % "1.5-beta1",				// BSD-style 1.5-beta1
  "org.apache.xmlgraphics" % "batik-bridge" % "1.12"
)

// Batch library
libraryDependencies ++= Seq(
  "javax.enterprise" % "cdi-api" % "2.0",
  "org.jboss.weld.se" % "weld-se" % "2.4.8.Final",						// Apache 2.4.8.Final
  "org.jboss.spec.javax.batch" % "jboss-batch-api_1.0_spec" % "1.0.2.Final",
  "org.jboss.marshalling" % "jboss-marshalling" % "2.0.9.Final",		// Apache 2.0.9.Final
  "org.jboss.logging" % "jboss-logging" % "3.4.1.Final",
  "org.jberet" % "jberet-core" % "1.3.4.Final",							// Eclipse Public 1.3.4.Final
  "org.jberet" % "jberet-support" % "1.3.4.Final",
  "org.jberet" % "jberet-se" % "1.3.4.Final",
  "org.wildfly.security" % "wildfly-security-manager" % "1.1.2.Final"	// LGPL(http://www.wildfly.org footer) 1.1.2.Final
)

dependencyOverrides ++= Seq(
  "org.webjars.npm" % "jquery" % "3.4.1",								// MIT 3.4.1
  "org.webjars.bower" % "jquery" % "3.4.1",
  "org.webjars.npm" % "marked" % "0.5.2",								// MIT 0.5.2
  "org.webjars.npm" % "typo-js" % "1.1.0"								// Modified BSD 1.1.0
)
excludeDependencies ++= Seq(
  ExclusionRule("xml-apis", "xml-apis")
//  "[GroupId]" % "[ArtifactId]"
)

// //////////

javacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-s", "generate",	// Create JPA metamodel
  "-parameters",
  "-Xlint:unchecked",
  "-Xlint:deprecation"
)

javaOptions in Test ++= Seq(
  "-Dconfig.file=conf/application_test.conf",
  "-Dwebdriver.gecko.driver=misc/geckodriver"	// v0.21.0 / Firefox 57
)

// Create JPA metamodel
unmanagedSourceDirectories in Compile ++= Seq(baseDirectory.value / "generate")
unmanagedSourceDirectories in Test ++= Seq(baseDirectory.value / "generate")

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

// Java project. Don't expect Scala IDE
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
// Use .class files instead of generated .scala files for views and routes
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)

// Not Create javadoc
sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false
// Create javadoc
//sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java"))

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

// CPD
//cpdLanguage := CpdLanguage.Java
//cpdReportType := CpdReportType.XML

// CheckStyle
checkstyleConfigLocation := CheckstyleConfigLocation.File("conf/checkstyle_checks.xml")
checkstyleXsltTransformations := {
  Some(Set(CheckstyleXSLTSettings(baseDirectory(_ / "conf" / "checkstyle-noframes.xml").value, target(_ / "checkstyle-report.html").value)))
}
