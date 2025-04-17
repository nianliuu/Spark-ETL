import Dependencies._

ThisBuild / scalaVersion     := "2.13.16"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "milvus-spark-connector-example",
    libraryDependencies ++= Seq(
      munit % Test,
      sparkCore,
      sparkSql,
      sparkCatalyst,
      "com.zilliz" %% "spark-connector" % "0.1.0-SNAPSHOT"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
