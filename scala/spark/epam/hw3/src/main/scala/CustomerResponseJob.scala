import java.nio.file.{Path, Paths}

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification._
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature._
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._

/**
  * Created by Nikolay_Vasilishin on 9/8/2016.
  */
object CustomerResponseJob {
  val featureName = """(?:\d{1,2}\)\s)(.*)(?:\:\s.*)""".r
  val FEATURE_DESCRIPTION_FILE = """PropertyDesciptionEN.txt"""
  val OBJECTS_FILE = """Objects.csv"""
  val TARGET_FILE = """Target.csv"""

  def zip(one: DataFrame, other: DataFrame, sqlContext: SQLContext): DataFrame = {
    val newSchema: StructType = StructType(Array.concat(one.schema.fields, other.schema.fields))
    sqlContext.createDataFrame(
      one.rdd.zipWithIndex().map(_.swap).join(other.rdd.zipWithIndex().map(_.swap)).map { case (l, (r1, r2)) => Row.merge(r1, r2) }, newSchema)
  }


  def main(args: Array[String]): Unit = {
    val (sc, sqlContext) = initContext()
    val path = """D:\education\Spark\session.dataset"""
    val (featureNames, features, label) = loadDataset(sqlContext, path)
    var df: DataFrame = null

    if(!Paths.get("df").toFile.exists()) {
      df = zip(features, label, sqlContext)


      println("Filling Nan values with mean of column...")

      /**
        * this <code>df.agg(avg(df(column))).head().getDouble(0).toInt</code>
        * terrible construction just for extracting mean of column values as int (for case when values are categorical)
        *
        */
      df.columns foreach { (column) => df = df.na.fill(df.agg(avg(df(column))).head().getDouble(0).toInt, Seq(column)) }
      df.cache()
      println(df.count())
      df.show()

      df.columns foreach { (column) => println((column, df.select(column).where(s"$column < 0").count())) }


      val assembler = new VectorAssembler()
        .setInputCols(features.schema.fieldNames)
        .setOutputCol("nsfeatures")
      df = assembler.transform(df).select("nsfeatures", "label")
      df.show(10)

      val scaler = new StandardScaler()
        .setInputCol("nsfeatures")
        .setOutputCol("sfeatures")
        .setWithStd(true)
      // Compute summary statistics by fitting the StandardScaler.
      val scalerModel = scaler.fit(df)
      // Normalize each feature to have unit standard deviation.
      df = scalerModel.transform(df)
      df.show()


      //
      val normalizer = new Normalizer()
        .setInputCol("sfeatures")
        .setOutputCol("nfeatures")
        .setP(2.0)

      df = normalizer.transform(df)
      df.show()

      val pca = new PCA()
        .setInputCol("nfeatures")
        .setOutputCol("features")
        .setK(10)
        .fit(df)

      df = pca.transform(df).select("features", "label")
      df.show()

      df.save("df")
    } else {
       df = sqlContext.load("df")
    }

    val splits = df.randomSplit(Array(0.8, 0.2))
    val train = splits(0)
    val test = splits(1)


    // Модели получились плохие
    // Тоже еще попробую поработать с фичами, сделать сетку параметров для оптимизации, поработать с pipeline
    /**
      * 1
      */
//        println("Building Naive Bayes model")
//        bayes(train, test)

        /**
          * 2
          */
        println("Building Decision Tree model")
        tree(df, train, test)

        /**
          * 3
          */
        println("Building Gradient Boosted Trees model")
        gradientBoostedTree(df, train, test)

        /**
          * 4
          */
        println("Building Linear Regression model")
        linearRegression(train)


    sc.stop()
  }

  def linearRegression(train: DataFrame): Unit = {
    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.5)
      .setElasticNetParam(0.3)

    // Fit the model
    val lrModel = lr.fit(train)

    // Print the coefficients and intercept for logistic regression
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
    val trainingSummary = lrModel.summary

    // Obtain the objective per iteration.
    val objectiveHistory = trainingSummary.objectiveHistory
    objectiveHistory.foreach(loss => println(loss))

