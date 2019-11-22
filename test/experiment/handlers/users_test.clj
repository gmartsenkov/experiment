(ns experiment.handlers.users-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [experiment.support.database :as db]
            [experiment.services.bcrypt :as bcrypt]
            [experiment.handler :refer :all]))

(use-fixtures :each db/clear-db-fixture)

(deftest test-sign-up
  (testing "with wrong params"
    (let [body (pr-str {:email "rob@stark" :first_name 3 :last_name 4 :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/signup")
                         (mock/content-type "application/edn")
                         (mock/body body)))
          expected-body "({:path [:first_name], :pred clojure.core/string?, :val 3} {:path [:last_name], :pred clojure.core/string?, :val 4})"]
      (is (= 400 (:status response)))
      (is (= expected-body (:body response)))))
  (testing "when user already exists"
    (let [user (db/factory-build :user {})
          body (pr-str {:email (:email user) :first_name "Jon" :last_name "Snow" :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/signup")
                         (mock/content-type "application/edn")
                         (mock/body body)))]
      (is (= 400 (:status response)))
      (is (= "" (:body response)))))
  (testing "when params are correct and user does not exist"
    (let [body (pr-str {:email "rob@stark" :first_name "Rob" :last_name "Stark" :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/signup")
                         (mock/content-type "application/edn")
                         (mock/body body)))
          expected-body "{:id 2, :first_name \"Rob\", :last_name \"Stark\", :email \"rob@stark\"}"]
      (is (= 200 (:status response)))
      (is (= expected-body (:body response))))))

(deftest test-login
  (testing "when params are wrong"
    (let [body (pr-str {:email "rob@stark"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/edn")
                         (mock/body body)))
          expected-body "({:path [], :pred (clojure.core/fn [%] (clojure.core/contains? % :password)), :val {:email \"rob@stark\"}})"]
      (is (= 400 (:status response)))
      (is (= expected-body (:body response)))))
  (testing "when user does not exist"
    (let [body (pr-str {:email "rob@stark" :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/edn")
                         (mock/body body)))]
      (is (= 404 (:status response)))))
  (testing "when password is wrong"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "rob@stark.com" :password password})
          body (pr-str {:email (:email user) :password "incorrect"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/edn")
                         (mock/body body)))]
      (is (= 404 (:status response)))))
  (testing "when email and password are correct"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "jon@snow.com" :password password})
          body (pr-str {:email (:email user) :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/edn")
                         (mock/body body)))
          expected-body "{:token \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2ZpcnN0X25hbWUiOiJKb24iLCJ1c2VyX2lkIjoyLCJ1c2VyX2xhc3RfbmFtZSI6IlNub3ciLCJpc3MiOiJleHBlcmltZW50In0.utYoO7VcdY3759WLGZYyrojk-OvcHoCZdkIl_rwWfSE\"}"]
      (is (= 200 (:status response)))
      (is (= expected-body (:body response))))))
