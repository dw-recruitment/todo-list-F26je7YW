(ns todo.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [todo.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[todo started successfully using the development profile]=-"))
   :middleware wrap-dev})
