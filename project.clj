(defproject detris "0.1.0-SNAPSHOT"
  :description "DRW programming assignment (detris = drw tetris)"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :main ^:skip-aot detris.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
