(ns experiment.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.adapter.jetty :refer [run-jetty]]
            [experiment.handlers.users :as users]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:gen-class))

(defroutes app-routes
  (context "/api" []
    (GET "/ping" [] "pong")
    (context "/users" []
      (POST "/signup" [:as req] (users/sign-up req))
      (POST "/login" [:as req] (users/login req))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults
   (-> app-routes
       wrap-keyword-params
       wrap-json-params
       (wrap-cors :access-control-allow-origin [#"http://localhost:3000"]
                  :access-control-allow-methods [:get :put :post :delete]))
   (assoc-in site-defaults [:security :anti-forgery] false)))

(defn -main [& args]
  (let [port (Integer/valueOf (System/getenv "PORT"))]
    (run-jetty #'app {:port port :join? true})))
