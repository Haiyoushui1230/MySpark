package com.bjsxt.scalaspark.core.actions

import java.io.{File, FileOutputStream, OutputStreamWriter, PrintWriter}


import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}





object Test extends java.io.Serializable{
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("foreach").setMaster("local")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("./data/csv.txt")

    val worlds: RDD[String] = lines.map(line=>
    {
      var str=""
      val array: Array[String] = line.split(",")
      val length: Int = line.split(",").length

     for (i <- 2 until  (array.length-2)){
         str += array(i)+","
     }
      str=str.substring(0,str.length-1)//去掉末尾的逗号
      if(str.contains(",")){
        str="\""+str+"\""
      }
      if (str.contains("\"")){
        str=str.replaceAll("\"","\"\"")
      }
      line.split(",")(0) +"#"+ line.split(",")(1)+"#"+str+"#" +line.split(",")(length-2)+"#"+ line.split(",")(length-1)

    })
   // worlds.foreach(println)

    val pathFileName = "./data/output.txt"
    //创建文件
    val createFile = CreateFile(pathFileName)

    //向文件中写入数据 需要的对象
    val file = new File(pathFileName)
    val fos = new FileOutputStream(file, true)
    val osw = new OutputStreamWriter(fos, "UTF-8")
    val pw = new PrintWriter(osw)

    worlds.foreach(world=>{
      val content:String = world
      pw.write(content + "\n")
    })
  }

  def CreateFile(pathFileName: String): Boolean = {
    val file = new File(pathFileName)
    if (file.exists) file.delete
    val createNewFile:Boolean = file.createNewFile()
    System.out.println("create file " + pathFileName + " success!")
    createNewFile
  }
}
