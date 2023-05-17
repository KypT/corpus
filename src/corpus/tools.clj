(ns corpus.tools
    (:use corpus.data
          corpus.components))

(defn sfirst [v]
  (second (first v)))

(defn sqr [x]
  (* x x))

(defn sqrt [x]
  (Math/sqrt x))

(def != (complement =))

(defn find-pred [world pred]
  (loop [index 0 entities []]
    (cond
      (> index (world-size world)) entities
      (pred (get-comps world index))(recur (inc index) (conj entities index))
      :else (recur (inc index) entities))))

(defn if-rend [entity]
  (when (renderable? (second entity))
    {:rend (list (first entity))}))

(defn if-mortal [entity]
  (when (mortal? (second entity))
    {:mortal (list (first entity))}))

(defn if-mov [entity]
  (when (movable? (second entity))
    {:vel (list (first entity))}))

(defn find-entities [world]
  (loop [index 0 m {}]
    (if (> index (world-size world))
      m
      (let [res ((juxt if-rend if-mortal if-mov) [index (get-comps world index)])]
        (recur  (inc index) (merge-with concat m (apply merge-with concat res)))))))

(defn examine-world-part [part pred fun args]
  (doseq [e part]
    (fun (get-comps world e) args)))

(defn change-world-part [part pred fun]
  (doseq [e part]
    (if-let [cmp (fun (get-comps world e))]
      (set-entity! world e cmp)
      (del-entity! world e))))