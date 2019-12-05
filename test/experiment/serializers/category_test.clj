(ns experiment.serializers.category-test
  (:require [clojure.test :refer :all]
            [experiment.serializers.category :refer :all]))

(deftest test-serialize
  (testing "serializes the user in JSONAPI format"
    (let [category {:id 1 :name "Tech"}]
      (is (= {:data
              {:id 1
               :type "categories"
               :attributes {:name "Tech"}}}
             (serialize category)))))
  (testing "serializes a collection in JSONAPI format"
    (let [categories [{:id 1 :name "Tech"} {:id 2 :name "Beauty"}]]
      (is (= [{:data {:id 1 :type "categories" :attributes {:name "Tech"}}}
              {:data {:id 2 :type "categories" :attributes {:name "Beauty"}}}]
             (serialize categories :is-collection true))))))
