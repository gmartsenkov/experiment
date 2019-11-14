(ns experiment.support.database
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]))

(defn clear-db-fixture [function]
  (jdbc/execute! database ["TRUNCATE TABLE users RESTART IDENTITY"])
  (function))

(defn count [relation]
  (->
   (jdbc/query database [(format "SELECT COUNT(ID) FROM %s" relation)]
               {:result-set-fn first})
   :count))
