(ns experiment.repositories.user
  (:require [clojure.java.jdbc :as jdbc]
            [experiment.core :refer [database]]
            [experiment.repositories.helpers :refer :all]))

(defn create [attributes]
  "Inserts a user into the database"
  (first (jdbc/insert! database :users (add-timestamps attributes))))
