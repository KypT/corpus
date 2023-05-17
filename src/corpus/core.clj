(ns corpus.core
  (:gen-class
    main true)
  (:use corpus.components
        corpus.data
        corpus.logic
        ;corpus.forces
        corpus.tools)
  (:import
    (javax.swing JFrame)
    (java.awt Color)))


(defn -main []
  (let [window (create-window)
        buf-strategy (. window getBufferStrategy)
        g (. buf-strategy getDrawGraphics)
        super-massives [0 1]]
    (loop [entity-map (find-entities world)]
      (time (do
      (render (:rend entity-map) g)
      (. buf-strategy show)
      ;(Thread/sleep 5)
      (entropy (:mortal entity-map))
      (move (:vel entity-map))
      (add-entity! world (random-particle))))
      (recur (find-entities world)))))
