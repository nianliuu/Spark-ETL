package zilliz

import com.zilliz.spark.connector.MilvusOption
import org.apache.spark.sql.SparkSession

object MergeData extends App {
  
  try {
    // Load configuration from file or command line
    val config = if (args.length >= 1) {
      // If config path provided as argument
      ConfigManager.loadConfig(Some(args(0)))
    } else {
      // Use default config file locations
      ConfigManager.loadConfig()
    }
    
    // Print loaded configuration
    ConfigManager.printConfig(config)
    
    // Create Spark session with configuration
    val spark = SparkSession
      .builder()
      .master(config.spark.master)
      .appName(config.spark.appName)
      .config("spark.driver.memory", config.spark.driverMemory)
      .config("spark.executor.memory", config.spark.executorMemory)
      .config("spark.hadoop.fs.s3a.access.key", config.s3.accessKey)
      .config("spark.hadoop.fs.s3a.secret.key", config.s3.secretKey)
      .config("spark.hadoop.fs.s3a.endpoint", config.s3.endpoint)
      .config("spark.hadoop.fs.s3a.path.style.access", "false")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()

    try {
      println("🚀 Starting to read Milvus data...")
      
      val df = spark.read
        .format("milvus")
        .option(MilvusOption.ReaderType, "insert")
        .option(MilvusOption.S3FileSystemTypeName, "s3a://")
        .option(MilvusOption.MilvusUri, config.milvus.uri)
        .option(MilvusOption.MilvusToken, config.milvus.token)
        .option(MilvusOption.MilvusCollectionName, config.milvus.collectionName)
        .option(MilvusOption.S3Endpoint, config.s3.endpoint)
        .option(MilvusOption.S3BucketName, config.s3.bucketName)
        .option(MilvusOption.S3RootPath, config.s3.rootPath)
        .option(MilvusOption.S3AccessKey, config.s3.accessKey)
        .option(MilvusOption.S3SecretKey, config.s3.secretKey)
        .load()
      
      println("✅ Successfully loaded Milvus data!")
      df.show()
      println(s"📊 Total record count: ${df.count()}")

    } catch {
      case ex: Exception =>
        println(s"❌ Error reading Milvus data: ${ex.getMessage}")
        ex.printStackTrace()
    } finally {
      spark.stop()
    }
    
  } catch {
    case ex: Exception =>
      println(s"❌ Configuration error: ${ex.getMessage}")
      println("💡 Usage:")
      println("  sbt run                          # Use default config file")
      println("  sbt run config/custom.json       # Use custom config file")
      System.exit(1)
  }
}