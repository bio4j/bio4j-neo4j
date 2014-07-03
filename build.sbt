import AssemblyKeys._

Nice.javaProject

javaVersion := "1.8"

fatArtifactSettings

organization := "bio4j"

name := "neo4jdb"

description := "Neo4jdb implementation of the Bio4j model"

bucketSuffix := "era7.com"


libraryDependencies ++= Seq(
  "bio4j" % "bio4j" % "0.12.0-SNAPSHOT",
  "com.amazonaws" % "aws-java-sdk" % "1.8.3",
  "ohnosequences" % "bioinfo-util" % "1.3.0",
  "org.apache.httpcomponents" % "httpclient" % "4.3.4",
  "org.apache.commons" % "commons-math" % "2.2",
  "junit" % "junit" % "3.8.1" % "test",
  "net.sf.opencsv" % "opencsv" % "2.3"
)

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.1.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.2",
  "commons-beanutils" % "commons-beanutils" % "1.8.3",
  "commons-beanutils" % "commons-beanutils-core" % "1.8.3",
  "commons-codec" % "commons-codec" % "1.6",
  "net.sf.opencsv" % "opencsv" % "2.3"
)

// fat jar assembly settings
mainClass in assembly := Some("com.bio4j.neo4jdb.programs.ImportNeo4jDB")

assemblyOption in assembly ~= { _.copy(includeScala = false) }

mergeStrategy in assembly ~= { old => {
    case PathList("META-INF", "CHANGES.txt")                     => MergeStrategy.rename
    case PathList("META-INF", "LICENSES.txt")                    => MergeStrategy.rename
    case PathList("org", "apache", "commons", "collections", _*) => MergeStrategy.first
    case x                                                       => old(x)
  }
}
