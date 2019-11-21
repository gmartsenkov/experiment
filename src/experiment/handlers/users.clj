(ns experiment.handlers.users
  (:require [experiment.services.user.create :as signup-service]
            [experiment.services.user.login :as login-service]))

(defn- response
  ([status]
   {:status status})
  ([status body]
   {:status status
    :body (pr-str body)
    :headers {"content/type" "application/edn"}}))

(defn sign-up
  [body]
  (let [[msg data] (signup-service/call body)]
    (case msg
      :invalid-attributes (response 400 data)
      :user-already-exists (response 400)
      :user-created (response 200 data))))

(defn login
  [body]
  (let [[msg data] (login-service/call body)]
    (case msg
      :invalid-attributes (response 400 data)
      :user-does-not-exist (response 404)
      :incorrect-password (response 404)
      :signed-in (response 200 {:token data}))))
