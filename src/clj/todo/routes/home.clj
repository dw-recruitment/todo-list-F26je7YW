(ns todo.routes.home
  (:require [todo.model :as model]
            [todo.layout :as layout])
  (:require [ring.util.response :refer [response]]
            [compojure.core :refer [GET POST PUT DELETE defroutes]]
            [ring.util.response :refer [redirect]]))

(def db "jdbc:postgresql://localhost/todos")

(let []
  (when (nil? (model/get-todos db))
    (do
      (model/add-todo! db "foo" "desc 1")
      (model/add-todo! db "bar" "desc 2"))))

(defn home-page []
  (layout/render "home.html"))

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

(defn handle-get-todos [req]
  (let [todos (model/get-todos db)]
    (when todos
      (response {:data todos}))))

(defroutes home-routes
           (GET "/" [] (home-page))
           (GET "/get-all-todos" [] handle-get-todos)
           (POST "/add-new-todo" [] handle-post-new-todo)
           (PUT "/update-todo" [] handle-update-todo)
           (DELETE "/delete-todo" [] handle-delete-todo))
