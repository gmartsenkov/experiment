(ns experiment.support.database
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]))

(defn clear-db-fixture
  "Helper function to clear the database between tests"
  [function]
  (jdbc/execute! database ["TRUNCATE TABLE users RESTART IDENTITY"])
  (function))

(defn count
  "Helper function to return the row count of a table"
  [relation]
  (->
   (jdbc/query database [(format "SELECT COUNT(ID) FROM %s" relation)]
               {:result-set-fn first})
   :count))

(defn build-factory
  "Helper function to easily create db record in tests"
  [relation attributes]
  (case relation
    ))

(build-f
