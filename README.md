# detris

This project contains my first attempt at using the Clojure language.

I'd still like to learn more tricks with Clojure, since I could improve on making the code less verbose and replacing my use of loops with more idiomatic functional paradigms.  I look forward to learning more about this powerful language!  Having been a Java programmer for the past decade, I have to say, learning some Scala and Clojure has really been an eye opener.

The application is a simplified game of Tetris played from reading the "input.txt" file for the moves.  Each line represents a new board.   Basically, it has all the basic principles you love about the original game, but dummied down so there's no rotating or horizontal movement of pieces.

## Installation

I'm using Clojure 1.5.1

Also, if you don't have Leiningen, one of the ways to install is like this:
> cd; wget https://raw.github.com/technomancy/leiningen/stable/bin/lein

> sudo cp lein /usr/bin/lein

> sudo chmod ugo+rx /usr/bin/lein

## Usage

To run using Leiningen: 
> lein run [inputfile]

The optional inputfile arg is the location of some file that contains moves for the detris board.
If no option is specified, the default will be "input.txt", which exists in the project directory (the project already provides one of these).  
The results will be written to "output.txt" at the same location.

To build the the uberjar: 
> lein uberjar

To run with the uberjar (from the base project dir):
> java -jar target/detris-0.1.0-SNAPSHOT-standalone.jar
