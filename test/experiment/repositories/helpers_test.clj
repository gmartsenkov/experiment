(ns experiment.repositories.helpers-test
  (:require [clojure.test :refer :all]
            [experiment.repositories.helpers :refer :all]
            [java-time :refer [with-clock mock-clock sql-timestamp]]))

(deftest test-add-timestamp
  (testing "adds timestamps"
    (with-clock (mock-clock 0 "Europe/London")
      (let [input {:test 1}
            result (add-timestamps input)
            now (sql-timestamp 1970 1 1 1)
            expected {:test 1 :created_at now :updated_at now}]
        (is (= result expected))))))

(deftest test-touch
  (testing "adds updated_at timestamp"
    (with-clock (mock-clock 0 "Europe/London")
      (let [input {:test 1 :updated_at "something"}
            result (touch input)
            now (sql-timestamp 1970 1 1 1)
            expected {:test 1 :updated_at now}]
        (is (= result expected))))))
