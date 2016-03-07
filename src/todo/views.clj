(ns todo.views
  (:require [hiccup.core :as h]
            [hiccup.page :as page]))

(defn add-new-todo-form []
  [:form {:action "/add-new-todo" :method "POST"}
   [:p "add new:"]
   [:p [:label [:b "title: "]
        [:input {:type "text" :name "name"}]]]
   [:p [:label [:b "description"]
        [:input {:type "text" :name "description"}]]
    [:input {:type "submit" :value "submit new todo"}]]])

(defn index [content]
  (let [todos content]
    (page/html5
      [:head
       [:title "Todos App"]]
      [:body
       [:h1 {:id "mount"} "TODOS:"]
       (add-new-todo-form)
       [:ul
        (for [t todos]
          [:div.todo
           [:label
            [:input {:type "checkbox"}
             [:b (:name t)] " - "]]
           [:em (:description t)]])]])))