(ns experiment.specs.sign-up
  (:require [clojure.spec.alpha :as s]
            [experiment.specs.errors :refer [explain]]))

(s/def ::email string?)
(s/def ::first_name string?)
(s/def ::last_name string?)
(s/def ::password string?)
(s/def ::type #(= % "users"))

(s/def ::attributes (s/keys :req-un [::email
                                     ::first_name
                                     ::last_name
                                     ::password]))
(s/def ::data (s/keys :req-un [::type ::attributes]))
(s/def ::sign-up (s/keys :req-un [::data]))


(defn valid?
  "Validates the attributes against the sign-up spec"
  [data]
  (= (s/valid? ::sign-up data) true))

(defn errors
  "Returns the validation errors"
  [data]
  (explain ::sign-up data))
