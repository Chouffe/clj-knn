(ns clj-knn.core
  (:require [clj-knn.utils :refer [inverse lwr mode mean take-at-least make-data]]
            [clj-knn.distances :refer [euclidean manhattan]]))

; TODO: Not very efficient - Use a min-heap O(k.lg(N)) instead of O (N.lg(N))
(defn predict [prediction-policy]
  "Prediction policy takes in a sequence of values
   [[[X1 y1] distance], ... [[Xk yk] distancek]] and returns a y

   Output: Returns a function that takes in the following keys:

   - k: Integer
   - distance-metric: similarity function - Mercer's Conditions
   - data: all the data for the algo to be able to predict - lazy learning
   - datapoint: a new datapoint to make a prediction on

   - Output: the prediction on the new-datapoint"
  (fn [k distance-metric data datapoint]
    (let [distances (map (comp (partial distance-metric datapoint) first) data)
          k (->> distances sort (take-at-least k) count)]
      (->> distances
           (map vector data)
           (sort-by second)
           (take k)
           prediction-policy))))

(def ys (partial map (comp second first)))
(def distances (partial map second))

(def classify (predict (comp mode ys)))
(def regression (predict (comp mean ys)))
; Locally Weighted Regression
(def regression-2 (predict (comp (partial apply lwr)
                                 (juxt ys
                                       (comp (partial map inverse)
                                             distances)))))

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
(assert (= 171/5 (regression-2 3 manhattan data [4 2])))
(assert (<= 37 (regression-2 3 euclidean data [4 2]) 38))

(def y-class [:slow :slow :fast :fast :fast :light-speed])
(def data-class (make-data X y-class))

(assert (= :slow (classify 1 euclidean data-class [4 2])))
(assert (= :slow (classify 3 euclidean data-class [4 2])))
(assert (= :fast (classify 3 manhattan data-class [5 6])))
(assert (= :fast (classify 3 euclidean data-class [5 6])))
