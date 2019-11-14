(ns experiment.repositories.user-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.repositories.user :as repo]
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
                    :updated_at now}
          create (fn [] (repo/create attributes))]
      (testing "creates a record in the database"
        (do
          (is (= 0 (db/count "users")))
          (create)
          (is (= 1 (db/count "users")))))
      (testing "it returns the user"
        (is (= expected (create)))))))

(deftest test-update
  (with-clock (mock-clock 0)
    (let [now (sql-timestamp 1970 1 1 1)
          user (db/factory-build :user {})
          user-id (:id user)
          attributes {:first_name "Rob"
                      :last_name "Stark"
                      :email "rob@email"
                      :password "new_secret"}
          expected {:id 1
                    :first_name "Rob"
                    :last_name "Stark"
                    :email "rob@email"
                    :password "new_secret"
                    :created_at now
                    :updated_at now}
          update (fn [] (repo/update user-id attributes))]
      (testing "does not create a record in the database"
        (do
          (is (= 1 (db/count "users")))
          (update)
          (is (= 1 (db/count "users")))))
      (testing "returns the updated user"
        (is (= expected (update)))))))
