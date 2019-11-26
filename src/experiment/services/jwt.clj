(ns experiment.services.jwt
  (:require [environ.core :refer [env]])
  (:import (com.auth0.jwt algorithms.Algorithm
                          JWT
                          JWTVerifier$BaseVerification)))

(def ^:private secret (env :jwt-secret))
(def ^:private issuer "experiment")
(def ^:private issuer-as-array (into-array String [issuer]))
(def ^:private algorithm (Algorithm/HMAC256 secret))
(def ^:private verifier (->
               algorithm
               (JWT/require)
               (.withIssuer issuer-as-array)
               .build))

(defn- map-claims
  "Map JWT claims into a clojure map"
  [claims]
  {:user {:id (-> claims (.get "user_id") .asInt)}})

(defn encode
  "Generate a JWT token with the some basic user information"
  [user]
  (-> (JWT/create)
      (.withIssuer issuer)
      (.withClaim "user_id" (:id user))
      (.sign algorithm)))

(defn decode
  "Decodes a JWT into a map with the user information
  Returns :invalid-token if token is not valid or expired"
  [token]
  (try
    (-> verifier
      (.verify token)
      .getClaims
      (map-claims))
    (catch Exception e :invalid-token)))
