# telegenic
Video creation and manipulation in Clojure

## Usage

```clojure
(use 'telegenic.core)
(use 'mikera.image.core :as img)

(let [f1 (img/new-image 100 100)
         _ (img/fill-rect! f1 0 0 100 100 java.awt.Color/GREEN)
         f2 (img/new-image 100 100)]
     (encode (concat (repeat 20 f1) (repeat 20 f2) (repeat 20 f1) (repeat 20 f2))) {:filename "out.mp4"})

```
