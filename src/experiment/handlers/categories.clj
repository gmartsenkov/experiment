(ns experiment.handlers.categories
  (:require [experiment.services.categories.list :as category-services]
            [experiment.serializers.user :as user-serializer]
            [cheshire.core :refer :all]))

(defn- response
  ([status]
   {:status status})
  ([status body]
   {:status status
    :body (generate-string body)
    :headers {"Content-Type" "application/json"}}))

(defn profile
  [request]
  (let [user-id (get-in request [:user :id])
        [msg user] (profile-service/call user-id)]
    (case msg
      :user-not-found (response 404)
      :profile-show (response 200 (user-serializer/serialize user)))))
