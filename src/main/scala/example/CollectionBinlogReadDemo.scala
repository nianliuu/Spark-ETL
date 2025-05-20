package example

import org.apache.spark.sql.SparkSession

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