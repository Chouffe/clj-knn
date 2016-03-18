# clj-knn

Implementation of k-nearest neighbors in Clojure.

### Regression

```
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
```

### Classification

```
(def X
  [[1 6]
   [2 4]
   [3 7]
   [6 8]
   [7 1]
   [8 4]])

(def y-class [:slow :slow :fast :fast :fast :light-speed])
(def data-class (make-data X y-class))

(assert (= :slow (classify 1 euclidean data-class [4 2])))
(assert (= :slow (classify 3 euclidean data-class [4 2])))
(assert (= :fast (classify 3 manhattan data-class [5 6])))
(assert (= :fast (classify 3 euclidean data-class [5 6])))
```

## knn

k-NN is a type of instance-based learning, or lazy learning, where the function is only approximated locally and all computation is deferred until classification. The k-NN algorithm is among the simplest of all machine learning algorithms.

The following distance metrics are provided:
* Euclidean
* Taxicab/Manhattan

The k-nn implementation is simple to extend by providing your own distance metrics (Should meet the [Mercer's Conditions](https://en.wikipedia.org/wiki/Mercer%27s_condition)).

It currently supports the following prediction policies:
* Classification
  * Mode
* Regression
  * Mean
  * LWR (Locally Weighted Regression)

## License

Copyright © 2016 Arthur Caillau

Distributed under the Eclipse Public License either version 1.
