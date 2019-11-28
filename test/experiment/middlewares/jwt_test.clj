(ns experiment.middlewares.jwt-test
  (:require [clojure.test :refer :all]
            [experiment.middlewares.jwt :refer :all]))

(def valid-token
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyLCJpc3MiOiJleHBlcmltZW50In0.FOf6m_b6qsUxkMcMgZ75S3tN7MzGzmcC7x2u-tj4Cp0")

(deftest test-wrap-auth
  (let [exceptions ["/ping" "/login"]
        handler (fn [x] x)
        unauthorized {:status 401}
        subject (wrap-auth handler {:exceptions exceptions})]
    (testing "when an exception"
      (is (= {:uri "/ping"} (subject {:uri "/ping"}))))
    (testing "again an exception"
      (is (= {:uri "/login"} (subject {:uri "/login"}))))
    (testing "when not an exception"
      (testing "when header is missing"
        (is (= unauthorized (subject {:uri "/user"}))))
      (testing "when header is there with invalid authorization"
        (is (= unauthorized (subject {:uri "/user" :headers { :authorization "invalid"}}))))
      (testing "when token is valid"
        (let [request {:uri "/user" :headers { :authorization valid-token}}
              expected (merge request {:user { :id 2 }})]
          (is (= expected (subject request))))))))
