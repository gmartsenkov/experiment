(ns experiment.specs.errors
  (:require [clojure.spec.alpha :as s]))

(defn format-error
  [errors]
  (map
   (fn [error] {:path (:path error)
                :pred (:pred error)
                :val (:val error)})
   (last (first errors)))
  )
