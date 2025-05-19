package example

import org.apache.spark.sql.SparkSession

object CollectionBinlogReadDemo extends App {
  val collection = "458064902976430480"
  val partition = "458064902976430481"
  val segment = "458064902976430580"
  val field = "100"
  // val minioPath = "data" // 支持目录/文件读取

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("BinlogReadDemo")
    .getOrCreate()

  val df = spark.read
    .format("milvusbinlog")
    .option("s3.fs", "s3a://")
    .option("collection", collection)
    .option("readerType", "delete")
    .load()
  df.show()

  val df2 = spark.read
    .format("milvusbinlog")
    .option("s3.fs", "s3a://")
    .option("collection", collection)
    .option("partition", partition)
    // .option("segment", segment)
    .option("field", field)
    .option("readerType", "insert")
    .load()
  df2.show()

  spark.stop()
}