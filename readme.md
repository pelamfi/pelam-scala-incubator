# Pelam Scala Incubator

Pelam's open source utility code collection.

I will collect all kinds of useful bits of code here without worrying
about their dependencies.

If anyone finds anything here useful or interesting, let me know!
I promise to clean up the dependencies of that part and publish it separately.

## Listing of different utilities

  * OffensiveFSM -- Standard Akka FSM is defensive, but during development you want a loud signals when the unexpected happens.
  * JavaFXActor -- A marriage between JavaFX and Akka; Using the actor model in UI development is suprisingly handy.
  * Throttle -- Wrap your function in a limiter that executes only after a quiet period.
  * JavaFXNodeDb -- Track nodes in a JavaFX hierarchy and get notified when nodes of interest are added
  * JavaFXImplicits -- Implicit conversions for working with various JavaFX callbacks
  * Rectangle2DUtil -- Some utils for working with JavaFX rectangle objects
  * TransformUtil -- Some utils for working with JavaFX transforms and bounds
  * DiffUtil -- Simple wrappers for java-diff-utils library
  * EitherUtil -- Some operations on Scala Either type: `partitionEihers` and `pullUpEithers`
  * EnumLocalizationMapFactory -- Quickly get a map from Enumeratum enum into localized strings loaded from a resource bundle.
  * FormatterUtil -- Convert Java non thread safe formaters into a nice thread safe Scala functions.
  * Memoize -- A simple memoizing functor based on an idea from Stakoverflow
  * RingBuffer -- A fast polling based buffer for communicating between threads. Ideas from LMAX Disruptor.
  * plus some other tiny utility functions waiting to grow and improve
  

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
  * Difflib
  
## TODO

  * Complete code examples for each of the utilities
  * At least one test for each util

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
