(ns corpus.logic
  (:use corpus.data
        corpus.tools
        corpus.components
        ;corpus.forces
        )
  (:import
    (javax.swing JFrame)
    (java.awt Color)))

; Graphics
(defn create-window []
  (let [window (JFrame. "adAstra!")]
    (doto window
      (. setSize (window-config :width) (window-config :height))
      (. setLocation 300 200)
      (. setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (. setResizable false)
      (. show)
      (. toFront)
      (. setAlwaysOnTop true)
      (. createBufferStrategy 2))))

(defn render-entity [comps g]
  ((-> comps :rend :fn) g comps))

(defn render [world g]
  (. g setColor (Color/black))
  (. g fillRect 0 0 (:width window-config) (:height window-config))
  (examine-world-part world renderable? render-entity g))

;(defn render [world renderables g]
;  (. g setColor (Color/black))
;  (. g fillRect 0 0 (:width window-config) (:height window-config))
;  (doseq [rend renderables]
;    (render-entity (get-comps world rend) g)))

; Entropy

(defn kill [entity-comps]
  (let [lifetime (-> entity-comps :mortal :t)]
    (if-let [bool (> lifetime 0)]
      (assoc entity-comps :mortal (struct mortal (dec lifetime))))))

(defn entropy [world]
  (change-world-part world mortal? kill))


; Movement
;
(defn step [comps]
  (let [pos (:pos comps) vel (:vel comps)
        dX (+ (:vx vel) (:dx vel))
        dY (+ (:vy vel) (:dy vel))
        x (+ (:x pos) (int dX))
        y (+ (:y pos) (int dY))]
    (assoc comps :pos (struct position x y)
                 :vel (struct velocity (:vx vel) (:vy vel) (- dX (int dX)) (- dY (int dY))))))

(defn move [world]
  (change-world-part world movable? step))
;
; Particle generator

;  (let [t (atom 0)]
;    (defn particle-generator [fun]
;      (fn []
;        (fun (swap! t inc)))))

;; Generator functions

(defn random-particle []
                   {:pos    (struct position (rand-int (:width window-config))
                                    (rand-int (:height window-config)))
                    :vel    (struct velocity (- (rand) 0.5) (- (rand) 0.5) 0 0)
                    :mortal (struct mortal (rand-int 1000))
                    :rend   (struct renderable draw-dot)
                    :inert  (struct inert 1)})
;
;(defn static-particle [x y lifetime]
;  (create-particle (get-id)
;                   {:pos    (struct position x y)
;                    :vel    (struct velocity 0 0 0 0)
;                    :mortal (struct mortal lifetime)
;                    :rend   (struct renderable draw-dot)
;                    :inert  (struct inert 1)}))
;
;(defn dispersion [particle du dt]
;  (let [comps (second particle)
;        vx (-> comps :vel :vx)
;        vy (-> comps :vel :vy)
;        lifetime (-> comps :mortal :t)]
;    (create-particle (first particle)
;                     (assoc (second particle)
;                            :vel (struct velocity (+ vx (* (- (rand) 0.5) du))
;                                         (+ vy (* (- (rand) 0.5) du)) 0 0)
;                            :mortal (struct mortal (+ lifetime (- (rand-int dt) (/ dt 2))))))))
;
;(defn thrower [x y lifetime force du dt]
;  (fn []
;  (dispersion (push (static-particle x y lifetime) force) du dt)))
;
;(defn create-directed-generator [x y lifetime force du dt]
;  [(get-id)
;   {:static (struct static nil)
;    :gen    (struct generator (thrower x y lifetime force du dt))}])
;
;  (let [t (atom 0)]
;    (defn particle-generator [fun]
;      (fn []
;        (fun (swap! t inc)))))
;
;(defn birth [world]
;  (loop [generators (filter generator? world)
;         new-world world]
;    (if (empty? generators)
;      new-world
;      (recur (rest generators)
;             (add-particle ((-> (sfirst generators) :gen :fn)) new-world)))))



