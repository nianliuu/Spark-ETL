package example

import org.apache.spark.sql.SparkSession
import com.zilliz.spark.connector.MilvusOption

object BinlogReadDemo extends App {
  val deleteFilePath = "data/read_binlog/delta_str_pk"
  val insertVarcharFilePath = "data/read_binlog/insert_varchar"
  val insertShortFilePath = "data/read_binlog/insert_short"
  val insertVecFilePath = "data/read_binlog/insert_float_vec"
  val minioPath = "insert_log/458155846610556542/458155846610556543/458155846610556627/101/458155846610556630"

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("BinlogReadDemo")
    .getOrCreate()

  val df = spark.read
    .format("milvusbinlog")
    .option(MilvusOption.ReaderPath, deleteFilePath)
    .option(MilvusOption.ReaderType, "delete")
    .load()
  df.show()

  val df2 = spark.read
    .format("milvusbinlog")
    .option(MilvusOption.ReaderPath, insertVarcharFilePath)
    .option(MilvusOption.ReaderType, "insert")
    .load()
  df2.show()

  val df3 = spark.read
    .format("milvusbinlog")
    .option(MilvusOption.ReaderPath, insertShortFilePath)
    .option(MilvusOption.ReaderType, "insert")
    .load()
  df3.show()

  val df4 = spark.read
    .format("milvusbinlog")
    .option(MilvusOption.ReaderPath, insertVecFilePath)
    .option(MilvusOption.ReaderType, "insert")
    .load()
  df4.show()

  // val df5 = spark.read
  //   .format("milvusbinlog")
  //   .option(MilvusOption.ReaderPath, minioPath)
  //   .option(MilvusOption.S3FileSystemTypeName, "s3a://")
  //   .option(MilvusOption.ReaderType, "insert")
  //   .load()
  // df5.show()

  spark.stop()
}