(ns telegenic.core
  (:import [java.io File])
  (:import [java.awt.image BufferedImage])
  (:import [org.jcodec.api.awt FrameGrab]
           [org.jcodec.codecs.h264 H264Encoder]
           [org.jcodec.api SequenceEncoder]
           [org.jcodec.common.model Picture]
           [org.jcodec.scale AWTUtil])
  (:require [mikera.image.core :as img])
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
      (let [^BufferedImage bi (FrameGrab/getFrame file (int frame-number))]
        bi)))


(defn encode
  "Encodes a sequence of frames to a video file.

   Returns a map decribing the encoding the result.

   TODO: Options is a map that may include:
     :filename   Filename to output the econded video to
     :log        True if you want logging output
     :framerate  Numeric number of frames per second"
  ([frames]
    (encode frames nil))
  ([frames options]
    (let [start-time (System/currentTimeMillis)
          filename (str (or (:filename options "out.mp4")))
          ^File file (or (:file options (File. filename)))
          enc (SequenceEncoder. file)
          ;; ^H264Encoder encoder (.getEncoder enc)
          ;; framerate (or (:frame-rate options 30))
          ;; _ (.setKeyInterval encoder (int framerate))
          counter (atom 0)]
      (doseq [frame frames]
        (let [^BufferedImage frame frame
              ^Picture picture (AWTUtil/fromBufferedImage frame)]
          (.encodeNativeFrame enc picture))
        (swap! counter inc))
      (.finish enc)
      {:filename filename
       :frame-count @counter
       :time (* 0.001 (- (System/currentTimeMillis) start-time))})))