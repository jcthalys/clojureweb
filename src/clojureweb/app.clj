(ns clojureweb.app
  (:require [clojureweb.domain :as domain]
            [clojureweb.web :as web]
            [compojure.core :refer [defroutes GET POST]]
            [hiccup.element :refer (link-to)]
            [hiccup.form :as f]
            [hiccup.page :refer [html5]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [redirect]]))

(def id (atom 0))
(def items (atom []))


(defn next-id []
  (swap! id inc))

(defn add-item [item]
  (swap! items conj (assoc item :id (next-id))))

(defn get-todos []
  @items)

(defn get-item [id]
  (first (filter #(= id (:id %)) @items)))

(defn index [items]
  (html5
    [:head
     [:title "My first clojure web application"]]
    [:body
     [:h1 "My first clojure web application"]
     [:ul
      (map #(vector :li (link-to (str "/" (:id %)) (:summary %))) items)]
     (f/form-to [:post "/"]
                (f/text-field "summary")
                (f/submit-button "Save"))
     ]))

(defn save [request]
  (add-item {:summary (get-in request [:params :summary])})
  (redirect "http://localhost:3000"))

(defn show [id]
  (when-let [item (get-item id)]
    (html5
      [:head
       [:title (str "Web App Clojure - " (:summary item))]]
      [:body
       [:h1 "Web App TODO"]
       [:h2 (:summary item)]
       (link-to "/" "Back")])))

(defroutes routes
           (GET "/" [] (index @items))
           (POST "/" [] save)
           (GET "/:id" [id] (show (Integer/parseInt id))))

(def app
  (-> routes
      wrap-keyword-params
      wrap-params))


(defn -main [& args]
  (add-item {:summary "Dish wash"})
  (add-item {:summary "Do dinner"})
  (add-item {:summary "More todo"})

  (run-jetty app {:port 3000}))
