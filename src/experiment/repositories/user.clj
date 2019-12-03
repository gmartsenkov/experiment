(ns experiment.repositories.user
  (:refer-clojure :exclude [update])
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]
            [experiment.repositories.helpers :refer :all]))

(def ^:private table :users)

(defn by-id
  "Fetches the user by id"
  [id]
  (first (jdbc/query database (sql/format
                               {:select [:*]
                                :from [table]
                                :where [:= :id id]}))))

(defn by-email
  "Fetches the user by email"
  [email]
  (first (jdbc/query database (sql/format
                               {:select [:*]
                                :from [table]
                                :where [:= :email email]}))))

(defn create
  "Inserts a user into the database"
  [attributes]
  (by-id
   (:id (first (jdbc/insert! database table (add-timestamps attributes))))))

(defn update
  "Updates a user into the database"
  [id attributes]
  (by-id
   (first (jdbc/update! database :users
                        (touch attributes)
                        ["id = ?" id]))))
