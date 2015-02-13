(ns vine.util.http
  (:refer-clojure :exclude [get])
  (:require [org.httpkit.client :as http]))

(def endpoint "https://vine.co/api/")

(defmacro ^:private def-vine-request [method]
  `(defn ~(symbol (str "v" method)) [& args#]
     (let [http-func# ~(symbol (str "http/" method))
           params# (cons (str endpoint (first args#)) (rest args#))]
       (apply http-func# params#))))

(def-vine-request get)
; (def-vine-request post)
; (def-vine-request put)
