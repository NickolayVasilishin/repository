import java.nio.file.Paths

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{VectorAssembler, VectorIndexer}
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.sql.types.{DoubleType, FloatType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Nikolay_Vasilishin on 9/8/2016.
  */
object CustomerResponseJob {
  val featureName = """(?:\d{1,2}\)\s)(.*)(?:\:\s.*)""".r
  val FEATURE_DESCRIPTION_FILE = """PropertyDesciptionEN.txt"""
  val OBJECTS_FILE = """Objects.csv"""
  val TARGET_FILE = """Target.csv"""

  def main(args: Array[String]): Unit = {
    val (sc, sqlContext) = initContext()
    val path = """D:\education\Spark\session.dataset"""
    val (featureNames, features, label) = loadDataset(sqlContext, path)

    val data = features.rdd.
      .map {
      case (rowLeft, rowRight) => Row.fromSeq(rowLeft.toSeq ++ rowRight.toSeq)
    }
    val schema = StructType(features.schema.fields ++ label.schema.fields)
    val df = sqlContext.createDataFrame(data, schema)
    //    df.show(5)
    features.schema.fieldNames
    val assembler = new VectorAssembler()
      .setInputCols(features.schema.fieldNames)
      .setOutputCol("features")
    val dfo = assembler.transform(df)
    dfo.show(5)

    val splits = dfo.randomSplit(Array(0.8, 0.2))
    val train = splits(0)
    val test = splits(1)

    val lr = new LinearRegression()
    val paramGrid = new ParamGridBuilder()
      .addGrid(lr.regParam, Array(0.1, 0.01))
      .addGrid(lr.fitIntercept)
      .addGrid(lr.elasticNetParam, Array(0.0, 0.5, 1.0))
      .build()
    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(lr)
      .setEvaluator(new RegressionEvaluator)
      .setEstimatorParamMaps(paramGrid)
      // 80% of the data will be used for training and the remaining 20% for validation.
      .setTrainRatio(0.8)



    val model = trainValidationSplit.fit(train)

    // Make predictions on test data. model is the model with combination of parameters
    // that performed best.
    model.transform(test)
      .select("features", "label", "prediction")
      .show()

  }

  def extractName(line: String): String = {
    line match {
      case featureName(n) => n
      case _ => null
    }
  }

  private def initContext(): (SparkContext, SQLContext) = {
    val conf = new SparkConf()
      .setAppName("Airlines Analyzer Application")
      .setMaster("local[6]")
    val sparkContext = new SparkContext(conf)

    (sparkContext, new SQLContext(sparkContext))
  }

  def loadDataset(sqlContext: SQLContext, path: String) = {
    val featureNames = sqlContext.sparkContext.textFile(Paths.get(path, FEATURE_DESCRIPTION_FILE).toString).map(extractName).filter(_ != null)
    //    val featureSchema = StructType(featureNames
    //     .map(extractName)
    //     .filter(_ != null)
    //     .map(StructField(_, FloatType, nullable = true))
    //     .take(50))

    val features = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "false")
      .option("delimiter", ";")
      .option("inferSchema", "true")
      //      .schema(featureSchema) //Бесполезновато
      .load(Paths.get(path, OBJECTS_FILE).toString)
    val label = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "false")
      .schema(StructType(Array(StructField("label", DoubleType, nullable = false))))
      .load(Paths.get(path, TARGET_FILE).toString)


    (featureNames, features, label)
  }

}
