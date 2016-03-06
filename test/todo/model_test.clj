(ns todo.model-test
  (:require [clojure.test :refer :all]
            [todo.model :as model]))

(def test_db "jdbc:postgresql://localhost/todos_test")

(let []
  (model/create-table! test_db))

(deftest model-test
  (testing
    "basic C.R.U.D. CREATE and READ"
    (let [_ (model/add-todo! test_db "test" "desc")
          all (model/get-todos test_db)
          last-todo (last all)]
      (is (and (= (:name last-todo) "test")
               (= (:description last-todo) "desc")
               (false? (:checked last-todo)))))))