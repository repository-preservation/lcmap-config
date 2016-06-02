(ns lcmap.config.app
  ""
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [twig.core :as logger]
            [lcmap.config.components :as components]
            [lcmap.config.util :as util]))

(defn -main
  "This is the entry point. Note, however, that the system components are
  defined in lcmap.see.components. In particular, lcmap.see.components.system
  brings together all the defined (and active) components; that is the module
  which is used to bring the system up when (component/start ...) is called.

  'lein run' will use this as well as 'java -jar'."
  [& args]
  ;; Set the initial log-level before the components set the log-levels for
  ;; the configured namespaces
  (logger/set-level! ['lcmap] :info)
  (let [system (components/init)
        local-ip  (.getHostAddress (java.net.InetAddress/getLocalHost))]
    (log/info "LCMAP SEE service's local IP address:" local-ip)
    (component/start system)
    (util/add-shutdown-handler #(component/stop system))
    ;; Since there's no daemon, keep things running with a Thread join
    (.join (Thread/currentThread))))
