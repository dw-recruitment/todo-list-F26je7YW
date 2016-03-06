(ns todo.core
  (:require [todo.model :as model])
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))

(def db "jdbc:postgresql://localhost/todos")

(defroutes app
           (GET "/" [] "<iframe src=\"//giphy.com/embed/l0t2DBLGpsd4A\" width=\"480\" height=\"597\" frameBorder=\"0\" class=\"giphy-embed\" allowFullScreen></iframe><p><a href=\"http://giphy.com/gifs/please-construction-patient-l0t2DBLGpsd4A\">via GIPHY</a></p>")
           (GET "/about" [] "<h1>About</h1><h2>This project is about getting stuff done!</h2>")
           (route/not-found "<h1>Page not found</h1>"))

(defn -main [port]
  (model/create-table! db)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev [& [port]]
  (model/create-table! db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. (or port 3000))}))
