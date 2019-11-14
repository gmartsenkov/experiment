(ns experiment.repositories.user-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.repositories.user :refer :all]
            [java-time :refer [with-clock mock-clock sql-timestamp]]))


(use-fixtures :each db/clear-db-fixture)

(deftest test-create
  (with-clock (mock-clock 0)
    (let [now (sql-timestamp 1970 1 1 1)
          attributes {:first_name "Jon"
                      :last_name "Snow"
                      :email "jon@email"
                      :password "secret"}
          expected {:id 2
                    :first_name "Jon"
                    :last_name "Snow"
                    :email "jon@email"
                    :password "secret"
                    :created_at now
                    :updated_at now}]
      (testing "creates a record in the database"
        (do
          (is (= 0 (db/count "users")))
          (create attributes)
          (is (= 1 (db/count "users")))))
      (testing "it returns the user"
        (is (= expected (create attributes)))))))
