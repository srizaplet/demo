import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.collection.immutable.Map

object ScalaJsonSpark {

  def main(args: Array[String]) = {

    //Spark Session
    val spark = SparkSession.builder()
      .master("local")
      .appName("example of SparkSession")
      .getOrCreate()


    // json inputs

    val rec1: String =
      """{
    "visitorId": "v1",
    "products": [{
         "id": "i1",
         "interest": 0.68
    }, {
         "id": "i2",
         "interest": 0.42
    }]
}"""

    val rec2: String =
      """{
    "visitorId": "v2",
    "products": [{
         "id": "i1",
         "interest": 0.78
    }, {
         "id": "i3",
         "interest": 0.11
    }]
}"""

    val vData = Seq(rec1,rec2)
    val productIdToNameMap = Map("i1" -> "Nike Shoes", "i2" -> "Umbrella", "i3" -> "Jeans")

    import spark.implicits._

    //input data converted to DF
    val visitorDataReadDF = spark.read.json(vData.toDS).toDF()
    val productIdToMapDF = productIdToNameMap.toSeq.toDF("id","name")

    //Explode the Data for products
    val visitorDataExplodedDF = visitorDataReadDF.withColumn("productsline", explode($"products")).
      select($"visitorId",$"products",$"productsline.id",$"productsline.interest")

    println("Sree here is the data")

    visitorDataExplodedDF.show(truncate = false)

    // Join the data with corresponding id to get the names of the  product
    val joinVisitorDataWithNameDF = visitorDataExplodedDF.join(productIdToMapDF, "id")

    // Group by data on Visitor id for the required output
    val enrichedVisitorDataDF = joinVisitorDataWithNameDF.groupBy("visitorId").agg(collect_list(struct($"id", $"name", $"interest")) as  "products")


    enrichedVisitorDataDF.show(false)

    // Enriched Data to Json as expected
    enrichedVisitorDataDF.toJSON.show(false)

  }
}
