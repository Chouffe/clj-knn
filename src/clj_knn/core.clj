(ns clj-knn.core
  (:require [clj-knn.distances :refer [euclidean manhattan]]))

(def X
  [[1 6]
   [2 4]
   [3 7]
   [6 8]
   [7 1]
   [8 4]])

(def y [7 8 16 44 50 68])

(defn make-data [X y]
  (mapv vector X y))

(def data (make-data X y))

(defn mean [xs]
  {:pre [(seq xs) (every? number? xs)]
   :post [(number? %)]}
  (/ (reduce + xs) (count xs)))

(defn take-at-least-aux [n [x & xs :as coll] acc]
  (cond
    (not (seq coll)) acc
    (and (<= n 0) (not= x (last acc))) acc
    :else (recur (dec n) xs (conj acc x))))

(assert (= [1 1] (take-at-least-aux 2 [1 1 2] [])))
(assert (= [1 1] (take-at-least-aux 1 [1 1 2] [])))
(assert (= [] (take-at-least-aux 0 [1 1 2] [])))

(defn take-at-least [n coll]
  (take-at-least-aux n coll []))

; TODO: Very inefficient - Use a heap and do not sort twice
(defn predict [k distance-metric data datapoint]
  (let [distances (map (comp (partial distance-metric datapoint) first) data)
        k (->> distances sort (take-at-least k) count)]
    (->> data
         (sort-by (comp (partial distance-metric datapoint) first))
         (map second)
         (take k)
         mean)))

(assert (= 8 (predict 1 euclidean data [4 2])))
(assert (= 42 (predict 3 euclidean data [4 2])))
(assert (= 29 (predict 1 manhattan data [4 2])))
(assert (= 71/2 (predict 3 manhattan data [4 2])))
