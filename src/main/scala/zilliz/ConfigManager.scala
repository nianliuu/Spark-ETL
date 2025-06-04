package zilliz

import scala.io.Source
import java.io.File

case class MilvusConfig(uri: String, token: String, collectionName: String)
case class S3Config(endpoint: String, bucketName: String, rootPath: String, accessKey: String, secretKey: String)
case class SparkConfig(appName: String, master: String, driverMemory: String, executorMemory: String)
case class SchemaConfig(idField: String)

case class ApplicationConfig(
  milvus: MilvusConfig,
  s3: S3Config,
  spark: SparkConfig,
  schema: SchemaConfig
)

object ConfigManager {
  
  def loadConfig(configPath: Option[String] = None): ApplicationConfig = {
    val defaultPaths = List(
      "config/application.json",
      "src/main/scala/zilliz/config.json"
    )
    
    val configFile = configPath match {
      case Some(path) => new File(path)
      case None => defaultPaths.map(new File(_)).find(_.exists()).getOrElse {
        throw new RuntimeException(s"Configuration file not found. Tried: ${defaultPaths.mkString(", ")}")
      }
    }
    
    println(s"📁 Loading configuration from: ${configFile.getPath}")
    val jsonContent = Source.fromFile(configFile).mkString
    parseConfig(jsonContent)
  }
  
  private def parseConfig(jsonContent: String): ApplicationConfig = {
    // Simple JSON parsing function using regex
    def extractJsonValue(json: String, path: String): Option[String] = {
      val pattern = s""""$path"\\s*:\\s*"([^"]+)"""".r
      pattern.findFirstMatchIn(json).map(_.group(1))
    }
    
    def getRequired(path: String): String = {
      extractJsonValue(jsonContent, path).getOrElse {
        throw new RuntimeException(s"Required configuration '$path' not found in JSON")
      }
    }
    
    def getOptional(path: String, default: String): String = {
      extractJsonValue(jsonContent, path).getOrElse(default)
    }
    
    // Parse nested configuration
    val milvusConfig = MilvusConfig(
      uri = getRequired("uri"),
      token = getRequired("token"),
      collectionName = getRequired("collectionName")
    )
    
    val s3Config = S3Config(
      endpoint = getRequired("endpoint"),
      bucketName = getRequired("bucketName"),
      rootPath = getRequired("rootPath"),
      accessKey = getRequired("accessKey"),
      secretKey = getRequired("secretKey")
    )
    
    val sparkConfig = SparkConfig(
      appName = getOptional("appName", "MergeDataDemo"),
      master = getOptional("master", "local[*]"),
      driverMemory = getOptional("driverMemory", "4g"),
      executorMemory = getOptional("executorMemory", "2g")
    )
    
    val schemaConfig = SchemaConfig(
      idField = getOptional("idField", "id")
    )
    
    ApplicationConfig(milvusConfig, s3Config, sparkConfig, schemaConfig)
  }
  
  def printConfig(config: ApplicationConfig): Unit = {
    println("📋 Loaded Configuration:")
    println(s"🔗 Milvus URI: ${config.milvus.uri}")
    println(s"📦 Collection: ${config.milvus.collectionName}")
    println(s"🪣 S3 Bucket: ${config.s3.bucketName}")
    println(s"🔑 S3 Access Key: ${config.s3.accessKey.take(8)}...")
    println(s"🔐 S3 Secret Key: ${config.s3.secretKey.take(8)}...")
    println(s"⚡ Spark Master: ${config.spark.master}")
    println(s"💾 Driver Memory: ${config.spark.driverMemory}")
  }
} 