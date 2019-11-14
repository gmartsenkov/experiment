(ns experiment.repositories.user
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]
            [experiment.repositories.helpers :refer :all]))

(defn create
  "Inserts a user into the database"
  [attributes]
  (first (jdbc/insert! database :users (add-timestamps attributes))))

(defn update
  "Updates a user into the database"
  [id attributes]
  (first (jdbc/insert!
          database
          :users
          (touch attributes)
          ["id is ?" id])))
