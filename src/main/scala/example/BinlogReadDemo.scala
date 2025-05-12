package example

import org.apache.spark.sql.SparkSession

object BinlogReadDemo extends App {
  val filePath = "data/read_binlog/delta_str_pk"

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("BinlogReadDemo")
    .getOrCreate()

  val df = spark.read
    .format("milvusbinlog")
    .option("path", filePath)
    .load()

  df.show()

  spark.stop()
}