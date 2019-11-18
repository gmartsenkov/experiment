(ns experiment.support.database
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]
            [experiment.repositories.helpers :refer :all]))

(defn clear-db
  "Helper function to clear the database between tests"
  []
  (jdbc/execute! database ["TRUNCATE TABLE users RESTART IDENTITY"]))

(defn count
  "Helper function to return the row count of a table"
  [relation]
  (->
   (jdbc/query database [(format "SELECT COUNT(ID) FROM %s" relation)]
               {:result-set-fn first})
   :count))

(defn fetch
  "Helper function to fetch a record from the db with all columns"
  [id table]
  (first (jdbc/query database [(str "SELECT * from " table " where id = ?") id])))

(def ^:private default-attributes
  "Contains all the default attributes for the factory-build function"
  {:user {
          :first_name "Jon"
          :last_name "Snow"
          :email "jon@email"
          :password "secret"}})

(defn- insert
  "Inserts a record into the specified table in the database"
  [relation attributes]
  (first (jdbc/insert! database relation (add-timestamps attributes))))

(defn factory-build
  "Helper function to easily create db record in tests"
  [relation attributes]
  (case relation
    :user (insert :users (conj (:user default-attributes) attributes))
    (throw (Exception. (format "Factory %s does not exist" relation)))))

(defn clear-db-fixture
  "Fixture wrapping the clear-db function"
  [function]
  (clear-db)
  (function))
