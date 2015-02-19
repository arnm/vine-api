(defproject com.alexeinunez/vine-api "0.2.0"
  :description "A Clojure library designed to wrap the Unofficial Private Vine API"
  :url "http://github.com/arnm/vine-api"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.match  "0.3.0-alpha4"]
                 [http-kit "2.1.18"]]
  :profiles {:d {:dependencies [[org.clojure/data.json "0.2.5"]
                                [environ "1.0.0"]]
                 :plugins [[lein-environ "1.0.0"]]}})
