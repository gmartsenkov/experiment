(ns experiment.specs.sign-up
  (:require [clojure.spec.alpha :as s]
            [experiment.specs.errors :refer [format-error]]))

(s/def ::email string?)
(s/def ::first_name string?)
(s/def ::last_name string?)
(s/def ::password string?)

(s/def ::sign-up (s/keys :req-un [::email
                                  ::first_name
                                  ::last_name
                                  ::password]))

(defn invalid?
  "Validates the attributes against the sign-up spec"
  [attrs]
  (= (s/valid? ::sign-up attrs) false))

(defn errors
  "Returns the validation errors"
  [attrs]
  (format-error (s/explain-data ::sign-up attrs)))
