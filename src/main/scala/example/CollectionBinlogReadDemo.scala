package example

import org.apache.spark.sql.SparkSession
import com.zilliz.spark.connector.MilvusOption

object CollectionBinlogReadDemo extends App {
  val collection = "458155846610556542"
  val partition = "458155846610556543"
  val segment = "458155846610556627"
  val field = "101"
  // val minioPath = "data" // 支持目录/文件读取

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("BinlogReadDemo")
    .getOrCreate()

  val df = spark.read
    .format("milvusbinlog")
    .option(MilvusOption.S3FileSystemTypeName, "s3a://")
    .option(MilvusOption.MilvusCollectionID, collection)
    .option(MilvusOption.ReaderType, "delete")
    .load()
  df.show()

  val df2 = spark.read
    .format("milvusbinlog")
    .option(MilvusOption.S3FileSystemTypeName, "s3a://")
    .option(MilvusOption.MilvusCollectionID, collection)
    .option(MilvusOption.MilvusPartitionID, partition)
    // .option(MilvusOption.MilvusSegmentID, segment)
    .option(MilvusOption.MilvusFieldID, field)
    .option(MilvusOption.ReaderType, "insert")
    .load()
  df2.show()

  spark.stop()
}