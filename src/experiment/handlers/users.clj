(ns experiment.handlers.users
  (:require [experiment.services.user.create :as signup-service]
            [experiment.services.user.login :as login-service]))

(defn- response
  ([status]
   {:status status})
  ([status body]
   {:status status
    :body (pr-str body)
    :headers {"Content-Type" "application/edn"}}))

(defn sign-up
  [request]
  (let [params (:params request)
        [msg data] (signup-service/call params)]
    (case msg
      :invalid-attributes (response 400 data)
      :user-already-exists (response 400)
      :user-created (response 200 data))))

(defn login
  [request]
  (let [params (:params request)
        [msg data] (login-service/call params)]
    (case msg
      :invalid-attributes (response 400 data)
      :user-does-not-exist (response 404)
      :incorrect-password (response 404)
      :signed-in (response 200 {:token data}))))
