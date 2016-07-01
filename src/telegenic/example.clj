;;A simple example...
(ns telegenic.example
  (:require [telegenic.core :as core])
  (:import  [javax.swing JPanel JFrame]
            [java.awt Graphics2D Transparency GraphicsDevice GraphicsConfiguration
             GraphicsEnvironment]
            [java.awt.image BufferedImage]))

;;A long winded way to get a buffer...
(defn ^BufferedImage make-imgbuffer 
  ([w h ^Transparency t]
	  (let [^GraphicsEnvironment ge 
	          (GraphicsEnvironment/getLocalGraphicsEnvironment)
	        ^GraphicsDevice gd (.getDefaultScreenDevice ge)
	        ^GraphicsConfiguration gc (.getDefaultConfiguration gd)]	  
	    (.createCompatibleImage gc w h t)))
  ([w h] (make-imgbuffer w h Transparency/TRANSLUCENT)))

;;i have an entire scene graph lib at my disposal...but we'll use java2d
;;to keep to self contained...

;;minimalist shapes...
(defn ->rect [^Graphics2D g ^java.awt.Color color x y w h]
  (do (.setColor g color)
      (.fillRect g  (int x) (int y) (int w) (int h))
      g))

(defn ->text [^Graphics2D g  ^java.awt.Color color x y  ^String text]
  (do (.setColor g color)
      (.drawString g  text (float x) (float y) )
      g))

;;generate a sequence of image buffers.  In reality,
;;we're just returning a seq of references to the same
;;underlying mutated image buffer.  Better to use a reducer
;;here, but the original library prefers seqs.
(defn movie-maker [width height duration fps]
  (let [^BufferedImage the-buffer (make-imgbuffer width height)  ;;make a simple image.
        ^Graphics2D canvas     (.getGraphics the-buffer)
        interval      (/ 1.0 fps)
        framecount    (* duration fps)
        ^java.awt.Color green  java.awt.Color/green
        ^java.awt.Color red    java.awt.Color/red
        ^java.awt.Color blue   java.awt.Color/blue
        ;;this will render directly onto the buffer.
        time->image   (fn [t]
                        (do (-> canvas
                                ;green background
                                (->rect  green 0 0 width height)
                                ;transform a red rect around a unit circle.
                                (->rect  red  (+ 200 (* 4.0 (Math/sin t) 50))
                                              (+ 200 (* 4.0 (Math/cos t) 50))
                                         50
                                         50
                                         )
                                ;;some throbbing text                                
                                (->text blue  (+ 200 (* 4.0 (Math/cos t) 50)) 200 "Telegenic Makes Movies!")
                                )
                            ;;we recycle the same buffer.
                            the-buffer))] 
    (->> (iterate #(+ % interval) 0)
         (take  framecount)
         (map time->image))))

(defn dump-movie [path images]
  (let [_ (println [:dumping-to path])]         
    ;; finally encode everything into an mp4
    (core/encode images {:filename path})))

(defn demo
  ([] (demo "mymovie.mp4"))
  ([path & {:keys [fps duration width height]
            :or {fps 30 duration 10 width 400 height 400}}]
   
   (dump-movie  path (movie-maker width height duration fps))))

