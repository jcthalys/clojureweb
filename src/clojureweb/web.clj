(ns clojureweb.web
  (require [clojureweb.domain :as domain]))


(defn index [request]
  {:status 200
   :body (domain/get-todos)})