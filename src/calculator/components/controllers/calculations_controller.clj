;; src/calculator/components/controllers/calculations_controller.clj
(ns calculator.components.controllers.calculations-controller
  (:require [calculator.components.services.calculations-service :as calc-service]
            [ring.util.response :as response]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [buddy.auth.backends.session :refer [session-backend]]
            [com.stuartsierra.component :as component]
            [compojure.core :refer [defroutes POST GET PUT DELETE]]))

(def auth-backend (session-backend))

(defn wrap-secure [handler]
  (-> handler
      (wrap-authentication auth-backend)
      (wrap-authorization auth-backend)))

(defn create-calculation [request]
  (let [calculation-data (:body-params request)]
    (calc-service/create-calculation calculation-data)
    (response/ok {:message "Calculation created successfully"})))

(defn read-calculation [request]
  (let [id (get-in request [:params :id])]
    (response/ok (calc-service/read-calculation id))))

(defn update-calculation [request]
  (let [id (get-in request [:params :id])
        calculation-data (:body-params request)]
    (calc-service/update-calculation id calculation-data)
    (response/ok {:message "Calculation updated successfully"})))

(defn delete-calculation [request]
  (let [id (get-in request [:params :id])]
    (calc-service/delete-calculation id)
    (response/ok {:message "Calculation deleted successfully"})))

(defn get-paginated-calculations [request]
  (let [page (Integer. (get-in request [:params :page]))
        limit (Integer. (get-in request [:params :limit]))]
    (response/ok (calc-service/get-paginated-calculations page limit))))

(defn add [request]
  (let [a (Integer. (get-in request [:params :a]))
        b (Integer. (get-in request [:params :b]))]
    (response/ok {:result (calc-service/add a b)})))

(defn subtract [request]
  (let [a (Integer. (get-in request [:params :a]))
        b (Integer. (get-in request [:params :b]))]
    (response/ok {:result (calc-service/subtract a b)})))

(defn multiply [request]
  (let [a (Integer. (get-in request [:params :a]))
        b (Integer. (get-in request [:params :b]))]
    (response/ok {:result (calc-service/multiply a b)})))

(defn divide [request]
  (let [a (Integer. (get-in request [:params :a]))
        b (Integer. (get-in request [:params :b]))]
    (response/ok {:result (calc-service/divide a b)})))

(defroutes calculation-routes
  (POST "/calculations" [] (wrap-secure create-calculation))
  (GET "/calculations/:id" [] (wrap-secure read-calculation))
  (PUT "/calculations/:id" [] (wrap-secure update-calculation))
  (DELETE "/calculations/:id" [] (wrap-secure delete-calculation))
  (GET "/calculations" [] (wrap-secure get-paginated-calculations))
  (POST "/calculations/add" [] (wrap-secure add))
  (POST "/calculations/subtract" [] (wrap-secure subtract))
  (POST "/calculations/multiply" [] (wrap-secure multiply))
  (POST "/calculations/divide" [] (wrap-secure divide)))

(defrecord CalculationsController []
  component/Lifecycle
  (start [this]
    (println "Starting CalculationsController")
    this)
  (stop [this]
    (println "Stopping CalculationsController")
    this))

(defn new-calculations-controller []
  (map->CalculationsController {}))