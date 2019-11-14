(ns experiment.repositories.helpers
  (:require [java-time :refer [sql-timestamp local-date]]))

(defn add-timestamps [map]
  "Adds create/update timestamps to map"
  (let [time-now (sql-timestamp (local-date))]
    (conj map
          {:created_at time-now
           :updated_at time-now})
    ))

(defn touch [map]
  "Adds update timestamp to map"
  (let [time-now (sql-timestamp (local-date))]
    (conj map
          {:updated_at time-now})
    ))
