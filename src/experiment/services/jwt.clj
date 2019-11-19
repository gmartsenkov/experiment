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
  [claims]
  {:user {:id (-> claims (.get "user_id") .asInt)
          :first_name (-> claims (.get "user_first_name") .asString)
          :last_name (-> claims (.get "user_last_name") .asString)}})

(defn encode
  [user]
  (-> (JWT/create)
      (.withIssuer issuer)
      (.withClaim "user_id" (:id user))
      (.withClaim "user_first_name" (:first_name user))
      (.withClaim "user_last_name" (:last_name user))
      (.sign algorithm)))

(defn decode
  [token]
  (try
    (-> verifier
      (.verify token)
      .getClaims
      (map-claims))
    (catch Exception e :invalid-token)))
