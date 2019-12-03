(ns experiment.services.user.create-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.services.user.create :as service]
            [java-time :refer [with-clock mock-clock sql-timestamp]]))

(use-fixtures :each db/clear-db-fixture)

(deftest test-call
  (testing "when params are incorrect"
    (let [invalid-params
          {:email "rob@mail.com" :first_name "Rob" :last_name "Stark" :password 1234}
          errors [{:path [] :msg "Data is missing."}]]
      (is (= [:invalid-attributes errors] (service/call invalid-params)))))
  (testing "when user already exists"
    (let [user (db/factory-build :user {})
          params {:data {:type "users"
                  :attributes {:email (:email user) :first_name "Jon" :last_name "Snow" :password "1234"}}}]
      (is (= [:user-already-exists] (service/call params)))))
  (testing "when params are valid and user does not exist"
    (with-clock (mock-clock 0)
      (let [params {:data {:type "users"
                           :attributes {:email "jon@snow.com" :first_name "Jon" :last_name "Snow" :password "1234"}}}]
        (is (= [:user-created] (service/call params)))))))
