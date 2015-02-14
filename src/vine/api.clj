(ns vine.api
  (:require [clojure.string :as string]
            [vine.util.http :as http]))

(defmacro ^:private def-vine-endpoint
  [method resource-path]
  (let [http-func (symbol (str "http/v" method))
        fn-req-args (->> resource-path
                         (re-seq #"<(.*?)>")
                         (map last)
                         (map symbol)
                         vec)
        fn-args (conj fn-req-args '& 'args)
        fn-name (-> resource-path
                    (string/replace #"/" "-")
                    (string/replace #"<|>" "")
                    (string/replace #"-$" "")
                    symbol)]
    `(defn ~fn-name
       ~(str (.toUpperCase (name method)) " request to " (str http/endpoint resource-path))
       ~fn-args
       (let [built-path# (loop [path# ~resource-path idx# 0]
                           (if (> idx# (-> ~fn-req-args count dec))
                             path#
                             (recur (string/replace path#
                                                    (re-pattern (str "<" (name (nth '~fn-req-args idx#)) ">"))
                                                    (nth ~fn-req-args idx#))
                                    (inc idx#))))]
         (apply ~http-func built-path# ~(last fn-args))))))

;; users
(def-vine-endpoint get "users/profiles/<id>")

;; timelines
(def-vine-endpoint get "timelines/posts/<id>")
(def-vine-endpoint get "timelines/users/<id>")
(def-vine-endpoint get "timelines/users/<id>/likes")
(def-vine-endpoint get "timelines/tags/<name>")
(def-vine-endpoint get "timelines/popular")
(def-vine-endpoint get "timelines/trending")
(def-vine-endpoint get "timelines/promoted")
(def-vine-endpoint get "timelines/channels/<id>/popular")
(def-vine-endpoint get "timelines/channels/<id>/recent")
(def-vine-endpoint get "timelines/venues/<id>")

;; tags
(def-vine-endpoint get "tags/trending")
(def-vine-endpoint get "channels/featured")

;; search
(def-vine-endpoint get "users/search/<query>")
(def-vine-endpoint get "tags/search/<name>")


