(ns ^{:doc
      "LCMAP configuration config"}
  lcmap.config
  (:require [lcmap.config.helpers :as helpers]
            [schema.core :as schema]))

(def opt-spec [])

(def cfg-schema
  {schema/Keyword schema/Any})

(def defaults
  {:ini helpers/*lcmap-config-ini*
   :args *command-line-args*
   :spec opt-spec
   :schema cfg-schema})
