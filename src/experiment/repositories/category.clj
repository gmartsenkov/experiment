(ns experiment.repositories.category
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]))

(def ^:private table :categories)

(defn all
  "Fetches all the categories"
  []
  (jdbc/query database (sql/format
                        {:select [:*]
                         :order-by [[:name :asc]]
                         :from [table]})))
