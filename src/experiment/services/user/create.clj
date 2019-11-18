(ns experiment.services.user.create
  (:require [experiment.repositories.user :as user-repo ]
            [experiment.specs.sign-up :as spec]
            [buddy.hashers :as hasher]))

(defn- user-exists?
  ""
  [user]
  (not= nil (user-repo/by-email (:email user))))

(defn- encrypt-password
  [user]
  (conj user {:password (hasher/derive (:password user))}))

(defn call
  "#TODO"
  [attrs]
  (cond
    (spec/invalid? attrs) [:invalid-attributes (spec/errors attrs)]
    (user-exists? attrs) [:user-already-exists]
    :else (let [user-with-encrypted-password (encrypt-password attrs)]
            [:user-created (user-repo/create user-with-encrypted-password)])))
