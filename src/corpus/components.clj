(ns corpus.components)

; Components
(defstruct position :x :y)
(defstruct velocity :vx :vy :dx :dy)
(defstruct inert :m)
(defstruct renderable :fn)
(defstruct mortal :t)
(defstruct colored :c)
(defstruct circled :r)
(defstruct static nil)
(defstruct generator :fn)


; Predicates
(defn renderable? [entity]
  (contains? entity :rend))

(defn movable? [entity]
  (and (contains?  entity :vel)
       (not (contains? entity :static))))

(defn material? [entity]
  (contains? entity :inert))

(defn mortal? [entity]
  (contains? entity :mortal))

(defn colored? [entity]
  (contains? (entity :color)))

(defn generator? [entity]
  (contains? entity :gen))

; render graphics

(defn draw-dot [g comps]
  (let [x (-> comps :pos :x) y (-> comps :pos :y)]
    (if (contains? comps :color)
      (. g setColor (-> comps :color :c))
      (. g setColor (java.awt.Color/white)))
    (. g drawLine x y x y)))

(defn draw-circle [g comps]
  (let [pos (-> comps :pos) r (-> comps :circle :r)
        x (- (:x pos) r) y  (- (:y pos) r)]
    (if (contains? comps :color)
      (. g setColor (-> comps :color :c))
      (. g setColor (java.awt.Color/white)))
    (. g fillOval x y (* r 2) (* r 2))))

(defn get-color [particle]
  (if (colored? particle)
    (-> (second particle) :color :c)))