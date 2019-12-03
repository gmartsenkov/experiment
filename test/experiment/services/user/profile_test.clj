(ns experiment.services.user.profile-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [java-time :refer [with-clock mock-clock sql-timestamp]]
            [experiment.services.user.profile :as service]))

(use-fixtures :each db/clear-db-fixture)

(deftest test-call
  (testing "when user does not exist"
    (let [user-id 0]
      (is (= [:user-not-found] (service/call user-id)))))
  (testing "when user exists do"
    (with-clock (mock-clock 0 "Europe/London")
      (let [now (sql-timestamp 1970 1 1 1)
            user-1 (db/factory-build :user {:email "jon@email"})
            user-2 (db/factory-build :user {:email "rob@email"})
            expected {:id 1
                      :first_name "Jon"
                      :last_name "Snow"
                      :email "jon@email"
                      :password "secret"
                      :updated_at now
                      :created_at now}]
        (is (= [:profile-show expected] (service/call (:id user-1))))))))
