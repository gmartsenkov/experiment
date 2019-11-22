(ns experiment.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.edn :refer :all]
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
   (wrap-edn-params app-routes)
   (assoc-in site-defaults [:security :anti-forgery] false)))

(defn -main [& args]
  (let [port (Integer/valueOf (System/getenv "PORT"))]
    (run-jetty #'app {:port port :join? true})))
