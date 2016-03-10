(ns todo.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[todo started successfully]=-"))
   :middleware identity})
