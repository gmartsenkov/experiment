(ns experiment.services.serializer-test
  (:require [clojure.test :refer :all]
            [experiment.services.serializer :refer :all]))

(deftest test-serialize
  (testing "type"
    (let [object {}
          options {:type "users" :attributes '()}]
      (is (= "users" (:type (serialize object options))))))
  (testing "id"
    (let [object {:id 66}
          options {:type "users" :attributes '()}]
      (is (= 66 (:id (serialize object options))))))
  (testing "attributes"
    (let [object {:id 1 :age 16 :name "Billy" :gender "male"}
          options {:type "users" :attributes '(:age :name :location)}]
      (is (= {:age 16 :name "Billy" :location nil}
             (:attributes (serialize object options)))))))
