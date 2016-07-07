# telegenic
Video creation and manipulation in Clojure

[![Clojars Project](http://clojars.org/net.mikera/telegenic/latest-version.svg)](http://clojars.org/net.mikera/telegenic)

Currently you can use it to encode H264 video from a sequence of image frames. Not much support for anything else yet.

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
