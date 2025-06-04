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
      scalapbRuntime,
      scalapbGrpc,
      grpcNetty,
      grpcProtobuf,
      grpcStub,
      awsS3,
      awsCore,
      hadoopAws
    ),
    // Use the local jar file from lib directory
    Compile / unmanagedJars += baseDirectory.value / "lib" / "spark-connector_2.13-0.1.0-SNAPSHOT.jar",
    
    // 本地运行配置 - 类似PySpark的直接运行模式
    run / fork := true,
    run / javaOptions ++= Seq(
      "-Xmx4g",
      "-Xms2g",
      "-Dspark.master=local[*]",
      "-Dspark.sql.warehouse.dir=target/spark-warehouse",
      "-Djava.io.tmpdir=target/tmp",
      // Java 17兼容性配置
      "--add-opens=java.base/java.lang=ALL-UNNAMED",
      "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
      "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
      "--add-opens=java.base/java.io=ALL-UNNAMED",
      "--add-opens=java.base/java.net=ALL-UNNAMED",
      "--add-opens=java.base/java.nio=ALL-UNNAMED",
      "--add-opens=java.base/java.util=ALL-UNNAMED",
      "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
      "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
      "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
      "--add-opens=java.base/sun.nio.cs=ALL-UNNAMED",
      "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
      "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED",
      "--add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED"
    ),
    
    // Debug配置 - 如果需要调试，取消注释下面这行
    // run / javaOptions += "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
