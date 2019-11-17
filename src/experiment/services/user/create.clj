(ns experiment.services.user.create
  (:require [experiment.repositories.user :as user-repo ]
            [experiment.specs.sign-up :as spec]))

(defn- user-exists?
  ""
  [user]
  (not= nil (user-repo/by-email (:email user))))

(defn call
  "#TODO"
  [attrs]
  (cond
    (spec/invalid? attrs) [:invalid-attributes (spec/errors attrs)]
    (user-exists? attrs) [:user-already-exists]
    :else [:user-created (user-repo/create attrs)]))
