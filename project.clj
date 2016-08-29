(defproject gov.usgs.eros/lcmap-config "1.0.0-SNAPSHOT"
  :parent-project {
    :coords [gov.usgs.eros/lcmap-system "1.0.0-SNAPSHOT"]
    :inherit [
      :license
      :managed-dependencies
      :plugins
      ;; XXX The following can be un-commented once this issue is resolved:
      ;;     * https://github.com/achin/lein-parent/issues/3
      ;; [:profiles [:uberjar :dev]]
      ]}
  :description "LCMAP Configuration Library"
  :url "https://github.com/USGS-EROS/lcmap-config"
  :dependencies [
    [camel-snake-kebab]
    [clj-http]
    [clojure-ini]
    [clojusc/twig]
    [com.stuartsierra/component]
    [environ]
    [leiningen-core]
    [org.apache.httpcomponents/httpclient]
    [org.clojure/clojure]
    [org.clojure/core.memoize]
    [org.clojure/tools.cli]
    [prismatic/schema]]
  :plugins [[lein-parent "0.3.0"]]
  :repl-options {:init-ns lcmap.config.dev}
  :main lcmap.config.app
  :codox {
    :project {
      :name "lcmap.config"
      :description "LCMAP Configuration Library"}
    :namespaces [#"^lcmap.config\."]
    :output-path "docs/master/current"
    :doc-paths ["docs/source"]
    :metadata {
      :doc/format :markdown
      :doc "Documentation forthcoming"}}
  :profiles {
    ;; XXX The :uberjar and :dev profiles can be removed once this issue is 
    ;;     resolved:
    ;;     * https://github.com/achin/lein-parent/issues/3
    :uberjar {:aot :all}
    :dev {
      :source-paths ["dev-resources/src"]
      :dependencies [[org.clojure/tools.namespace]
                     [slamhound]]
      :aliases {"slamhound" ["run" "-m" "slam.hound"]}}
    :test {
      :jvm-opts ["-DFOO=env-test-foo-val"
                 "-DBAR=env-test-bar-val"
                 "-DBAZ=env-test-baz-val"]
      :env {
        :log-level :info
        :clj-env :test}}})
