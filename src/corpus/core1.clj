(ns corpus.core
  (:gen-class
    main true)
  (:use corpus.components
        corpus.data
        corpus.logic)
  (:import
    (javax.swing JFrame)
    (java.awt Color)))

(defn -main []
  (let [window (create-window)
        buf-strategy (. window getBufferStrategy)
        g (. buf-strategy getDrawGraphics)]
    (loop [world init-world]
      (render g world)
      (. buf-strategy show)
      (Thread/sleep 10)
      (recur (gr(-> world entropy birth gravitation move)))))