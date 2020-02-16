name := "termite-store"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "io.suzaku" %% "boopickle-shapeless" % "1.3.1",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.mockito" % "mockito-scala-scalatest_2.12" % "1.11.2" % "test"
)