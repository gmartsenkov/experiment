(ns experiment.services.user.profile
  (:require [experiment.repositories.user :as user-repo]))

(defn call
  [user_id]
  (if-let [user (user-repo/by-id user_id)]
    [:profile-show user]
    [:user-not-found]))
