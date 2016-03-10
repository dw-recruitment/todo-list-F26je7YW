(ns todo.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [todo.core-test]))

(doo-tests 'todo.core-test)

