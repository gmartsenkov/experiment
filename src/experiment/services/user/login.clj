(ns experiment.services.user.login
  (:require [experiment.services.bcrypt :as bcrypt]
            [experiment.repositories.user :as user-repo]
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
  [login]
  (let [user (get-user login)]
      (cond
        (spec/invalid? login) [:invalid-attributes (spec/errors login)]
        (= user nil) [:user-does-not-exist]
        (not(passwords-match? user login)) [:incorrect-password]
        :else [:signed-in (jwt/encode user)])))
