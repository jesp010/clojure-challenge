;; src/calculator/components/repositories/calculations_repository.clj
(ns calculator.components.repositories.calculations-repository
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [calculator.components.repositories.db :refer [datasource]]
            [com.stuartsierra.component :as component]))

(defrecord CalculationsRepository []
  component/Lifecycle
  (start [this]
    (println "Starting CalculationsRepository")
    this)
  (stop [this]
    (println "Stopping CalculationsRepository")
    this))

(defn new-calculations-repository []
  (map->CalculationsRepository {}))

;; Function to create a calculation record with transaction
(defn create-calculation [data]
  (jdbc/with-transaction [tx datasource]
    (sql/insert! tx :calculations data)))

;; Function to read a calculation record by id
(defn read-calculation [id]
  (sql/get-by-id datasource :calculations id))

;; Function to delete a calculation record by id
(defn delete-calculation [id]
  (sql/delete! datasource :calculations {:id id}))

;; Function to get paginated calculations
(defn get-paginated-calculations [page limit]
  (let [offset (* (dec page) limit)
        query (-> (h/select :*)
                  (h/from :calculations)
                  (h/offset offset)
                  (h/limit limit)
                  hsql/format)]
    (jdbc/execute! datasource query)))
