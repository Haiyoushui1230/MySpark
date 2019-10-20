package com.bjsxt.javaspark;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;



import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

public class testES {
    public static void main(String[] args) throws Exception {

        //利用sparksql创建Context
        SparkSession spark = SparkSession.builder()
                .appName("JavaWordCount")
                .master("local[2]")//如果是本地环境可以是local[n]，n是线程数，必须大于1
                //    .master("spark://192.168.7.51:7077")//如果是集群模式，需要指定master地址
                .config("spark.es.nodes", "192.168.7.51")//指定es地址
                .config("spark.es.port", "9200")//指定es端口号
                .getOrCreate();

        //指定本地文件路径，如果spark是集群模式，需要每个节点上对应路径下都要有此文件。或者使用hdfs。
        JavaRDD<String> lines = spark.read().textFile("/path/to/test.txt").javaRDD();

        //切分单词
//        JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());
//
//        //转PairRDD
//        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));
//
//        //统计单词数
//        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);
//
//        //全角转半角
//        JavaPairRDD<String, String> ones1 = words.mapToPair(s -> new Tuple2<>(s, BCConvert.bj2qj(s)));

        //将结果保存到文件系统
        // counts.saveAsTextFile("/Users/liubowen/sparkstream/out");

        //返回结果
        // List<Tuple2<String, Integer>> output = counts.collect();

        //循环结果
        // for (Tuple2<?,?> tuple : output) {
        // 		System.out.println(tuple._1() + ": " + tuple._2());
        // }

        //将结果保存到es
        //JavaEsSpark.saveJsonToEs(lines, "/spark/doc");

        // List list = ones.collect();
        // System.out.println(list);
        spark.stop();
    }

}
