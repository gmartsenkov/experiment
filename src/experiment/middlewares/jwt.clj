(ns experiment.middlewares.jwt
  (:require [ring.util.response :refer [response status]]
            [experiment.services.jwt :refer [decode]]))

(defn- authorized? [request]
  (let [token (get-in request [:headers "authorization"])]
    (when token
      (let [decoded (decode token)]
        (case decoded
          :invalid-token false
          decoded)))))

(defn- exception? [uri exceptions] (some #{uri} exceptions))

(defn wrap-auth [handler options]
  (fn [request]
    (let [exceptions (:exceptions options)
          uri (request :uri)]
      (if (exception? uri exceptions)
        (handler request)
        (let [decoded (authorized? request)]
          (if decoded
            (->
             request
             (assoc :user (:user decoded))
             handler)
            {:status 401}))))))
