(ns experiment.services.serializer
  (:refer-clojure :exclude [type]))

(defn- empty-attributes-map
  "Generates a map from keys
  Example: (empty-attributes-map '(:name :age))
  #=> {:name nil :age nil}"
  [attributes]
  (into {} (map (fn [key] {key nil}) attributes)))

(defn- serialize-single
  [object options]
  (let [attributes (:attributes options)
        type (:type options)]
    {:id (:id object)
     :type type
     :attributes (merge
                  (empty-attributes-map attributes)
                  (select-keys object attributes))}))
(defn serialize
  "Serializes an object in JSONApi format"
  [data options]
  {:data (if (:is-collection options)
           (map #(serialize-single % options) data)
           (serialize-single data options))})
