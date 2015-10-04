name := "address-book-example"
 
version := "0.1"

scalaVersion := "2.11.7"

scalacOptions += "-deprecation"

// Typesafe config - https://github.com/typesafehub/config
libraryDependencies += "com.typesafe" % "config" % "1.3.0"

// scala io
libraryDependencies += "com.github.scala-incubator.io" % "scala-io-core_2.11" % "0.4.3-1"

libraryDependencies += "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3-1"

// scalatest
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("addressbook.AddressBook")
