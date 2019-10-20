package com.bjsxt.scalaspark.core.actions

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.spark_project.jetty.server.Authentication.User

object test22 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("test").getOrCreate()
    val df = spark.read.schema(ScalaReflection.schemaFor[User].dataType.asInstanceOf[StructType])
      .csv("./data/csv").toDF("id","name","interest","score","date")

    df.printSchema()
    df.createOrReplaceTempView("mytable")
    val transDf = spark.sql(
      """
        | select id ,concat('\'',name,'\'') as concatName,
        |   regexp_replace(interest, '(\"{2})', '\'') as repalceInterest,score,date_format(date,'yyyy/MM/dd') as formatDate
        | from mytable
      """.stripMargin).createOrReplaceTempView("mytable1")
    val transDf1 = spark.sql(
      """
        | select id ,concatName,concat('\'',regexp_replace(repalceInterest,'(\"{1})',''),'\''),score,formatDate from mytable1
      """.stripMargin)
    transDf1.show()
    transDf1.write.mode(SaveMode.Overwrite).option("delimiter","\t").option("inferSchema", "true").option("quote", "").csv("./result/sss")

  }

}
