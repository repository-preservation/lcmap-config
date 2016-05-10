(ns lcmap.config.helpers
  ""
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure-ini.core :as ini]
            [camel-snake-kebab.core :refer [->kebab-case-keyword]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [schema.core :as schema]
            [schema.coerce :as coerce]))

;;; Rebindable vars referencing LCMAP configuration files.

(def ^:dynamic *lcmap-config-dir*
  (io/file (System/getProperty "user.home") ".usgs"))

(def ^:dynamic *lcmap-config-ini*
  (io/file *lcmap-config-dir* "lcmap.ini"))

(def ^:dynamic *lcmap-config-edn*
  (io/file *lcmap-config-dir* "lcmap.edn"))


;;; Configuration source to cfg-map functions

(defn ini->cfg
  "Get .ini file as map."
  [path]
  (transform-keys ->kebab-case-keyword
                  (ini/read-ini path)))

(defn edn->cfg
  "Get .edn file as map."
  [path]
  (transform-keys ->kebab-case-keyword
                  (edn/read-string (slurp path))))

(defn env->cfg
  "Get environment, including java properties, as map."
  ([]
   (let [props (System/getProperties)
         env (into {} (System/getenv))
         env+props (merge env props)]
     (transform-keys ->kebab-case-keyword env+props)))
  ([& more]
   (let [ks (map ->kebab-case-keyword (flatten (vector more)))]
     (select-keys (env->cfg) ks))))

(defn cli->cfg
  "Get command line arguments as map."
  [args opt-spec]
  (transform-keys ->kebab-case-keyword
                  (-> (cli/parse-opts args opt-spec) :options)))

;;; Coercing functions

(defn string->strings [schema]
  (let [comma #"[, ]+"]
    (when (= [schema/Str] schema)
      (coerce/safe
       (fn [value]
         (if (string? value)
           (map clojure.string/trim
                (clojure.string/split value comma))
           value))))))

(defn string->numeric [schema]
  (let [nf (java.text.NumberFormat/getInstance)]
    (when (= schema/Num schema)
      (coerce/safe
       (fn [value]
         (if (string? value)
           (.parse nf value)
           value))))))

(def config-coercers (coerce/first-matcher [string->numeric
                                            string->strings]))


(def Config
  "A schema for config maps"
  {schema/Keyword schema/Str})

(defn check-cfg
  "Validate cfg against schema."
  ([cfg]
   (schema/validate Config cfg))
  ([schema cfg]
   (schema/validate schema cfg)))

(defn coerce-cfg
  "Transform values of config using coercers."
  [schema cfg]
  (let [cfn (coerce/coercer schema config-coercers)]
    (cfn cfg)))

(defn build-cfg
  "Helper function that invokes each cfg-map building function."
  [{:keys [ini edn args spec env] :as opts}]
  (merge (if env (env->cfg env) {})
         (if ini (ini->cfg ini) {})
         (if edn (edn->cfg edn) {})
         (if (and args spec (cli->cfg args spec)) {})))

(defn init-cfg
  "Produce a validated configuration map. When configuration is
  built in the context of another system, you may want to compose
  a schema for only the components you will use."
  [{schema :schema :as args}]
  (->> (build-cfg args)
       (coerce-cfg schema)
       (check-cfg schema)))
