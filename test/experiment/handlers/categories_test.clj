(ns experiment.handlers.categories-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [experiment.support.database :as db]
            [experiment.handler :refer :all]
            [cheshire.core :refer :all]))

(use-fixtures :each db/clear-db-fixture)

(deftest test-categories
  (testing "returns all categories"
    (let [category-1 (db/factory-build :category {:id 1 :name "Tech"})
          category-2 (db/factory-build :category {:id 2 :name "Beauty"})
          response (app (mock/request :get "/api/categories"))
          expected-body (generate-string [{:data {:id 2 :type "categories" :attributes {:name "Beauty"}}}
                                          {:data {:id 1 :type "categories" :attributes {:name "Tech"}}}])]
      (is (= 200 (:status response)))
      (is (= expected-body (:body response))))))
