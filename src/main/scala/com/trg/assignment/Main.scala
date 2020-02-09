package com.trg.assignment

import org.apache.spark.sql.functions._
import org.elasticsearch.spark.sql._

object Main extends App {

  val spark = org.apache.spark.sql.SparkSession.builder
    .appName("SparkTRG")
    .getOrCreate;

  val dfStreet = spark.read
    .option("header", "true")
    .csv("/opt/bitnami/spark/work/files/*/*-street.csv")

  val dfOutcomes = spark.read
    .option("header", "true")
    .csv("/opt/bitnami/spark/work/files/*/*-outcomes.csv")
    .select("Crime ID", "Outcome type")


  spark.udf.register("get_street_name",
    (path: String) => path.split("/").last.split("\\.").head.substring(8)
      .replace("street", "").replace("-", " "))

  val dfStreet_filtered = dfStreet.withColumn("districtName", callUDF("get_street_name", input_file_name()))
    .select("Crime ID", "districtName", "Longitude", "Latitude", "Crime type", "Last outcome category")

  dfStreet_filtered.join(dfOutcomes, "Crime ID")
    .na.fill("Outcome type", Array("Last outcome category"))
    .drop("Last outcome category")
    .withColumnRenamed("Crime ID", "crimeID")
    .withColumnRenamed("Longitude", "longitude")
    .withColumnRenamed("Latitude", "latitude")
    .withColumnRenamed("Crime type", "crimeType")
    .withColumnRenamed("Outcome type", "lastOutcome")
    .saveToEs("/police_uk/outcomes")

  spark.close()

}
