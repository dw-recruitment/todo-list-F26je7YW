(defproject todo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.4.0"]]
  :main todo.core
  :profiles {:uberjar {:omit-source  true
                       :aot          :all
                       :uberjar-name "todo.jar"}
             :dev     {:main         todo.core/-dev
                       :repl-options {:init-ns user}}})
