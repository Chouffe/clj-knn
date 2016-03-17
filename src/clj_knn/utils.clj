(ns clj-knn.utils)

(defn square [x]
  (* x x))

(defn abs [x]
  (max x (- x)))

(defn mean [xs]
  {:pre [(seq xs) (every? number? xs)]
   :post [(number? %)]}
  (/ (reduce + xs) (count xs)))

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

(defn make-data [X y]
  (mapv vector X y))

