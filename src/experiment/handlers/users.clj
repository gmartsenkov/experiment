(ns experiment.handlers.users
  (:require [experiment.services.user.create :as create-service]))

(defn- response
  ([status]
   {:status status})
  ([status body]
   {:status status
    :body (pr-str body)
    :headers {"content/type" "application/edn"}}))

(defn sign-up
  [body]
  (let [[msg data] (create-service/call body)]
    (case msg
      :invalid-attributes (response 400 data)
      :user-already-exists (response 400)
      :user-created (response 200 data))))
