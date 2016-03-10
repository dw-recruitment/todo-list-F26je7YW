(ns todo.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST PUT DELETE]]
            [todo.ajax :refer [load-interceptors!]])
  (:import goog.History))

(enable-console-print!)

(def app-state (r/atom nil))

(defn nav-link [uri title page collapsed?]
  [:ul.nav.navbar-nav>a.navbar-brand
   {:class    (when (= page (session/get :page)) "active")
    :href     uri
    :on-click #(reset! collapsed? true)}
   title])

(defn refresh-app-state! []
  (GET "/get-all-todos" {:handler         #(reset! app-state (:data %))
                         :response-format :json
                         :keywords?       true}))

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "Todos"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Home" :home collapsed?]
         [nav-link "#/about" "About" :about collapsed?]]]])))

(defn new-todo-form-component []
  [:div.form-group
   [:form {:class  "form-inline add-form"
           :action "/add-new-todo" :method "POST"}
    [:p [:input {:type "text" :name "name" :placeholder "title"}]
     [:input {:type "text" :name "description" :placeholder "description"}]
     [:button.btn.btn-primary.btn-sm
      {:type    "button"
       :onClick #(POST "/add-new-todo"
                       {:body    (js/FormData. (.querySelector js/document "form"))
                        :handler (fn []
                                   (refresh-app-state!)
                                   (.reset (.querySelector js/document "form"))
                                   )})} "Add"]]]])

(defn todos-component []
  [:ul
   (for [t @app-state]
     ^{:key (:id t)}
     [:li {:id    (:id t)
           :class (if (:checked t)
                    "todo strike"
                    "todo")}
      [:div.checkbox
       [:input {:type     "checkbox"
                :class    "check"
                :data-id  (:id t)
                :checked  (:checked t)
                :onChange (fn [e]
                            (PUT "/update-todo" {:handler #(refresh-app-state!)
                                                 :params  {:id      (:id t)
                                                           :checked (.-checked (.-target e))}}))}]]
      [:span
       [:b (:name t)] " : "
       [:em (:description t)] " "]
      [:button.btn.btn-sm.btn-danger.delete
       {:type    "button"
        :data-id (:id t)
        :onClick #(DELETE "/delete-todo"
                          {:params  {:id (:id t)}
                           :handler (fn [] (refresh-app-state!))})} "x"]])])

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "This project is about getting stuff done!"]]])

(defn home-page []
  (let []
    (r/create-class
      {:component-did-mount #(refresh-app-state!)
       :reagent-render      (fn []
                              [:div.container
                               [:div.jumbotron
                                [:h1 "What needs to be done?"]
                                [:p "Use the form to add a new todo item:"]
                                [new-todo-form-component]
                                [todos-component]]])})))

(def pages
  {:home  #'home-page
   :about #'about-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
                    (session/put! :page :home))

(secretary/defroute "/about" []
                    (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
