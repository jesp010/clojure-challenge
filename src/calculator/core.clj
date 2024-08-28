;; src/calculator/core.clj
(ns calculator.core
  (:require [calculator.config :as config]
            [calculator.components.controllers.users-controller :refer [new-users-controller]]
            [calculator.components.controllers.calculations-controller :refer [new-calculations-controller]]
            [calculator.components.services.users-service :refer [new-users-service]]
            [calculator.components.services.calculations-service :refer [new-calculations-service]]
            [calculator.components.repositories.users-repository :refer [new-user-repository]]
            [calculator.components.repositories.calculations-repository :refer [new-calculations-repository]]
            [calculator.components.repositories.db :refer [new-db]]
            [com.stuartsierra.component :as component]))

(defn init-system []
  (component/system-map
    :db (new-db)
    :users-repository (component/using (new-user-repository) [:db])
    :calculations-repository (component/using (new-calculations-repository) [:db])
    :users-service (component/using (new-users-service) [:users-repository])
    :calculations-service (component/using (new-calculations-service) [:calculations-repository])
    :users-controller (component/using (new-users-controller) [:users-service])
    :calculations-controller (component/using (new-calculations-controller) [:calculations-service])))

(defn -main []
  (let [config (config/read-config)
        system (init-system)]
    (println "Starting Real Clojure Calculator with config" config)
    (component/start system)))