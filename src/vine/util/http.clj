(ns vine.util.http
  (:refer-clojure :exclude [get])
  (:require [org.httpkit.client :refer :all]))

(def endpoint "https://vine.co/api/")

(defmacro ^:private def-vine-request [method]
  "Constructs a function which prepends the Vine api endpoint to a path"
  `(defn ~(symbol (str "v" method))
     ~'{:arglists '([path & [opts callback]] [path & [callback]])}
     ~'[path & args]
     (let [params# (cons (str endpoint ~'path) ~'args)]
       (apply ~method params#))))

(def-vine-request get)
(def-vine-request post)
(def-vine-request delete)
