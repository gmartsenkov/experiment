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
          error '({:path [:password] :pred clojure.core/string? :val 1234})]
      (is (= [:invalid-attributes error] (service/call invalid-params)))))
  (testing "when user already exists"
    (let [user (db/factory-build :user {})
          params {:email (:email user) :first_name "Jon" :last_name "Snow" :password "1234"}]
      (is (= [:user-already-exists] (service/call params)))))
  (testing "when params are valid and user does not exist"
    (with-clock (mock-clock 0)
      (let [params {:email "jon@snow.com" :first_name "Jon" :last_name "Snow" :password "1234"}
            now (sql-timestamp 1970 1 1 1)
            expected-user (conj params {:id 2 :updated_at now :created_at now})]
        (is (= [:user-created expected-user] (service/call params)))))))
