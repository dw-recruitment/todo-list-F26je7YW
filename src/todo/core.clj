(ns todo.core
  (:require [todo.model :as model]
            [todo.views :as views])
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))

(def db "jdbc:postgresql://localhost/todos")

(let []
  (when (nil? (model/get-todos db))
    (do
      (model/add-todo! db "foo" "desc 1")
      (model/add-todo! db "bar" "desc 2"))))

(defroutes app
           (GET "/" [] (views/index (model/get-todos db)))
           (GET "/about" [] "<h1>About</h1><h2>This project is about getting stuff done!</h2>")
           (route/not-found "<h1>Page not found</h1>"))

(defn -main [port]
  (model/create-table! db)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev [& [port]]
  (model/create-table! db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. (or port 3000))}))
