import AssemblyKeys._

Nice.javaProject

Nice.fatArtifactSettings

organization := "bio4j"

name := "neo4jdb"

description := "neo4jdb implementation of the Bio4j model"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq(
  "ohnosequences" % "bio4j-model" % "0.3.0-SNAPSHOT",
  "ohnosequences" % "bioinfo-util" % "1.3.0-SNAPSHOT"
)

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.1.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.2",
  "commons-beanutils" % "commons-beanutils" % "1.8.3",
  "commons-beanutils" % "commons-beanutils-core" % "1.8.3"
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
