These are maven-builds of the latest version of
jcodec as of 1 July 2016.  The jcodec folks pushed
out a major version increase in August/Nov, 0.2.0,
which - most importantly for me - fixed a deal
breaker for video encoding: no pframes.  This
meant that the mp4 encoded video would work in
say, VLC, but not other common players - most
notably Media Player.  For my use case, I need
portability.

Enter jcodec 0.2.0, which is NOT currently
on Maven.

You have a couple of options.  You can go grab the
source from http://github.com/jcodec, and,
assuming you have maven setup, use the java
build system via  'mvn clean install' from
the root directory and the /javase directory.

That'll produce two jars identical to the ones
here.

The "easier," more clojurian option, is to follow
the example of this project.

Add a :plugins entry to your project file
for lein-localrepo.  lein-localrepo is an
awesome tool that lets you virtually "install"
random jars into your local maven repository
under whatever name you'd like, without ever
having to touch maven.  If you go this route,
add the ":plugins [[lein-localrepo "0.5.3"]]"
to your project.clj file, inside the defproject
form, and save the file.

Then, from a command prompt - wherever you call
lein from - go to the lib directory and call:

lein local-repo install org.jcodec/jcodec "0.2.0" ./lib/jcodec-0.2.0.jar
lein local-repo install org.jcodec/jcodec-javase "0.2.0" ./lib/jcodec-javase-0.2.0.jar


Lein should automagically install these two libraries into your .m2 folder,
accessible via the dependencies:
[org.jcodec/jcodec "0.2.0"] 
[org.jcodec/jcodec-javase "0.2.0"]

If, in the future, jcodec development stabilizes, and the api
doesn't break too much, or the 0.2.0 version is pushed to
maven, these hacks won't be necessary.  Another alternative
is having a kind soul package the dependencies into a
clojure project and push it to clojars (I may do that).

-Tom