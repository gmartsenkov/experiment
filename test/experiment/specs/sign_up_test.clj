(ns experiment.specs.sign-up-test
  (:require [clojure.test :refer :all]
            [experiment.specs.sign-up :refer :all]))

(deftest test-valid?
  (testing "when valid"
    (let [input {:data
                 {:type "users"
                  :attributes {:email "jon@snow.com"
                               :first_name "Jon"
                               :last_name "Snow"
                               :password "1234"}}}]
      (is (= true (valid? input)))))
  (testing "when invalid"
    (let [input {:data {:type "users"}}]
      (is (= false (valid? input))))))

(deftest test-errors
  (testing "with empty data"
    (is (= [{:path [] :msg "Data is missing."}]
           (errors {}))))
  (testing "with wrong type"
    (is (= [{:path [:data] :msg "Attributes are missing."}
            {:path [:data :type] :msg "Type is not users."}]
           (errors {:data {:type "incorrect"}})))))
