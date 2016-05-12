(ns ^{:doc
  "Utility functions for configuring components that connect to
  a Cassandra cluster using Cassaforte."}
  lcmap.config.cassaforte)

(defn connect-opts
  "Build connect options from db-conf. Only sets credentials and basic parameters.
  Policies should be set using Cassaforte's rebinding functions for now."
  [db-conf]
  (let [hosts  (:db-hosts db-conf)
        basics (select-keys db-conf [:port :connections-per-host :max-connections-per-host])
        creds  {:credentials {:username (:db-user db-conf) :password (:db-pass db-conf)}}]
    ;; Credentials are ignored if the cluster has the
    ;; default AllowAll authentication setting.
    [hosts (merge basics creds)]))
