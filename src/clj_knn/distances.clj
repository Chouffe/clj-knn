(ns clj-knn.distances
  (:require [clj-knn.utils :refer [square abs]]))

(defn euclidean [x y]
  {:pre [(vector? x) (vector? y)]
   :post [(number? %) (>= % 0)]}
  (Math/sqrt (apply + (map (comp square -) x y))))

(assert (= 0.0 (euclidean [1 2] [1 2])))
(assert (= 1.0 (euclidean [1 0] [0 0])))

(defn manhattan [x y]
  {:pre [(vector? x) (vector? y)]
   :post [(number? %) (>= % 0)]}
  (apply + (map (comp abs -) x y)))

(assert (= 0 (manhattan [0 0] [0 0])))
(assert (= 1 (manhattan [1 0] [0 0])))
(assert (= 6 (manhattan [1 2 3] [2 4 6])))
