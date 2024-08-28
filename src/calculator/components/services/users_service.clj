;; src/calculator/components/services/users_service.clj
(ns calculator.components.services.users-service
  (:require [calculator.components.models.user :as user-model]
            [calculator.components.repositories.users-repository :as users-repo]
            [schema.core :as s]
            [com.stuartsierra.component :as component]))

(defrecord UsersService []
  component/Lifecycle
  (start [this]
    (println "Starting UsersService")
    this)
  (stop [this]
    (println "Stopping UsersService")
    this))

(defn new-users-service []
  (map->UsersService {}))

;; Function to create a user
(defn create-user [user-data]
  (s/validate user-model/User user-data)
  (users-repo/create-user user-data))

;; Function to read a user by id
(defn read-user [id]
  (users-repo/read-user id))

;; Function to update a user by id
(defn update-user [id user-data]
  (s/validate user-model/User user-data)
  (users-repo/update-user id user-data))

;; Function to delete a user by id
(defn delete-user [id]
  (users-repo/delete-user id))