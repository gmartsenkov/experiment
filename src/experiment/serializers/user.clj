f(ns experiment.serializers.user
  (:require [experiment.services.serializer :as s]))

(def options {:type "users"
              :attributes '(:first_name :last_name :email)})

(defn serialize [obj] (s/serialize obj options))
