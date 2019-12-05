(ns experiment.services.categories.list
  (:require [experiment.repositories.category :as category-repo]))

(defn call
  []
  (category-repo/all))
