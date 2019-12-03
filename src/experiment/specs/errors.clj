(ns experiment.specs.errors
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [phrase.alpha :refer [defphraser phrase-first phrase]]))

(defn- format-keyword
  [key]
  (-> (name key)
      string/capitalize
      (string/replace #"_"  " ")))

(defn is-are
  [word]
  (if (-> (last word) (= \s))
    "are" "is"))

(defphraser #(contains? % key)
  [_ _ key]
  (let [field (format-keyword key)]
    (format "%s %s missing." field (is-are field))))

(defphraser #(= % "users")
  [_ x]
  (let [field (format-keyword (last (:path x)))]
    (str field " " (is-are field) " not users.")))

(defphraser string?
  [_ x]
  (let [field (format-keyword (last (:path x)))]
    (str field " " (is-are field) " not a string.")))

(defn errors-map-func
  [spec error]
  {:path (:path error)
   :msg (phrase spec error)})

(defn explain
  [spec data]
  (let [errors (::s/problems (s/explain-data spec data))]
    (mapv #(errors-map-func spec %) errors)))
