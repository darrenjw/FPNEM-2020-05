name := "basics"

version := "0.1-SNAPSHOT"

scalacOptions ++= Seq(
  "-unchecked", "-deprecation", "-feature"
)

libraryDependencies  ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.0-SNAP13" % "test",
  "com.cibo" %% "evilplot" % "0.6.3", // 0.7.0
  "com.cibo" %% "evilplot-repl" % "0.6.3", // 0.7.0
  "org.scalameta" %% "mdoc" % "2.1.5",
  "org.scalameta" %% "mdoc-docs" % "2.1.5",
  "org.scalanlp" %% "breeze" % "1.0",
  // "org.scalanlp" %% "breeze-viz" % "1.0",
  "org.scalanlp" %% "breeze-natives" % "1.0"
)

enablePlugins(MdocPlugin)

resolvers += Resolver.bintrayRepo("cibotech", "public") // EvilPlot

resolvers ++= Seq(
  "Sonatype Snapshots" at
    "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at
    "https://oss.sonatype.org/content/repositories/releases/"
)

scalaVersion := "2.12.10"

