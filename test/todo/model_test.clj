(ns todo.model-test
  (:require [clojure.test :refer :all]
            [todo.model :as model]))

(def test_db "jdbc:postgresql://localhost/todos_test")

(defn create-test-db-fixture [f]
  (model/create-table! test_db)
  (f))

(defn clean-db-fixture [f]
  (f)
  (model/delete-all test_db))

(use-fixtures :once create-test-db-fixture)
(use-fixtures :each clean-db-fixture)

(deftest model-test
  (testing

    "Basic C.R.U.D.
     CREATE and READ"

    (let [_ (model/add-todo! test_db "test" "desc")
          all (model/get-todos test_db)
          last-todo (last all)]
      (is (and (= (:name last-todo) "test")
               (= (:description last-todo) "desc")
               (false? (:checked last-todo))))))
  (testing
    "UPDATE"
    (let [_ (model/add-todo! test_db "test" "desc")
          all (model/get-todos test_db)
          new-todo (first all)
          update-operation (model/update-todo test_db (:id new-todo) true)]
      (is (true? update-operation))
      (is (true? (:checked (first (model/get-todos test_db)))))))

  (testing
    "DELETE"
    (let [b4-we-insert-count (count (model/get-todos test_db))
          new-todo-id (model/add-todo! test_db "test" "to be deleted")
          delete-operation (model/delete-item test_db new-todo-id)]
      (is (true? delete-operation))
      (is (= (count (model/get-todos test_db)) b4-we-insert-count)))))