(ns experiment.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [experiment.handler :refer :all]))

(deftest test-app
  (testing "ping route"
    (let [response (app (mock/request :get "/api/ping"))]
      (is (= (:status response) 200))
      (is (= (:body response) "pong")))))
