(ns experiment.handlers.users-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [experiment.support.database :as db]
            [experiment.services.bcrypt :as bcrypt]
            [experiment.handler :refer :all]
            [cheshire.core :refer :all]))

(use-fixtures :each db/clear-db-fixture)

(def jwt-token
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyLCJpc3MiOiJleHBlcmltZW50In0.FOf6m_b6qsUxkMcMgZ75S3tN7MzGzmcC7x2u-tj4Cp0")

(deftest test-sign-up
  (testing "with wrong params"
    (let [body (generate-string {:email "rob@stark" :first_name 3 :last_name 4 :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/signup")
                         (mock/content-type "application/json")
                         (mock/body body)))
          expected-body (generate-string '({:path [:first_name], :pred clojure.core/string?, :val 3} {:path [:last_name], :pred clojure.core/string?, :val 4}))]
      (is (= 400 (:status response)))
      (is (= expected-body (:body response)))))
  (testing "when user already exists"
    (let [user (db/factory-build :user {})
          body (generate-string {:email (:email user) :first_name "Jon" :last_name "Snow" :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/signup")
                         (mock/content-type "application/json")
                         (mock/body body)))]
      (is (= 400 (:status response)))
      (is (= "" (:body response)))))
  (testing "when params are correct and user does not exist"
    (let [body (generate-string {:email "rob@stark" :first_name "Rob" :last_name "Stark" :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/signup")
                         (mock/content-type "application/json")
                         (mock/body body)))
          expected-body (generate-string {:id 2, :first_name "Rob", :last_name "Stark", :email "rob@stark"})]
      (is (= 200 (:status response)))
      (is (= expected-body (:body response))))))

(deftest test-login
  (testing "when params are wrong"
    (let [body (generate-string {:email "rob@stark"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/json")
                         (mock/body body)))
          expected-body (generate-string '({:path [], :pred (clojure.core/fn [%] (clojure.core/contains? % :password)), :val {:email "rob@stark"}}))]
      (is (= 400 (:status response)))
      (is (= expected-body (:body response)))))
  (testing "when user does not exist"
    (let [body (generate-string {:email "rob@stark" :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/json")
                         (mock/body body)))]
      (is (= 404 (:status response)))))
  (testing "when password is wrong"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "rob@stark.com" :password password})
          body (generate-string {:email (:email user) :password "incorrect"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/json")
                         (mock/body body)))]
      (is (= 404 (:status response)))))
  (testing "when email and password are correct"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "jon@snow.com" :password password})
          body (generate-string {:email (:email user) :password "1234"})
          response (app (->
                         (mock/request :post "/api/users/login")
                         (mock/content-type "application/json")
                         (mock/body body)))
          expected-body (generate-string {:token jwt-token})]
      (is (= 200 (:status response)))
      (is (= expected-body (:body response))))))

(deftest test-profile
  (testing "when not authorized"
    (let [response (app (->
                         (mock/request :get "/api/users/profile")
                         (mock/content-type "application/json")))]
      (is (= 401 (:status response)))))
  (testing "when authorized"
    (testing "when user does not exist"
      (let [response (app (->
                           (mock/request :get "/api/users/profile")
                           (mock/content-type "application/json")
                           (mock/header "Authorization" jwt-token)))]
        (is (= 404 (:status response)))))
    (testing "when the user exists"
      (db/clear-db)
      (let [user (db/factory-build :user {:id 2 :email "jon@snow.com"})
            response (app (->
                           (mock/request :get "/api/users/profile")
                           (mock/content-type "application/json")
                           (mock/header "Authorization" jwt-token)))]
        (is (= 200 (:status response)))))))
