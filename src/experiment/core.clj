(ns experiment.core
  (:require [environ.core :refer [env]]))

(def database (env :database-url))
