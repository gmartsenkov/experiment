(ns experiment.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [experiment.handlers.users :as users]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn- read-edn
  [string]
  (try
    (read-string string)
    (catch Exception e {})))

(defroutes app-routes
  (context "/api" []
    (GET "/ping" [] "pong")
    (context "/users" []
      (POST "/signup" [body] (users/sign-up (read-edn body)))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults
   app-routes
   (assoc-in site-defaults [:security :anti-forgery] false)))
