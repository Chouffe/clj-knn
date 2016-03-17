(ns clj-knn.utils)

(defn square [x]
  {:pre [(number? x)]
   :post [(number? %)]}
  (* x x))

(defn abs [x]
  {:pre [(number? x)]
   :post [(number? %)]}
  (max x (- x)))

(defn mean [xs]
  {:pre [(seq xs) (every? number? xs)]
   :post [(number? %)]}
  (/ (reduce + xs) (count xs)))

(assert (= 1 (mean [0 1 2])))
(assert (= 0 (mean [0 0 0])))

(defn mode [xs]
  {:post [(or (get (set xs) %) (nil? %))]}
  (let [freqs (frequencies xs)]
    (first (reduce (fn [[k-max v-max] [k v]] (if (> v v-max) [k v] [k-max v-max]))
                   (first freqs)
                   (rest freqs)))))

(assert (= :a (mode [:a :a :a :b :c])))
(assert (= :d (mode (shuffle [:a :a :a :b :c :c :d :d :d :d]))))
(assert (not (mode [])))

(defn inverse [x]
  (if (zero? x)
    (Integer/MAX_VALUE)
    (/ 1 x)))

(assert (= 1 (inverse 1)))
(assert (= 1/2 (inverse 2)))

(defn lwr [xs weights]
  (/ (reduce + (map * xs weights))
     (reduce + weights)))

(assert (= 1 (lwr [1 2] [1 0])))
(assert (= 3/2 (lwr [1 2] [1 1])))

(defn make-data [X y]
  (mapv vector X y))

(defn ^:private take-at-least-aux [n [x & xs :as coll] acc]
  (cond
    (not (seq coll)) acc
    (and (<= n 0) (not= x (last acc))) acc
    :else (recur (dec n) xs (conj acc x))))

(assert (= [1 1] (take-at-least-aux 2 [1 1 2] [])))
(assert (= [1 1] (take-at-least-aux 1 [1 1 2] [])))
(assert (= [] (take-at-least-aux 0 [1 1 2] [])))

(defn take-at-least [n coll]
  (take-at-least-aux n coll []))
