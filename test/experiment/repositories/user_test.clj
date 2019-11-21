(ns experiment.repositories.user-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.repositories.user :as repo]
            [java-time :refer [with-clock mock-clock sql-timestamp]]))


(use-fixtures :each db/clear-db-fixture)

(deftest test-by-id
  (let [user (db/factory-build :user {:email "rob@email"})
        user-2 (db/factory-build :user {:email "bob@email"})
        find (fn [u] (repo/by-id (:id u)))]
    (testing "returns nil when user does not exist"
      (is (= nil (repo/by-id 0))))
    (testing "it returns correct user"
      (is (= 1 (:id (find user))))
      (is (= "rob@email" (:email (find user)))))
    (testing "it works with another user"
      (is (= 2 (:id (find user-2))))
      (is (= "bob@email" (:email (find user-2)))))))

(deftest test-by-email
  (let [user (db/factory-build :user {:email "rob@email"})
        user-2 (db/factory-build :user {:email "bob@email"})
        find (fn [u] (repo/by-email (:email u)))]
    (testing "returns nil when user does not exist"
      (is (= nil (repo/by-id 0))))
    (testing "it returns correct user"
      (is (= 1 (:id (find user))))
      (is (= "rob@email" (:email (find user)))))
    (testing "it works with another user"
      (is (= 2 (:id (find user-2))))
      (is (= "bob@email" (:email (find user-2)))))))

(deftest test-create
  (with-clock (mock-clock 0 "Europe/London")
    (let [now (sql-timestamp 1970 1 1 1)
          attributes {:first_name "Jon"
                      :last_name "Snow"
                      :email "jon@email.com"
                      :password "secret"}
          expected {:id 1
                    :first_name "Jon"
                    :last_name "Snow"
                    :email "jon@email.com"}
          expected-db-row {:id 1
                           :first_name "Jon"
                           :last_name "Snow"
                           :email "jon@email.com"
                           :password "secret"
                           :updated_at now
                           :created_at now}
          create (fn [] (repo/create attributes))]
      (testing "creates a record in the database"
        (do
          (is (= 0 (db/count "users")))
          (create)
          (is (= 1 (db/count "users")))))
      (testing "it returns the user"
        (do
          (db/clear-db)
          (is (= expected (create)))))
      (testing "it creates the correct row in the database"
        (do
          (db/clear-db)
          (is (= expected-db-row (db/fetch (:id (create)) "users"))))))))

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
                    :email "rob@email"}
          update (fn [] (repo/update user-id attributes))]
      (testing "does not create a record in the database"
        (do
          (is (= 1 (db/count "users")))
          (update)
          (is (= 1 (db/count "users")))))
      (testing "returns the updated user"
        (is (= expected (update)))))))
