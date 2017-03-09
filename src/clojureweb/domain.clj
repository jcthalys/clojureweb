(ns clojureweb.domain)

(def todos (atom []))

(defn add-todo! [item]
  (swap! todos conj item))

(defn get-todos []
  @todos)
