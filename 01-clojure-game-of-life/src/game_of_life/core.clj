(ns game-of-life.core
  (:gen-class))

(def around [[-1 -1] [-1 0] [-1 1]
             [0 -1]         [0 1]
             [1 -1]  [1 0]  [1 1]])

(defn around-cell
  [[x y]]
  (map
   (fn
     [[_x _y]]
     [(+ x _x) (+ y _y)])
   around))

(defn world
  [& cells]
  (set cells))

(defn alive?
  [cell world]
  (contains? world cell))

(defn dead?
  [cell world]
  (not (alive? cell world)))

(defn neighbours
  [cell world]
  (->>
   cell
   (around-cell)
   (filter #(alive? % world))))

(defn possibly-live
  [world]
  (->>
   world
   (mapcat around-cell)
   (set)))

(defn will-live?
  [cell world]
  (let [neighbours-count (count (neighbours cell world))]
    (if (alive? cell world)
      (or (= 2 neighbours-count) (= 3 neighbours-count))
      (= 3 neighbours-count))))

(defn step
  [world]
  (->>
   world
   (possibly-live)
   (filter #(will-live? % world))
   (set)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
