name := "hw1"

version := "1.0"
organization := "com.epam"

scalaVersion := "2.10.5"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0" % "provided"
libraryDependencies += "com.holdenkarau" %% "spark-testing-base" % "1.6.0_0.3.3" % "provided"
libraryDependencies += "eu.bitwalker" % "UserAgentUtils" % "1.14"

resolvers += Resolver.mavenLocal