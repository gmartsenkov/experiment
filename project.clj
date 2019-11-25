(defproject experiment "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [migratus "1.2.7"]
                 [org.clojure/java.jdbc "0.7.10"]
                 [org.postgresql/postgresql "42.2.8"]
                 [environ "1.1.0"]
                 [ring/ring-mock "0.4.0"]
                 [clojure.java-time "0.3.2"]
                 [honeysql "0.9.8"]
                 [at.favre.lib/bcrypt "0.9.0"]
                 [com.fasterxml.jackson.core/jackson-databind "2.10.1"]
                 [cheshire "5.9.0"]
                 [com.auth0/java-jwt "3.8.3"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-cors "0.1.13"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-jetty-adapter "1.8.0"]]
  :plugins [[lein-ring "0.12.5"]
            [migratus-lein "0.7.2"]
            [lein-environ "1.1.0"]]
  :ring {:handler experiment.handler/app}
  :uberjar-name "experiment-standalone.jar"
  :migratus {:store :database
           :migration-dir "migrations"
             :db (get (System/getenv) "DATABASE_URL")}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}
   :uberjar { :aot :all :main experiment.handler} })
