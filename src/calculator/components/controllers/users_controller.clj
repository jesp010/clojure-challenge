;; src/calculator/components/controllers/users_controller.clj
(ns calculator.components.controllers.users-controller
  (:require [calculator.components.services.users-service :as users-service]
            [ring.util.response :as response]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.hashers :as hashers]
            [com.stuartsierra.component :as component]
            [compojure.core :refer [defroutes POST GET PUT DELETE]]))

(def auth-backend (session-backend))

(defn wrap-secure [handler]
  (-> handler
      (wrap-authentication auth-backend)
      (wrap-authorization auth-backend)))

(defn signup [request]
  (let [user-data (:body-params request)
        hashed-password (hashers/encrypt (:password user-data))
        user-data (assoc user-data :password hashed-password)]
    (users-service/create-user user-data)
    (response/ok {:message "User created successfully"})))

(defn signin [request]
  (let [credentials (:body-params request)
        user (users-service/read-user (:username credentials))]
    (if (and user (hashers/check (:password credentials) (:password user)))
      (response/ok {:message "Signin successful"})
      (response/unauthorized {:message "Invalid credentials"}))))

(defn get-user [request]
  (let [id (get-in request [:params :id])]
    (response/ok (users-service/read-user id))))

(defn update-user [request]
  (let [id (get-in request [:params :id])
        user-data (:body-params request)]
    (users-service/update-user id user-data)
    (response/ok {:message "User updated successfully"})))

(defn delete-user [request]
  (let [id (get-in request [:params :id])]
    (users-service/delete-user id)
    (response/ok {:message "User deleted successfully"})))

(defroutes user-routes
  (POST "/signup" [] signup)
  (POST "/signin" [] signin)
  (GET "/users/:id" [] (wrap-secure get-user))
  (PUT "/users/:id" [] (wrap-secure update-user))
  (DELETE "/users/:id" [] (wrap-secure delete-user)))

(defrecord UsersController []
  component/Lifecycle
  (start [this]
    (println "Starting UsersController")
    this)
  (stop [this]
    (println "Stopping UsersController")
    this))

(defn new-users-controller []
  (map->UsersController {}))