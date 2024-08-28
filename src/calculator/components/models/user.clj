(ns calculator.components.models.user
  (:require [schema.core :as s]))

(s/defschema User
  {:id s/Int
   :username s/Str
   :email s/Str
   :password s/Str})