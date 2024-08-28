;; src/calculator/components/repositories/db.clj
(ns calculator.components.repositories.db
  (:require
    [com.stuartsierra.component :as component]
    [environ.core :refer [env]]
    [next.jdbc :as jdbc]
    [next.jdbc.connection :as connection]

    [next.jdbc.sql :as sql]))

;; Database connection specification using environment variables
(def db-spec
  {:dbtype   "postgresql"
   :dbname   (env :db-name)
   :host     (env :db-host)
   :port     (Integer. (env :db-port))
   :user     (env :db-user)
   :password (env :db-password)})

(defrecord Database []
  component/Lifecycle
  (start [this]
    (println "Starting Database")
    (assoc this :datasource (connection/->pool HikariCP db-spec)))
  (stop [this]
    (println "Stopping Database")
    (when-let [ds (:datasource this)]
      (.close ds))
    (assoc this :datasource nil)))


(defonce instance (atom nil))

(defn get-database []
  (or @instance
      (reset! instance (map->Database {}))))

;; Create a connection pool using HikariCP
(def datasource
  (connection/->pool HikariCP db-spec))

;; Function to create the users table if it doesn't exist
(defn create-users-table []
  (sql/execute! datasource
                ["CREATE TABLE IF NOT EXISTS users (
        id SERIAL PRIMARY KEY,
        username VARCHAR(50) NOT NULL,
        email VARCHAR(100) NOT NULL,
        password VARCHAR(100) NOT NULL
      )"]))

;; Function to create the calculations table if it doesn't exist
(defn create-calculations-table []
  (sql/execute! datasource
                ["CREATE TABLE IF NOT EXISTS calculations (
        id SERIAL PRIMARY KEY,
        user_id INTEGER NOT NULL,
        operation VARCHAR(10) NOT NULL,
        operand1 DOUBLE PRECISION NOT NULL,
        operand2 DOUBLE PRECISION NOT NULL,
        result DOUBLE PRECISION NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id)
      )"]))