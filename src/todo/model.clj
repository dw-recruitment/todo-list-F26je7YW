(ns todo.model
  (:require [clojure.java.jdbc :as db]))

(defn create-table! [db]
  (db/execute!
    db
    ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (db/execute!
    db
    ["CREATE TABLE IF NOT EXISTS todos
      (id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
      name TEXT NOT NULL,
      description TEXT NOT NULL,
      checked BOOLEAN NOT NULL DEFAULT FALSE,
      date_created TIMESTAMPTZ NOT NULL DEFAULT now())"]))

(defn add-todo! [db name description]
  (:id (first
         (db/query
           db
           ["INSERT INTO todos (name, description)
             VALUES (?, ?)
             RETURNING id"
            name
            description]))))

(defn get-todos [db]
  (db/query
    db
    ["SELECT id, name, description, checked, date_created
    FROM todos
    ORDER BY date_created"]))

(defn update-todo [db id checked]
  (= [1]
     (db/execute!
       db
       ["UPDATE todos
       SET checked = ?
       WHERE id = ?"
        checked
        id])))