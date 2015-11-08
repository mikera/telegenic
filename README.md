# telegenic
Video creation and manipulation in Clojure

Telegenic is a wrapper for the pure-Java JCodec library for video encoding / decoding.

https://github.com/jcodec/jcodec

## Usage

```clojure
(use 'telegenic.core)
(use 'mikera.image.core :as img)

(let [f1 (img/new-image 100 100)
         _ (img/fill-rect! f1 0 0 100 100 java.awt.Color/GREEN)
         f2 (img/new-image 100 100)]
     (encode (concat (repeat 20 f1) (repeat 20 f2) (repeat 20 f1) (repeat 20 f2))) {:filename "out.mp4"})

```
