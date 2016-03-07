(ns todo.views
  (:require [hiccup.core :as h]
            [hiccup.page :as page]))

(defn index [content]
  (let [todos content]
    (page/html5
      [:head
       [:title "Todos App"]]
      [:body
       [:h1 {:id "mount"} "TODOS:"]
       [:ul
        (for [t todos]
          [:div.todo
           [:label
            [:input {:type "checkbox"}
             [:b (:name t)] " - "]]
           [:em (:description t)]])]])))