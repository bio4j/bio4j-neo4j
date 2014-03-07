import AssemblyKeys._

Nice.javaProject

fatArtifactSettings

organization := "bio4j"

name := "neo4jdb"

description := "Neo4jdb implementation of the Bio4j model"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq(
  "bio4j" % "bio4j" % "0.11.0-SNAPSHOT",
  "org.neo4j" % "neo4j" % "1.9.6",
  "com.amazonaws" % "aws-java-sdk" % "1.6.12",
  "org.apache.httpcomponents" % "httpclient" % "4.2",
  "org.apache.commons" % "commons-math" % "2.2",
  "org.jdom" % "jdom" % "2.0.2"
)

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.1.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.2",
  "commons-beanutils" % "commons-beanutils" % "1.8.3",
  "commons-beanutils" % "commons-beanutils-core" % "1.8.3",
  "commons-codec" % "commons-codec" % "1.6"
)

// fat jar assembly settings
mainClass in assembly := Some("com.ohnosequences.bio4j.neo4j.programs.ImportNeo4jDB")

assemblyOption in assembly ~= { _.copy(includeScala = false) }

mergeStrategy in assembly ~= { old => {
    case PathList("META-INF", "CHANGES.txt")                     => MergeStrategy.rename
    case PathList("META-INF", "LICENSES.txt")                    => MergeStrategy.rename
    case PathList("org", "apache", "commons", "collections", _*) => MergeStrategy.first
    case x                                                       => old(x)
  }
}
