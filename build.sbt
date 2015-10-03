name := "address-book-example"
 
version := "0.1"

scalaVersion := "2.11.7"

scalacOptions += "-deprecation"

// reactive mongo needs play iteratees
resolvers += "typesafe repo" at "http://repo.typesafe.com/typesafe/releases/"

// scala swing - {https://github.com/scala/scala-module-dependency-sample}
// add scala-xml dependency when needed (for Scala 2.11 and newer) in a robust way
// this mechanism supports cross-version publishing
// taken from: http://github.com/scala/scala-module-dependency-sample
libraryDependencies := {
    CrossVersion.partialVersion(scalaVersion.value) match {
        // if scala 2.11+ is used, add dependency on scala-xml module
        case Some((2, scalaMajor)) if scalaMajor >= 11 =>
            libraryDependencies.value ++ Seq(
                "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
                "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
                "org.scala-lang.modules" %% "scala-swing" % "1.0.1")
        case _ =>
            // or just libraryDependencies.value if you don't depend on scala-swing
            libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
    }
}

// scala actors
libraryDependencies += "org.scala-lang" % "scala-actors" % "2.11.6"

// Typesafe config - https://github.com/typesafehub/config
libraryDependencies += "com.typesafe" % "config" % "1.3.0"

// JSON - argonaut
libraryDependencies += "io.argonaut" %% "argonaut" % "6.0.4"

// scala io
libraryDependencies += "com.github.scala-incubator.io" % "scala-io-core_2.11" % "0.4.3-1"

libraryDependencies += "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3-1"

// reactive mongo
libraryDependencies += "org.reactivemongo" %% "reactivemongo" % "0.11.6"

// netty
libraryDependencies += "io.netty" % "netty-all" % "5.0.0.Alpha1"

// lucene - todo - upgrade to 5.3.0
libraryDependencies += "org.apache.lucene" % "lucene-core" % "5.1.0"
libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "5.1.0"
libraryDependencies += "org.apache.lucene" % "lucene-queryparser" % "5.1.0"
libraryDependencies += "org.apache.lucene" % "lucene-queries" % "5.1.0"

// scalatest
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.28.0" % "test"

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("Main")
