(ns experiment.services.user.login-test
  (:require [clojure.test :refer :all]
            [experiment.support.database :as db]
            [experiment.services.bcrypt :as bcrypt]
            [experiment.services.jwt :as jwt]
            [experiment.services.user.login :as service]))

(use-fixtures :each db/clear-db-fixture)

(def expected-token
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2ZpcnN0X25hbWUiOiJKb24iLCJ1c2VyX2lkIjoyLCJ1c2VyX2xhc3RfbmFtZSI6IlNub3ciLCJpc3MiOiJleHBlcmltZW50In0.utYoO7VcdY3759WLGZYyrojk-OvcHoCZdkIl_rwWfSE")

(deftest call
  (testing "when the params are invalid"
    (let [invalid-params {:email 1 :password 2}
          errors '({:path [:email], :pred clojure.core/string?, :val 1}
                   {:path [:password], :pred clojure.core/string?, :val 2})]
      (is (= [:invalid-attributes errors] (service/call invalid-params)))))
  (testing "when user does not exist"
    (let [params {:email "jon@snow.com" :password "1234"}]
      (is (= [:user-does-not-exist] (service/call params)))))
  (testing "when password does not match"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "jon@snow.com" :password password})
          params {:email "jon@snow.com" :password "incorrect"}]
      (is (= [:incorrect-password] (service/call params)))))
  (testing "when login information is correct"
    (let [password (bcrypt/encrypt "1234")
          user (db/factory-build :user {:email "rob@stark.com" :password password})
          params {:email "rob@stark.com" :password "1234"}
          decoded-token {:user {:id 2 :first_name "Jon" :last_name "Snow"}}
          expected-user {:id 2 :first_name "Jon" :last_name "Snow" :email "rob@stark.com"
                         :token expected-token}
          [response user] (service/call params)]
      (is (= :signed-in response))
      (is (= expected-user user))
      (is (= decoded-token (jwt/decode (user :token)))))))
