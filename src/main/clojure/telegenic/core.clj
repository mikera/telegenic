(ns telegenic.core
  (:import [org.jcodec.api FrameGrab])
  (:import [java.io File])
  (:import [java.awt.image BufferedImage])
  (:import [org.jcodec.common.model Picture])
  )

(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defprotocol PGetFrame
  (get-frame [video frame-number]))

(extend-protocol PGetFrame
  String
    (get-frame [s frame-number] (get-frame (File. s) frame-number))
  File
    (get-frame [file frame-number]
      (let [^Picture nf (FrameGrab/getNativeFrame file (int frame-number))
            bi (.toBufferedImage nf)]
        bi)))