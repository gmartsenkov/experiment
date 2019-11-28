(ns experiment.handlers.users
  (:require [experiment.services.user.create :as signup-service]
            [experiment.services.user.login :as login-service]
            [experiment.services.user.profile :as profile-service]
            [cheshire.core :refer :all]))

(defn- response
  ([status]
   {:status status})
  ([status body]
   {:status status
    :body (generate-string body)
    :headers {"Content-Type" "application/json"}}))

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

(defn profile
  [request]
  (let [user-id (get-in request [:user :id])
        [msg data] (profile-service/call user-id)]
    (case msg
      :user-not-found (response 404)
      :profile-show (response 200 data))))
