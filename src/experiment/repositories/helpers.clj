(ns experiment.repositories.helpers
  (:use [java-time]))

(defn add-timestamps [map]
  (let [time-now (sql-timestamp (local-date))]
    (conj map
          {:created_at time-now
           :updated_at time-now})
    ))
