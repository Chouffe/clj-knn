(ns clj-knn.core
  (:require [clj-knn.utils :refer [mean take-at-least make-data]]
            [clj-knn.distances :refer [euclidean manhattan]]))

; TODO: Very inefficient - Use a heap and do not sort twice
(defn predict [prediction-policy]
  (fn [k distance-metric data datapoint]
    (let [distances (map (comp (partial distance-metric datapoint) first) data)
          k (->> distances sort (take-at-least k) count)]
      (->> data
           (sort-by (comp (partial distance-metric datapoint) first))
           (map second)
           (take k)
           prediction-policy))))

(def classify (predict (comp first (partial sort-by second >) frequencies)))
(def regression (predict mean))

; Test Cases
; ------------
(def X
  [[1 6]
   [2 4]
   [3 7]
   [6 8]
   [7 1]
   [8 4]])

(def y [7 8 16 44 50 68])
(def data (make-data X y))

(assert (= 8 (regression 1 euclidean data [4 2])))
(assert (= 42 (regression 3 euclidean data [4 2])))
(assert (= 29 (regression 1 manhattan data [4 2])))
(assert (= 71/2 (regression 3 manhattan data [4 2])))
