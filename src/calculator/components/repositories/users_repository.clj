(ns calculator.components.repositories.users-repository
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [calculator.components.repositories.db :refer [datasource]]
            [calculator.components.models.user :as user]))

(defrecord UserRepository []
  component/Lifecycle
  (start [this]
    (println "Starting UserRepository")
    this)
  (stop [this]
    (println "Stopping UserRepository")
    this))

(defn new-user-repository []
  (map->UserRepository {}))

;; Function to create a user record
(defn create-user [data]
  (sql/insert! datasource :users data))

;; Function to read a user record by id
(defn read-user [id]
  (sql/get-by-id datasource :users id))

;; Function to update a user record by id
(defn update-user [id data]
  (sql/update! datasource :users data {:id id}))

;; Function to delete a user record by id
(defn delete-user [id]
  (sql/delete! datasource :users {:id id}))