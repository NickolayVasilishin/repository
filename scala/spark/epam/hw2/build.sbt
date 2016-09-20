name := "hw2"

version := "1.0"
organization := "com.epam"

scalaVersion := "2.10.5"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0" //% "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.0" //% "provided"
libraryDependencies += "com.holdenkarau" %% "spark-testing-base" % "1.6.0_0.3.3" //% "provided"
libraryDependencies += "com.databricks" %% "spark-csv" % "1.5.0"

resolvers += Resolver.mavenLocal