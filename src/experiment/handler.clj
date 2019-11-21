(ns experiment.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [experiment.handlers.users :as users]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:gen-class))

(defn- read-edn
  [string]
  (try
    (read-string string)
    (catch Exception e {})))

(defroutes app-routes
  (context "/api" []
    (GET "/ping" [] "pong")
    (context "/users" []
      (POST "/signup" [body] (users/sign-up (read-edn body)))
      (POST "/login" [body] (users/login (read-edn body)))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults
   app-routes
   (assoc-in site-defaults [:security :anti-forgery] false)))

(defn -main [& [port]]
  (let [port (Integer. (or port (System/getenv :port) 5000))]
    (run-jetty 'app {:port port :join? false})))
