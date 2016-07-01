# telegenic
Video creation and manipulation in Clojure

[![Clojars Project](http://clojars.org/net.mikera/telegenic/latest-version.svg)](http://clojars.org/net.mikera/telegenic)

Currently you can use it to encode H264 video from a sequence of image frames. Not much support for anything else yet.

## Note on JCodec usage

Telegenic is a wrapper for the pure-Java JCodec library for video encoding / decoding. You may need to manually install the `jcodec-javase` dependency into your local maven repository, as this is currently not available on Maven Central. You can download the artifacts here:

http://jcodec.org/

Check installation.md for more info on this.  Currently, the required 0.2.0 libraries are included in /lib.

## Usage

```clojure
(use 'telegenic.core)
(use 'mikera.image.core :as img)

(let [;; A green frame
      f1 (img/new-image 100 100)
      _ (img/fill-rect! f1 0 0 100 100 java.awt.Color/GREEN)
      
      ;; A green frame
      f2 (img/new-image 100 100)
      
      ;; A sequence of frames. Frames can be re-used for efficiency.
      frames (concat (repeat 20 f1) (repeat 20 f2) (repeat 20 f1) (repeat 20 f2)))]
  
  ;; finally encode everything into an mp4
  (encode frames {:filename "out.mp4"})

```

#Changes in 0.0.2

jcodec is still in development, so there are breaking changes to the API as well
as non-implemented features.  0.0.1 relied on jcodec 0.1.9, which did not support
portable encoding of .mp4 movies.  Specifically, on some platforms, the movie
would immediately skip to the end, not playing anything. jcodec 0.2.0 fixes this,
and quite a bit more, albeit with some breaking changes to the previous api.

Telegenic now targets jcodec 0.2.0, and produces mp4s correctly.

To go with 0.0.2, there's a short swing-based example included in telegenic.examples.


