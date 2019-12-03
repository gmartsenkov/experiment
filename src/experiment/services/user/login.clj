(ns experiment.services.user.login
  (:require [experiment.repositories.user :as user-repo]
            [experiment.services.bcrypt :as bcrypt]
            [experiment.services.jwt :as jwt]
            [experiment.specs.login :as spec]))

(defn- get-user
  [login]
  (user-repo/by-email (str (:email login))))

(defn- passwords-match?
  [user login]
  (bcrypt/match? (:password login) (:password user)))

(defn call
  [data]
  (let [attributes (get-in data [:data :attributes])
        user (get-user attributes)]
    (cond
      (not (spec/valid? data)) [:invalid-attributes (spec/errors data)]
      (= user nil) [:user-does-not-exist]
      (not(passwords-match? user attributes)) [:incorrect-password]
      :else [:signed-in (jwt/encode user)])))
