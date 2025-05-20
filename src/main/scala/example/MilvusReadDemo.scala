package example

import org.apache.spark.sql.SparkSession
import com.zilliz.spark.connector.MilvusOption

object MilvusReadDemo extends App {
  val uri = "http://localhost:19530"
  val token = "root:Milvus"
  val collectionName = "hello_milvus"
  val milvusSegmentPath = "insert_log/458155846610556542/458155846610556543/458155846610556627"
  // a-bucket/files/insert_log/458155846609142108/458155846609142109
  val collection = "458155846610556542"
  val partition = "458155846610556543"
  val segment = "458155846610556627"

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("MilvusReadDemo")
    .getOrCreate()

  val df = spark.read
    .format("milvus")
    .option("path", milvusSegmentPath)
    .option("readerType", "insert")
    .option("s3.fs", "s3a://")
    .option(MilvusOption.URI_KEY, uri)
    .option(MilvusOption.TOKEN_KEY, token)
    .option(MilvusOption.MILVUS_COLLECTION_NAME, collectionName)
    .load()
  df.show()

  val df2 = spark.read
    .format("milvus")
    .option("collection", collection)
    .option("partition", partition)
    .option("segment", segment)
    .option("readerType", "insert")
    .option("s3.fs", "s3a://")
    .option(MilvusOption.URI_KEY, uri)
    .option(MilvusOption.TOKEN_KEY, token)
    .option(MilvusOption.MILVUS_COLLECTION_NAME, collectionName)
    .load()
  df2.show()

  val df3 = spark.read
    .format("milvus")
    .option("collection", collection)
    .option("readerType", "insert")
    .option("s3.fs", "s3a://")
    .option(MilvusOption.URI_KEY, uri)
    .option(MilvusOption.TOKEN_KEY, token)
    .option(MilvusOption.MILVUS_COLLECTION_NAME, collectionName)
    .load()
  df3.show()

  val df4 = spark.read
    .format("milvus")
    .option("collection", collection)
    .option("readerType", "insert")
    .option("s3.fs", "s3a://")
    .option(MilvusOption.URI_KEY, uri)
    .option(MilvusOption.TOKEN_KEY, token)
    .option(MilvusOption.MILVUS_COLLECTION_NAME, collectionName)
    .load()
  df4.show()

  spark.stop()
}