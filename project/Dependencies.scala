import sbt._

object Dependencies {
  lazy val scalapbVersion = "0.11.3"
  lazy val sparkVersion = "3.5.3"
  // lazy val sparkVersion = "3.3.2"

  lazy val munit = "org.scalameta" %% "munit" % "0.7.29"
  lazy val sparkCore =
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided,test"
  lazy val sparkSql =
    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided,test"
  lazy val sparkCatalyst =
    "org.apache.spark" %% "spark-catalyst" % sparkVersion % "provided,test"
}
