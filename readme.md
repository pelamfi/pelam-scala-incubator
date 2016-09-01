# Project Pelam Scala Incubator

Pelam's open source utility code collection.

I will collect all kinds of useful bits of code here without worrying
about their dependencies.

If anyone finds anything here useful or interesting, let me know!
I promise to clean up the dependencies of that part and publish it separately.

## Current Dependencies

  * [Akka](http://akka.io/docs/) -- Erlang like Actor model implementation for Scala and Java
    * Used in Akka + JavaFX binding binding utilities
    * Also some utilities built on top of Akka
  * [JavaFX](http://docs.oracle.com/javase/8/javase-clienttechnologies.htm) -- The new modern Java GUI toolkit
    * Used in Akka + JavaFX binding binding utilities 
  * [Guava](https://github.com/google/guava) -- Guava the Google Common code library (for [CacheBuilder](https://google.github.io/guava/releases/18.0/api/docs/com/google/common/cache/CacheBuilder.html))
    * Used in enumeration to localization bundle mapping utility.
  * [Enumeratum](https://github.com/lloydmeta/enumeratum) -- Nice Java like enums for Scala.
    * Used in enumeration to localization bundle mapping utility.
  * [Grizzled-SLF4J](http://software.clapper.org/grizzled-slf4j/) Grizzled-SLF4J, a Scala-friendly SLF4J Wrappe

## License

Pelam's Scala Incubator library is distributed under the 
Apache 2.0 license which is available in the included file [LICENSE.txt](LICENSE.txt)
and [online](http://www.apache.org/licenses/LICENSE-2.0).

## History of this project

Most of this code was originally developed for a custom project called Ahma.
I broke these bits of code off from Ahma as I felt that some of them could
become generally useful open source Scala libraries. As a note about the Git history,
I just copied the bits of code over from the Ahma project. I did not bother to clean
up and bring over the Git history from Ahma.
