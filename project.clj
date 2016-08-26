(defproject gov.usgs.eros/lcmap-config "1.0.0-SNAPSHOT"
  :parent-project [
    :path "../lcmap-system/project.clj"
    :coords [gov.usgs.eros/lcmap-system "1.0.0-SNAPSHOT"]
    :inherit [
      [:codox :output-path]
      [:codox :doc-paths]
      [:codox :metadata]
      :license
      :managed-dependencies
      :plugins
      [:profiles :uberjar]
      [:profiles :dev :source-paths]]]
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
  :plugins [[lein-parent "0.2.1"]]
  :repl-options {:init-ns lcmap.config.dev}
  :main lcmap.config.app
  :codox {
    :project {
      :name "lcmap.config"
      :description "LCMAP Configuration Library"}
    :namespaces [#"^lcmap.config\."]}
  :profiles {
    :dev {
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
