(ns experiment.services.serializer
  (:refer-clojure :exclude [type]))

(defn- empty-attributes-map
  "Generates a map from keys
  Example: (empty-attributes-map '(:name :age))
  #=> {:name nil :age nil}"
  [attributes]
  (into {} (map (fn [key] {key nil}) attributes)))

(defn serialize
  "Serializes an object in JSONApi format"
  [object options]
  (let [attributes (:attributes options)
        type (:type options)]
    {:data
     {:id (:id object)
      :type type
      :attributes (merge
                   (empty-attributes-map attributes)
                   (select-keys object attributes))}}))
