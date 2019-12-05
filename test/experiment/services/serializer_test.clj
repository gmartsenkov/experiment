(ns experiment.services.serializer-test
  (:require [clojure.test :refer :all]
            [experiment.services.serializer :refer :all]))

(deftest test-serialize
  (testing "type"
    (let [object {}
          options {:type "users" :attributes '()}]
      (is (= "users" (-> (serialize object options) :data :type)))))
  (testing "id"
    (let [object {:id 66}
          options {:type "users" :attributes '()}]
      (is (= 66 (-> (serialize object options) :data :id)))))
  (testing "attributes"
    (let [object {:id 1 :age 16 :name "Billy" :gender "male"}
          options {:type "users" :attributes '(:age :name :location)}]
      (is (= {:age 16 :name "Billy" :location nil}
             (-> (serialize object options) :data :attributes)))))
  (testing "is-collection"
    (let [objects [{:id 1 :age 16 :name "Billy" :gender "male"}
                   {:id 2 :age 32 :name "John" :gender "male"}]
          options {:type "users" :attributes '(:age :name :location) :is-collection true}]
      (is (= {:data [{:id 1 :type "users" :attributes {:age 16, :name "Billy", :location nil}}
                     {:id 2 :type "users" :attributes {:age 32, :name "John", :location nil}}]}
             (serialize objects options))))))
