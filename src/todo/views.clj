(ns todo.views
  (:require [hiccup.core :as h]
            [hiccup.page :as page]))

(defn add-new-todo-form []
  [:div.form-group
   [:form {:class  "form"
           :action "/add-new-todo" :method "POST"}
    [:p "add new:"]
    [:p [:input {:type "text" :name "name" :placeholder "title"}]
     [:input {:type "text" :name "description" :placeholder "description"}]
     [:input {:class "btn-sm" :type "submit" :value "submit new todo"}]]]])

(defn index [content]
  (let [todos content]
    (page/html5
      [:head
       [:title "Todos App"]
       [:link {:href "css/vendor/bootstrap.min.css"
               :rel  :stylesheet}]]
      [:link {:href "css/main.css"
              :rel  :stylesheet}]
      [:body
       [:div.container
        [:div.row
         [:div.col-lg-12
          [:h1 {:id "mount"} "TODOS:"]
          (add-new-todo-form)]]
        [:div.row
         [:div.col-lg-6
          [:ul
           (for [t todos]
             [:div {:id    (:id t)
                    :class (if (:checked t)
                             "todo strike"
                             "todo")}
              [:div.checkbox
               [:input {:type    "checkbox"
                        :class   "check"
                        :data-id (:id t)
                        :checked (:checked t)} " "]]
              [:span
               [:b (:name t)] " : "
               [:em (:description t)]]
              " "
              [:button.btn.btn-sm.btn-danger.delete
               {:type "button"
                :data-id (:id t)} "x"]])]]]]
       [:script {:src "js/vendor/jquery.min.js"}]
       [:script {:src "js/main.js"}]])))