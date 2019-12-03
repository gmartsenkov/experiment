(ns experiment.services.user.create
  (:require [experiment.services.bcrypt :as bcrypt]
            [experiment.repositories.user :as user-repo]
            [experiment.specs.sign-up :as spec]))

(defn- user-exists?
  [user]
  (not= nil (user-repo/by-email (:email user))))

(defn- encrypt-password
  [user]
  (assoc user :password (bcrypt/encrypt (:password user))))

(defn call
  [data]
  (let [attributes (get-in data [:data :attributes])]
    (cond
      (not (spec/valid? data)) [:invalid-attributes (spec/errors data)]
      (user-exists? attributes) [:user-already-exists]
      :else (let [user-with-encrypted-password (encrypt-password attributes)]
              [:user-created]))))
