# bio4j-neo4j

Welcome to the Neo4j implementation of Bio4j!
In order to get general information regarding Bio4j project please go to our [main repository](https://github.com/bio4j/bio4j).

## Getting started

You've got two main options:

### Import Bio4j from scratch
Please visit the section [Importing Bio4j](/docs/importing_bio4j_neo4j.markdown) if you want to import your own Bio4j DB from scratch.

### AWS _(Amazon Web Services)_ users 

* [**AWS Releases**](/docs/aws_releases.markdown) You can find here the corresponding information for the release you're interested in.
* [**CloudFormation templates**](/docs/cloudformation_neo4j.markdown) Check this section to see how you can benefit from [Cloud Formation](http://aws.amazon.com/cloudformation/) service together with Bio4j.


### Using a query language

* [**Cypher** cheat sheet](docs/cypher_cheat_sheet.markdown)
* [**Gremlin** cheat sheet](docs/gremlin_cheat_sheet.markdown)
 


### SBT dependency

To use it in your sbt-project, add this to `build.sbt`:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "bio4j" % "neo4jdb" % "0.2.0"
```
