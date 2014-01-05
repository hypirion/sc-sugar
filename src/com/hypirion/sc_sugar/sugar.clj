(ns com.hypirion.sc-sugar.sugar
  (:require [simple-check.generators :as gen]
            [simple-check.properties :as prop]
            [clojure.walk :as walk]
            [clojure.set :as set]))

(defn- all-atoms
  "Returns a set of all the atoms (non-compound values) in the form."
  [form]
  (walk/postwalk (fn [n] (if (coll? n) (reduce into #{} n) #{n})) form))

(defn- binding-deps
  "Returns all deps which this expression depends on. Ignores binding forms."
  [prev-vars expr]
  (-> (all-atoms expr)
      (set/intersection (set prev-vars))))
