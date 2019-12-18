import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

object TestSpark_wordCount  {

  def main(args: Array[String]) = {
    //Start the Spark context
    val conf = new SparkConf()
      .setAppName("WordCount")
      .setMaster("local")
    val sc = new SparkContext(conf)
    //Read some example file to a test RDD
    val test = sc.textFile("/Users/sree/SparkScalaOutput/first.txt") //need to provide the input path of the file
    test.flatMap { line => //for each line
      line.split(" ") //split the line in word by word.
    }
      .map { word => //for each word
        (word, 1) //Return a key/value tuple, with the word as key and 1 as value
      }
      .reduceByKey(_ + _) //Sum all of the value with same key
      .saveAsTextFile("/Users/sree/SparkScalaOutput/second2.txt") //Save to a text file
    //Stop the Spark context
    sc.stop
  }

  println("Hello World")

}
