## Instructions on how to release to Sonatype repository
I'm adding some random notes here, so I don't have to discover
all this from scratch when I'm doing this again.

## Building and uploading

IIRC I previously succeeded in uploading to Sonatype
with Gradle. However, I don't know how to setup cross
compiling with Gradle. Due to this 0.2 and 0.3 were built with SBT.

It seems that JDK 8 is needed due to certs problem in new JDKs
(failed with SDK 9.0.4-open)
https://stackoverflow.com/a/53246821/1148030

    sdk use java 8u151-oracle
    
Here are the SDK versions I used for 0.3:

    $ sdk current
    
    Using:
    
    gradle: 5.4.1
    java: 8u151-oracle
    sbt: 1.2.8
    scala: 2.12.8 


JavaFX and tests don't work. Probably lot of work with JafaFX 12
based on not being too simple with other tools either.

    rm -rf src/test
    
Make a temp git stage to make SBT happy (edit history later or start with a branch)

    brew install gnupg@1.4

Gpg 1.4 needed by SBT

    ln -sf /usr/local/Cellar/gnupg\@1.4/1.4.23_1/bin/gpg1 /usr/local/bin/gpg
    
Finally

    sbt release    

SBT will ask for the GPG passphrase multiple times
in a confusing way. Just keep pasting it.

Finally restore the correct new gnupg version:

    brew unlink gnupg && brew link --overwrite gnupg

## Publishing in Sonatype Nexus

  * https://oss.sonatype.org/index.html#view-repositories;releases~browsestorage~/fi/pelam/pelam-scala-csv
  
  * Remember to Log in. The UI mostly works, but you can't find the right stuff which can be confusing.

  * Staging Repositories from left bar

  * From dropdown choose Nexus managed repositories (originally User Managed Repositories)
    * Type your organisation name to the search box without dots (fipelam since I have fi.pelam,)
    
  * From the top bar, "close" the repository
  
  * Hit refresh after a while, "Release" should be available
  
  * After this it seems to take a while (10min or so) for the dependency to become visible
    * https://repo.maven.apache.org/maven2/fi/pelam/pelam-scala-incubator_2.12/0.3/pelam-scala-incubator_2.12-0.3.pom
    * However, it is immediately visible here, but dependency lookup can't see it:
    * https://oss.sonatype.org/index.html#view-repositories;releases~browsestorage~/fi/pelam/pelam-scala-csv  
  
   
