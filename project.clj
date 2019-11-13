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
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]
            [migratus-lein "0.7.2"]
            [lein-environ "1.1.0"]]
  :ring {:handler experiment.handler/app}
  :migratus {:store :database
           :migration-dir "migrations"
           :db (get (System/getenv) "DATABASE_URL")}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
