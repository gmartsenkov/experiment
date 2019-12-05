(ns experiment.repositories.category-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.repositories.category :as repo]
            [java-time :refer [with-clock mock-clock sql-timestamp]]))


(use-fixtures :each db/clear-db-fixture)

(deftest test-all
  (testing "returns all categories"
    (with-clock (mock-clock 0 "Europe/London")
      (let[now (sql-timestamp 1970 1 1 1)
           category-1 (db/factory-build :category {:name "Tech"})
           category-1 (db/factory-build :category {:name "Beauty"})]
        (is (= [{:id 2,
                 :name "Beauty"
                 :created_at now
                 :updated_at now}
                {:id 1,
                 :name "Tech"
                 :created_at now
                 :updated_at now}] (repo/all)))))))