    // Obtain the metrics useful to judge performance on test data.
    // We cast the summary to a BinaryLogisticRegressionSummary since the problem is a
    // binary classification problem.
    val binarySummary = trainingSummary.asInstanceOf[BinaryLogisticRegressionSummary]

    // Obtain the receiver-operating characteristic as a dataframe and areaUnderROC.
    val roc = binarySummary.roc
    roc.show()
    println(s"Area under ROC: ${binarySummary.areaUnderROC}")
  }

  def gradientBoostedTree(df: DataFrame, train: DataFrame, test: DataFrame): Unit = {
    val labelIndexer = new StringIndexer()
      .setInputCol("label")
      .setOutputCol("indexedLabel")
      .fit(df)
    // Automatically identify categorical features, and index them.
    // Set maxCategories so features with > 4 distinct values are treated as continuous.
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4)
      .fit(df)

    // Split the data into training and test sets (30% held out for testing).

    // Train a GBT model.
    val gbt = new GBTClassifier()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")
      .setMaxIter(10)

    // Convert indexed labels back to original labels.
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)

    // Chain indexers and GBT in a Pipeline.
    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, gbt, labelConverter))

    // Train model. This also runs the indexers.
    val model = pipeline.fit(train)

    // Make predictions.
    val predictions = model.transform(test)

    // Select example rows to display.
    predictions.show(5)

    // Select (prediction, true label) and compute test error.
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName("precision")
    val accuracy = evaluator.evaluate(predictions)
    println("Test Error = " + (1.0 - accuracy))

    //    val gbtModel = model.stages(2).asInstanceOf[GBTClassificationModel]
    //    println("Learned classification GBT model:\n" + gbtModel.toDebugString)

    predictions.select("predictedLabel", "label").show()
    println(predictions.where("predictedLabel = label").count())
    println(predictions.count())

    val metrics = new BinaryClassificationMetrics(predictions.select("predictedLabel", "label").rdd.map { case (r) =>
      (r.getString(0).toDouble, r.getDouble(1))
    })
    println(s"Area under ROC: ${metrics.areaUnderROC()}")
//    metrics.roc().
  }

  def tree(df: DataFrame, train: DataFrame, test: DataFrame): Unit = {
    // Index labels, adding metadata to the label column.
    // Fit on whole dataset to include all labels in index.
    val labelIndexer = new StringIndexer()
      .setInputCol("label")
      .setOutputCol("indexedLabel")
      .fit(df)
    // Automatically identify categorical features, and index them.
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4) // features with > 4 distinct values are treated as continuous.
      .fit(df)

    // Train a DecisionTree model.
    val dt = new DecisionTreeClassifier()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")

    // Convert indexed labels back to original labels.
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)

    // Chain indexers and tree in a Pipeline.
    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, dt, labelConverter))

    // Train model. This also runs the indexers.
    val model = pipeline.fit(train)

    // Make predictions.
    val predictions = model.transform(test)

    // Select example rows to display.
    predictions.select("predictedLabel", "label", "features").show(5)

    // Select (prediction, true label) and compute test error.
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName("precision")
    val accuracy = evaluator.evaluate(predictions)
    println("Test Error = " + (1.0 - accuracy))

    //    val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
    //    println("Learned classification tree model:\n" + treeModel.toDebugString)

    val metrics = new BinaryClassificationMetrics(predictions.select("predictedLabel", "label").rdd.map { case (r) =>
      (r.getString(0).toDouble, r.getDouble(1))
    })
    println(s"Area under ROC: ${metrics.areaUnderROC()}")
    metrics.roc() foreach println
  }

  def bayes(train: DataFrame, test: DataFrame): Unit = {
    val bayes = new NaiveBayes()
      .setLabelCol("label")
      .setFeaturesCol("features")
      .setModelType("multinomial")

    val model = bayes.fit(train)
    val predictions = model.transform(test)

    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("precision")
    val accuracy = evaluator.evaluate(predictions)

    println("Test Error = " + (1.0 - accuracy))
    val metrics = new BinaryClassificationMetrics(predictions.select("prediction", "label").rdd.map { case (r) => (r.getDouble(0), r.getDouble(1)) })
    println(s"Area under ROC: ${metrics.areaUnderROC()}")
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
