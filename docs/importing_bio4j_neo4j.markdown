Please follow this step-by-step guide to build your own Bio4j database from scratch including all modules.

_(If you are not using AWS please go directly to step 6)_

### 1. Launch a new AWS instance (preferably m2.2xlarge)
  
### 2. Create a EBS volume of 1 TB
> _You would only need so much space in the case where you're importing all modules and you are not deleting input files once a module import process is done._

### 3. Attach the volume to the instance

### 4. Create folder `/mnt/bio4j_volume` in the AWS instance

``` bash
 mkdir /mnt/bio4j_volume 
```
 
### 5. Mount volume

```  bash
mkfs -t ext3 /dev/sdh
mount /dev/sdh /mnt/bio4j_volume
```

### 6. Download and install official Java 7 JDK

Here's the link to the official website describing how to [install JDK 7 for Linux platforms](http://docs.oracle.com/javase/7/docs/webnotes/install/linux/linux-jdk.html)

### 7. Get the last versions of the following files:

- [neo4jdb-0.x.x-fat.jar](https://github.com/bio4j/neo4jdb/releases) _(get the correponding jar file for the release you want to use)_
- [executionsBio4j.xml](/executionsBio4j.xml) _(this file can be customized in order to just import a sub-set of the data available)_
- [batchInserter.properties](/batchInserter.properties)  _**IMPORTANT** -->(this file should be changed according to the amount of RAM memory available in your machine)_
- [uniprotData.xml](/uniprotData.xml) _This file will only be used in the case where you want to import Uniprot module. (Set the boolean flags included in the XML file to true/false depending on your choice of data you want to import from Uniprot)_
   
### 8. Download and execute the following bash script:

- [DownloadAndPrepareBio4jSources.sh](https://raw.githubusercontent.com/bio4j/neo4jdb/master/docs/DownloadAndPrepareBio4jSources.sh)

This script download and decompresses all the sources needed to build a full Bio4j DB (Swissprot, TrEMBL, GO, RefSeq, etc).

(Once the script has finished, make sure that the final file names coincide with those specified in your XML file executionsBio4j.xml).

### 8b. Tuning executions.xml file

**Bio4j is divided in modules** and so it is the importing process, that way you don't have to import the whole thing in the case where you are interested only in some of the data sources _( **Gene Ontology**, **NCBI taxonomy tree**, etc...)_. However you must be coherent when importing a set modules, that's to say, for example it's not possible to import the **Uniref clusters** without previously importing **Uniprot KB** - otherwise there wouldn't be proteins to link to in the clusters!

Here is a diagram showing what resources must be present before importing others:

![Bio4j modules dependencies](https://github.com/bio4j/Bio4j/raw/master/ModuleDependencies.png)

In order to customize the modules that will be imported you have to modify the file **executionsBio4j.xml**.
Let's imagine that we want a database including only the Gene Ontology, NCBI taxonomy tree and Uniprot KB (only Swiss-prot entries). 
The corresponding executions.xml file should look like this:

> Don't forget to include InitBio4jDB program in the case where you want to have Uniprot KB module in your database.


``` xml
<scheduled_executions>
  <execution>
    <class_full_name>com.bio4j.neo4jdb.programs.InitBio4jDB</class_full_name>
    <arguments>
      <argument>bio4jdb</argument>
      <argument>batchInserter.properties</argument>
    </arguments>
  </execution>
  <execution>
    <class_full_name>com.bio4j.neo4jdb.programs.ImportEnzymeDB</class_full_name>
    <arguments>
      <argument>enzyme.dat</argument>
      <argument>bio4jdb</argument>
      <argument>batchInserter.properties</argument>
    </arguments>
  </execution>
  <execution>
    <class_full_name>com.bio4j.neo4jdb.programs.ImportGeneOntology</class_full_name>
    <arguments>
      <argument>go.xml</argument>
      <argument>bio4jdb</argument>
      <argument>batchInserter.properties</argument>
    </arguments>
  </execution>
  <execution>
    <class_full_name>com.bio4j.neo4jdb.programs.ImportUniprot</class_full_name>
    <arguments>
      <argument>uniprot_sprot.xml</argument>
      <argument>bio4jdb</argument>
      <argument>batchInserter.properties</argument>
    </arguments>
  </execution>
  <execution>
    <class_full_name>com.bio4j.neo4jdb.programs.ImportNCBITaxonomy</class_full_name>
    <arguments>
      <argument>nodes.dmp</argument>
      <argument>names.dmp</argument>
      <argument>merged.dmp</argument>
      <argument>bio4jdb</argument>
      <argument>true</argument>
    </arguments>
  </execution>
</scheduled_executions>
```

> Check how ImportEnzymeDB program was included in this example - following what was just shown in the module dependencies schema.

One more thing, since we are only interested in some of the data sources, it wouldn't make much sense to download the ones we were not going to include. We can avoid that simply removing the lines we don't need from the shell script _DownloadAndPrepareBio4jSources.sh_. For our example it should look something like this:

``` bash
curl 'ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot.xml.gz' -o uniprot_sprot.xml.gz
curl 'http://archive.geneontology.org/latest-termdb/go_daily-termdb.obo-xml.gz' -o go.xml.gz
curl 'ftp://ftp.expasy.org/databases/enzyme/enzyme.dat' -o enzyme.dat
gzip -d *.gz
curl 'ftp://ftp.ncbi.nih.gov/pub/taxonomy/taxdump.tar.gz' -o taxdump.tar.gz
tar -xvf taxdump.tar.gz
```

### 9. Launch importing process in background

```  bash
java -d64 -Xmx30G -jar neo4jdb-0.x.x-fat.jar executionsBio4j.xml &
```

Different log files will be created at jar folder level regarding the progress of the data importation
