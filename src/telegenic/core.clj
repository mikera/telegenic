(ns telegenic.core
  (:import [java.io File])
  (:import [java.awt.image BufferedImage])
  (:import [org.jcodec.api FrameGrab] ;;changed, no longer in .awt
           [org.jcodec.codecs.h264 H264Encoder]
           [org.jcodec.api SequenceEncoder]
           [org.jcodec.common.model Picture]
           [org.jcodec.scale AWTUtil]))

(set! *warn-on-reflection* true)
(set! *unchecked-math*     true)

(defprotocol PGetFrame
  (get-frame [video frame-number]))

(extend-protocol PGetFrame
  String
    (get-frame [s frame-number] (get-frame (File. s) frame-number))
  File
    (get-frame [file frame-number]
      (let [^BufferedImage bi (FrameGrab/getFrameFromFile file (int frame-number))]
        bi)))


;;trying to wrap a bit of idiomatic functions around the underlying jcodec
;;api, since it appears to be changing.  Ideally, we can just update the
;;functions later if we need to.
(defn ^SequenceEncoder encode-frame [^SequenceEncoder enc ^BufferedImage frame]
  ;;method changed in 0.2.0
  (let [^Picture picture (AWTUtil/fromBufferedImageRGB frame)] 
    (doto enc (.encodeNativeFrame picture))))

(defn finish! [^SequenceEncoder enc] (.finish enc))

(defn encode
  "Encodes a sequence of frames to a video file.

   Returns a map decribing the encoding the result.

   TODO: Options is a map that may include:
     :filename   Filename to output the econded video to
     :log        True if you want logging output
     :framerate  Numeric number of frames per second
     :key-interval"
  ([frames]         (encode frames nil))
  ([frames options]
    (let [start-time (System/currentTimeMillis)
          filename   (str (or (:filename options "out.mp4")))
          ^File file (or  (:file options (File. filename)))
          ^SequenceEncoder enc (SequenceEncoder/createSequenceEncoder file) ;api changed.
          ;; ^H264Encoder encoder (.getEncoder enc)
          ;; framerate (or (:frame-rate options 30))
          ;; _ (.setKeyInterval encoder (int framerate))
          counter (atom 0)]
      ;;changing to reduce here, more general, can be used with transducers/reducers too.
      (do 
        (->> frames
             (reduce (fn [enc frame]
                       (do (swap! counter inc)
                           (encode-frame enc frame)))
                     enc)
             (finish!))
        {:filename filename
         :frame-count @counter
         :time (* 0.001 (- (System/currentTimeMillis) start-time))}))))
