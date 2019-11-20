(ns experiment.specs.login
  (:require [clojure.spec.alpha :as s]
            [experiment.specs.errors :refer [format-error]]))

(s/def ::email string?)
(s/def ::password string?)

(s/def ::login (s/keys :req-un [::email
                                ::password]))

(defn invalid?
  "Validates the attributes against the login spec"
  [attrs]
  (= (s/valid? ::login attrs) false))

(defn errors
  "Returns the validation errors"
  [attrs]
  (format-error (s/explain-data ::login attrs)))
