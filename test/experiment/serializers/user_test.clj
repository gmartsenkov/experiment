(ns experiment.serializers.user-test
  (:require [clojure.test :refer :all]
            [experiment.serializers.user :refer :all]))

(deftest test-serialize
  (testing "serializes the user in JSONAPI format"
    (let [user {:id 1 :first_name "Jon" :last_name "Snow" :email "jon@snow"}]
      (is (= {:data
              {:id 1
               :type "users"
               :attributes {
                            :first_name "Jon"
                            :last_name "Snow"
                            :email "jon@snow"}}}
             (serialize user))))))
