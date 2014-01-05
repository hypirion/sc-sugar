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

(defn- all-binding-deps
  "Given the ordered list of paired bindings, returns a map with the binding
  dependencies."
  [paired-bindings]
  (loop [prev-vars #{}
         deps {}
         [[var expr] & rest-bindings :as binds] paired-bindings]
    (if-not (seq binds)
      deps
      (recur (conj prev-vars var)
             (assoc deps var (binding-deps prev-vars expr))
             rest-bindings))))

(defn- all-resolved?
  "Return true if all dependencies have been resolved, false otherwise."
  [dep-map var-sets]
  ;; for all vars in a var set, the var's dependencies must be a part of the var
  ;; set itself (i.e. must be a subset of the var set)
  (let [set-resolved? (fn [var-set]
                        (every? (fn [var]
                                  (set/subset? (get dep-map var) var-set))))]
    (every? set-resolved? var-sets)))

(defmacro ^:private bind->>
  ([bindings gen] [bindings gen])
  ([b1 g1 b2 g2 & more]
     `(bind->> [~b1 ~b2]
               (gen/bind ~g1
                         (fn [~b1] (gen/tuple (gen/return ~b1)
                                             ~g2)))
      ~@more)))

(defmacro for-all-bind
  "As for-all, but automatically initialises bindings between values. Threads
  the bindings through bind, making each dependent of all the previous generator
  results."
  [bindings & body]
  `(for-all (bind->> ~@bindings)
     ~@body))

(comment
  ;; Will have to revisit this one later, is rather daunting.
  (defn- create-binding-tree [dep-map expr-maps]
    ;; First emit the expression without dependencies. Then emit the ones
    ;; depending on others.
    (let [init-keymap (into {} (for [[var expr] expr-maps]
                                 [#{var} [var expr]]))]
      (loop [keymap init-keymap]
        (if (all-resolved? dep-map (keys keymap))
          (vec (apply concat (vals keymap))) ; finalise expr here
          (let []

            )
          )
        )))

  (defmacro for-all-bind
    [bindings & body]
    
    ))

