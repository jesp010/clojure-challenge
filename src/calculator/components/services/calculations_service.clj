;; src/calculator/components/services/calculations_service.clj
(ns calculator.components.services.calculations-service
  (:require [calculator.components.models.calculation :as calculation-model]
            [calculator.components.repositories.calculations-repository :as calc-repo]
            [schema.core :as s]
            [com.stuartsierra.component :as component]))

(defrecord CalculationsService []
  component/Lifecycle
  (start [this]
    (println "Starting CalculationsService")
    this)
  (stop [this]
    (println "Stopping CalculationsService")
    this))

(defn new-calculations-service []
  (map->CalculationsService {}))

;; Function to create a calculation
(defn create-calculation [calculation-data]
  (s/validate calculation-model/Calculation calculation-data)
  (calc-repo/create-calculation calculation-data))

;; Function to read a calculation by id
(defn read-calculation [id]
  (calc-repo/read-calculation id))

;; Function to get paginated calculations
(defn get-paginated-calculations [page limit]
  (calc-repo/get-paginated-calculations page limit))

;; Function to update a calculation by id
(defn update-calculation [id calculation-data]
  (s/validate calculation-model/Calculation calculation-data)
  (calc-repo/update-calculation id calculation-data))

;; Function to delete a calculation by id
(defn delete-calculation [id]
  (calc-repo/delete-calculation id))

;; Arithmetic functions
(defn add [a b]
  (let [result (+ a b)
        calculation-data {:operation "add" :operand1 a :operand2 b :result result}]
    (create-calculation calculation-data)
    result))

(defn subtract [a b]
  (let [result (- a b)
        calculation-data {:operation "subtract" :operand1 a :operand2 b :result result}]
    (create-calculation calculation-data)
    result))

(defn multiply [a b]
  (let [result (* a b)
        calculation-data {:operation "multiply" :operand1 a :operand2 b :result result}]
    (create-calculation calculation-data)
    result))

(defn divide [a b]
  (if (zero? b)
    (throw (IllegalArgumentException. "Division by zero"))
    (let [result (/ a b)
          calculation-data {:operation "divide" :operand1 a :operand2 b :result result}]
      (create-calculation calculation-data)
      result)))