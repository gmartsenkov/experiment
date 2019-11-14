(ns experiment.repositories.user
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]
            [experiment.repositories.helpers :refer :all]))

(defn by_id
  "Fetches the user by id"
  [id]
  (first (jdbc/query database ["SELECT * FROM USERS WHERE ID = ?" id])))

(defn create
  "Inserts a user into the database"
  [attributes]
  (first (jdbc/insert! database :users (add-timestamps attributes))))

(defn update
  "Updates a user into the database"
  [id attributes]
  (by_id
   (first (jdbc/update! database :users
                       (touch attributes)
                       ["id = ?" id]))))
