name := "cavy"

version := "1.0"

scalaVersion := "2.10.1"

resolvers += "Morphia repository" at "http://morphia.googlecode.com/svn/mavenrepo/"

libraryDependencies += "com.google.code.findbugs" % "jsr305" % "1.3.9"

libraryDependencies += "com.google.code.morphia" % "morphia" % "0.99"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "2.7.2"

libraryDependencies += "com.google.guava" % "guava" % "14.0.1"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.5"

libraryDependencies += "ch.qos.logback" % "logback-core" % "1.0.11"

libraryDependencies += "junit" % "junit" % "4.10" % "test"
