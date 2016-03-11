(ns todo.routes.home
  (:require [todo.layout :as layout]
            [clojure.java.jdbc :as jdbc]
            [todo.db.core :refer [*db*] :as db]
            [ring.util.response :refer [response]]
            [compojure.core :refer [GET POST PUT DELETE defroutes]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]))

(defn home-page []
  (layout/render "home.html"))

(defn handle-post-new-todo [req]
  (let [name (get-in req [:params :name])
        description (get-in req [:params :description])
        transaction (jdbc/with-db-transaction [t-conn *db*]
                                              (jdbc/db-set-rollback-only! t-conn)
                                              (db/add-todo! {:name        name
                                                             :description description}))]
    (if transaction
      {:status  200
       :headers {"Content-Type" "text/plain"}
       :body    (str transaction)}
      {:status  400
       :headers {"Content-Type" "text/plain"}
       :body    "no can do!"})))

(defn handle-update-todo [req]
  (let [id (get-in req [:params :id])
        checked (true? (get-in req [:params :checked]))
        transaction (jdbc/with-db-transaction [t-conn *db*]
                                              (jdbc/db-set-rollback-only! t-conn)
                                              (db/update-todo! {:id      id
                                                                :checked checked}))]
    (if transaction
      {:status  200
       :headers {"Content-Type" "text/plain"}
       :body    (str id)}
      {:status  400
       :headers {"Content-Type" "text/plain"}
       :body    "no can do!"})))

(defn handle-delete-todo [req]
  (let [id (get-in req [:params :id])
        transaction (jdbc/with-db-transaction [t-conn *db*]
                                              (jdbc/db-set-rollback-only! t-conn)
                                              (db/delete-todo! {:id id}))]
    (if transaction
      {:status  200
       :headers {"Content-Type" "text/plain"}
       :body    (str id)}
      {:status  400
       :headers {"Content-Type" "text/plain"}
       :body    "no can do!"})))

(defn handle-get-todos [_]
  (let [todos (jdbc/with-db-transaction [t-conn *db*]
                                        (jdbc/db-set-rollback-only! t-conn)
                                        (db/get-todos))]
    (when todos
      (response {:data todos}))))

(defroutes home-routes
           (GET "/" [] (home-page))
           (GET "/get-all-todos" [] handle-get-todos)
           (wrap-multipart-params
             (POST "/add-new-todo" {params :params} handle-post-new-todo))
           (PUT "/update-todo" {params :params} handle-update-todo)
           (DELETE "/delete-todo" {params :params} handle-delete-todo))