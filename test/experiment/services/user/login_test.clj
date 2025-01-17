(ns experiment.services.user.login-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.services.bcrypt :as bcrypt]
            [experiment.services.jwt :as jwt]
            [experiment.services.user.login :as service]))

(use-fixtures :each db/clear-db-fixture)

(def expected-token
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyLCJpc3MiOiJleHBlcmltZW50In0.FOf6m_b6qsUxkMcMgZ75S3tN7MzGzmcC7x2u-tj4Cp0")

(deftest call
  (testing "when the params are invalid"
    (let [invalid-params {:email "jon@snow.com" :password "1234"}
          errors [{:path [] :msg "Data is missing."}]]
      (is (= [:invalid-attributes errors] (service/call invalid-params)))))
  (testing "when user does not exist"
    (let [params {:data {:type "users"
                         :attributes {:email "jon@snow.com" :password "1234"}}}]
      (is (= [:user-does-not-exist] (service/call params)))))
  (testing "when password does not match"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "jon@snow.com" :password password})
          params {:data {:type "users"
                         :attributes {:email "jon@snow.com" :password "incorrect"}}}]
      (is (= [:incorrect-password] (service/call params)))))
  (testing "when login information is correct"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "rob@stark.com" :password password})
          params {:data {:type "users"
                         :attributes {:email "rob@stark.com" :password "1234"}}}
          decoded-token {:user {:id 2}}
          [response token] (service/call params)]
      (is (= :signed-in response))
      (is (= decoded-token (jwt/decode token))))))
