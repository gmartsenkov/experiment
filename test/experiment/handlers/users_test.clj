(ns experiment.handlers.users-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [experiment.support.database :as db]
            [experiment.handler :refer :all]))

(use-fixtures :each db/clear-db-fixture)

(deftest test-sign-up
  (testing "with wrong params"
    (let [
          body (pr-str {:email "rob@stark" :first_name 3 :last_name 4 :password "1234"})
          response (app (mock/request :post "/api/users/signup" {:body body}))
          expected-body "({:path [:first_name], :pred clojure.core/string?, :val 3} {:path [:last_name], :pred clojure.core/string?, :val 4})"]
      (is (= 400 (:status response)))
      (is (= expected-body (:body response)))))
  (testing "when user already exists"
    (let [user (db/factory-build :user {})
          body (pr-str {:email (:email user) :first_name "Jon" :last_name "Snow" :password "1234"})
          response (app (mock/request :post "/api/users/signup" {:body body}))]
      (is (= 400 (:status response)))
      (is (= "" (:body response)))))
  (testing "when params are correct and user does not exist"
    (let [body (pr-str {:email "rob@stark" :first_name "Rob" :last_name "Stark" :password "1234"})
          response (app (mock/request :post "/api/users/signup" {:body body}))
          expected-body "{:id 2, :first_name \"Rob\", :last_name \"Stark\", :email \"rob@stark\"}"]
      (is (= 200 (:status response)))
      (is (= expected-body (:body response))))))
