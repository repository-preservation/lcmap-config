(ns lcmap.config.components.config
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [lcmap.config.helpers :as config-help]))

(defrecord Configuration [cfg-opts]
  component/Lifecycle

  (start [component]
    (log/info "Starting configuration component ...")
    (let [cfg-map (config-help/init-cfg cfg-opts)]
      (log/debug "Configuration data:" cfg-map)
      (merge component cfg-map)))

  (stop [component]
    (log/info "Stopping configuration component ...")))

(defn new-configuration [cfg-opts]
  (log/debug "Building configuration component ...")
  (->Configuration cfg-opts))
