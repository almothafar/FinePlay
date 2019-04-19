// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.1")

// Web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.6")
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.10")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.2")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.12")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
//addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.2.2")

// //////////

// Eclipse IDE
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

// JaCoCo
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.1.0")
dependencyOverrides ++= Seq(
  "org.jacoco" % "org.jacoco.core" % "0.8.3",
  "org.jacoco" % "org.jacoco.report" % "0.8.3"
)

// PMD/CPD
//addSbtPlugin("com.github.sbt" % "sbt-cpd" % "2.0.0")

// CheckStyle
addSbtPlugin("com.etsy" % "sbt-checkstyle-plugin" % "3.1.1")
dependencyOverrides += "com.puppycrawl.tools" % "checkstyle" % "8.11"

// License report
addSbtPlugin("com.typesafe.sbt" % "sbt-license-report" % "1.2.0")
