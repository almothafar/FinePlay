// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.6")

// Web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.9")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.2")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.11")

// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
//addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.1.0")
//libraryDependencies += "org.javassist" % "javassist" % "3.21.0-GA"

// //////////

// Eclipse IDE
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")

// JaCoCo
addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.3.0")

// FindBugs
addSbtPlugin("de.johoop" % "findbugs4sbt" % "1.4.0")

// PMD/CPD
addSbtPlugin("de.johoop" % "cpd4sbt" % "1.2.0")

// CheckStyle
addSbtPlugin("com.etsy" % "sbt-checkstyle-plugin" % "3.0.0")

// License report
addSbtPlugin("com.typesafe.sbt" % "sbt-license-report" % "1.2.0")
