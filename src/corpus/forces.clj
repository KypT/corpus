(ns corpus.forces
  (:use corpus.data
        corpus.components
        corpus.tools))

(defn add-forces [f1 f2]
  [(+ (first f1) (first f2))
   (+ (second f1) (second f2))])

(defn single-impact [particle src]
  (if (= (-> (comps particle) :pos :x) (-> (comps src) :pos :x))
    [0 0]
    (let [comps-p (comps particle)
          comps-s (comps src)
          xs (-> comps-s :pos :x)   ys (-> comps-s :pos :y)
          x  (-> comps-p :pos :x)   y  (-> comps-p :pos :y)
          ms (-> comps-s :inert :m) m (-> comps-p :inert :m)
          fx (-> comps-p :vel :dx) fy (-> comps-p :vel :dy)
          r (Math/sqrt (+ (* (- xs x) (- xs x)) (* (- ys y) (- ys y))))
          a (Math/atan2 (- ys y) (- xs x))
          F (/ (* ms m) r r)
          dx (* F (Math/cos a))
          dy (* F (Math/sin a))]
      [dx dy])))

(defn push [particle force]
  (let [c (comps particle)
        vx (-> c :vel :vx) vy (-> c :vel :vy)
        dx (-> c :vel :dx) dy (-> c :vel :dy)]
    (create-particle (id particle)
                     (assoc (comps particle) :vel 
                            (struct velocity (+ (first force) vx)
                                             (+ (second force) vy)
                                             dx dy)))))

(defn resultant-force [particle sources]
  (loop [src sources res-force [0 0]]
    (if (empty? src)
      res-force
      (recur (rest src)
             (add-forces res-force (single-impact particle (first src)))))))

(defn overlap? [particle obj]
  (let [id-p (id particle) id-o (id obj)
        xp (-> (comps particle) :pos :x)
        yp (-> (comps particle) :pos :y)
        xs (-> (comps obj) :pos :x)
        ys (-> (comps obj) :pos :y)
        r  (-> (comps obj) :circle :r)
        dist (Math/sqrt (+ (sqr(- xp xs)) (sqr (- yp ys))))]
    (and (<= dist r) (!= id-p id-o))))

(defn collide? [particle sources]
  (loop [src sources]
    (cond 
      (empty? src) false 
      (overlap? particle (first src)) true
      true (recur (rest src)))))

(defn impact [particle sources]
  (if (collide? particle sources)
    nil
    (let [res-force (resultant-force particle sources)]
      (push particle res-force))))

(defn gravitation [world super-massives]
  (loop [index 0]
    (if (empty? old-world)
      new-world
      (recur (rest old-world)
             (if (movable? (first old-world))
               (add-particle (impact (first old-world) (ids->entities super-massives world)) new-world)
               (add-particle (first old-world) new-world))))))
  










