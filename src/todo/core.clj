(ns todo.core
  (:require [todo.model :as model]
            [todo.views :as views])
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
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

(defn handle-update-todo [req]
  (let [id (java.util.UUID/fromString (get-in req [:params "id"]))
        checked (= "true" (get-in req [:params "checked"]))
        transaction (model/update-todo db id checked)]
    (if transaction
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body (str id)}
      {:status 400
       :headers {"Content-Type" "text/plain"}
       :body "no can do!"})))

(defn handle-delete-todo [req]
  (let [id (java.util.UUID/fromString (get-in req [:params "id"]))
        transaction (model/delete-item db id)]
    (if transaction
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body (str id)}
      {:status 400
       :headers {"Content-Type" "text/plain"}
       :body "no can do!"})))

(defroutes app-routes
           (GET "/" [] (views/index (model/get-todos db)))
           (POST "/add-new-todo" [] handle-post-new-todo)
           (PUT "/update-todo" [] handle-update-todo)
           (DELETE "/delete-todo" [] handle-delete-todo)
           (GET "/about" [] "<h1>About</h1><h2>This project is about getting stuff done!</h2>")
           (route/not-found "<h1>Page not found</h1>"))
(def app
  (-> app-routes
      wrap-params
      (wrap-file "resources/public")
      (wrap-content-type)
      (wrap-not-modified)))

(defn -main [port]
  (model/create-table! db)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev [& [port]]
  (model/create-table! db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. (or port 3000))}))
