package example

import org.apache.spark.sql.SparkSession
import com.zilliz.spark.connector.MilvusOption

object MilvusReadDemo extends App {
  val uri = "http://localhost:19530"
  val token = "root:Milvus"
  val collectionName = "hello_milvus"
  val milvusSegmentPath = "insert_log/458064902976430480/458064902976430481/458064902976430580"

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

  spark.stop()
}