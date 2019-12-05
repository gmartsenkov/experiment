(ns experiment.handlers.categories
  (:require [experiment.services.categories.list :as category-service]
            [experiment.serializers.category :as category-serializer]
            [cheshire.core :refer :all]))

(defn- response
  ([status]
   {:status status})
  ([status body]
   {:status status
    :body (generate-string body)
    :headers {"Content-Type" "application/json"}}))

(defn categories
  []
  (let [categories (category-service/call)]
    (response 200 (category-serializer/serialize categories :is-collection true))))
