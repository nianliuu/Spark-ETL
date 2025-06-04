import sbt._

object Dependencies {
  lazy val scalapbVersion = "0.11.3"
  lazy val sparkVersion = "3.5.3"
  // lazy val sparkVersion = "3.3.2"

  lazy val munit = "org.scalameta" %% "munit" % "0.7.29"
  
  // 改为compile模式，支持本地直接运行（类似PySpark）
  lazy val sparkCore =
    "org.apache.spark" %% "spark-core" % sparkVersion
  lazy val sparkSql =
    "org.apache.spark" %% "spark-sql" % sparkVersion
  lazy val sparkCatalyst =
    "org.apache.spark" %% "spark-catalyst" % sparkVersion
  
  // Required dependencies for Milvus connector
  lazy val scalapbRuntime = "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion
  lazy val scalapbGrpc = "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion
  lazy val grpcNetty = "io.grpc" % "grpc-netty-shaded" % "1.57.2"
  lazy val grpcProtobuf = "io.grpc" % "grpc-protobuf" % "1.57.2"
  lazy val grpcStub = "io.grpc" % "grpc-stub" % "1.57.2"
  
  // AWS SDK for S3 connectivity checking
  lazy val awsS3 = "software.amazon.awssdk" % "s3" % "2.21.29"
  lazy val awsCore = "software.amazon.awssdk" % "aws-core" % "2.21.29"
  lazy val hadoopAws = "org.apache.hadoop" % "hadoop-aws" % "3.3.4"
}
