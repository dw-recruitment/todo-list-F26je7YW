(ns todo.core
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))

(defroutes app
           (GET "/" [] "<iframe src=\"//giphy.com/embed/l0t2DBLGpsd4A\" width=\"480\" height=\"597\" frameBorder=\"0\" class=\"giphy-embed\" allowFullScreen></iframe><p><a href=\"http://giphy.com/gifs/please-construction-patient-l0t2DBLGpsd4A\">via GIPHY</a></p>")
           (route/not-found "<h1>Page not found</h1>"))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev [& [port]]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. (or port 3000))}))
