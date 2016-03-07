(ns todo.core
  (:require [todo.model :as model]
            [todo.views :as views])
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [redirect]])
  (:gen-class))

(def db "jdbc:postgresql://localhost/todos")

(let []
  (when (nil? (model/get-todos db))
    (do
      (model/add-todo! db "foo" "desc 1")
      (model/add-todo! db "bar" "desc 2"))))

(defn handle-post-new-todo [req]
  (let [name (get-in req [:params "name"])
        description (get-in req [:params "description"])
        _ (model/add-todo! db name description)]
    (redirect "/")))

(defroutes app-routes
           (GET "/" [] (views/index (model/get-todos db)))
           (POST "/add-new-todo" [] handle-post-new-todo)
           (GET "/about" [] "<h1>About</h1><h2>This project is about getting stuff done!</h2>")
           (route/not-found "<h1>Page not found</h1>"))
(def app
        (-> app-routes
            wrap-params))

(defn -main [port]
  (model/create-table! db)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev [& [port]]
  (model/create-table! db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. (or port 3000))}))
