Nice.javaProject

name := "bio4j-neo4jdb"

description := "bio4j-neo4jdb project"

organization := "bio4j"

bucketSuffix := "era7.com"

libraryDependencies += "ohnosequences" % "bio4j-model" % "0.2.0"

libraryDependencies += "ohnosequences" % "bioinfo-util" % "1.2.0"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.1.2"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.2"
