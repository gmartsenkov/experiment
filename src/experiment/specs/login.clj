(ns experiment.specs.login
  (:require [clojure.spec.alpha :as s]
            [experiment.specs.errors :refer [explain]]))

(s/def ::email string?)
(s/def ::password string?)
(s/def ::type #(= % "users"))

(s/def ::attributes (s/keys :req-un [::email
                                     ::password]))
(s/def ::data (s/keys :req-un [::type ::attributes]))
(s/def ::login (s/keys :req-un [::data]))

(defn valid?
  "Validates the attributes against the login spec"
  [attrs]
  (s/valid? ::login attrs))

(defn errors
  "Returns the validation errors"
  [data]
  (explain ::login data))
