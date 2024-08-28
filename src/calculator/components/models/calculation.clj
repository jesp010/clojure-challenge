(ns calculator.components.models.calculation
  (:require [schema.core :as s]))

(s/defschema Calculation
  {:id s/Int
   :user_id s/Int
   :operation s/Str
   :operand1 s/Num
   :operand2 s/Num
   :result s/Num})

(s/defschema PaginatedCalculations
  {:page s/Int
   :limit s/Int
   :total s/Int
   :calculations [Calculation]})