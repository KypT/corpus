(ns corpus.data
  (:use corpus.components))

; Help functions

 (let [i (atom 0)]
   (defn get-id []
     (swap! i inc)))

; Data definitions

(def window-config
  {:width 600 :height 400})

;; Entity data structure

(defn create-entity [id comps]
  [id comps])

(defn id [entity]
  (first entity))

(defn comps [entity]
  (second entity))

; World data structure

(def world (object-array 1000))
(aset world 0 0)

(defn world-size [world]
  (aget world 0))

(defn add-entity! [world comps]
  (aset world 0 (inc (world-size world)))
  (aset world (world-size world) comps))

(defn set-entity! [world id comps]
  (aset world id comps))

(defn get-comps [world id]
  (aget world id))

(defn del-entity! [world id]
  (when-not (= (world-size world) 0)
    (aset world id (get-comps world (world-size world)))
    (aset world 0 (dec (world-size world)))))

;; Game-objects

(def sun1
  {:pos    (struct position 300 275)
   :rend   (struct renderable draw-circle)
   :vel    (struct velocity (- (Math/sqrt (/ 600 75.0))) 0 0 0)
   :color (struct colored (java.awt.Color/yellow))
   :circle (struct circled 2)
   :inert (struct inert 20)})

(def sun2
  {:pos    (struct position 300 200)
   :rend   (struct renderable draw-circle)
   :vel    (struct velocity 0 -1 0 0)
   :color (struct colored (java.awt.Color/yellow))
   :circle (struct circled 5)
   :inert (struct inert 30)
   :static (struct static nil)})

(add-entity! world sun1)
(add-entity! world sun2)
