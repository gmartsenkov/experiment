(ns experiment.services.user.profile-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.services.user.profile :as service]))

(use-fixtures :each db/clear-db-fixture)

(deftest test-call
  (testing "when user does not exist"
    (let [user-id 0]
      (is (= [:user-not-found] (service/call user-id)))))
  (testing "when user exists do"
    (let [user-1 (db/factory-build :user {:email "jon@email"})
          user-2 (db/factory-build :user {:email "rob@email"})
          expected {:id 1 :first_name "Jon" :last_name "Snow" :email "jon@email"}]
      (is (= [:profile-show expected] (service/call (:id user-1)))))))
