(ns experiment.serializers.category
  (:require [experiment.services.serializer :as s]))

(def options {:type "categories"
              :attributes '(:name)})

(defn serialize [data & args]
  (let [additional-options (apply hash-map args)]
    (s/serialize data (conj options additional-options))))
