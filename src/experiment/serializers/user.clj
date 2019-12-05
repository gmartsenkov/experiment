(ns experiment.serializers.user
  (:require [experiment.services.serializer :as s]))

(def options {:type "users"
              :attributes '(:first_name :last_name :email)})

(defn serialize [data & args]
  (let [additional-options (apply hash-map args)]
    (s/serialize data (conj options additional-options))))
