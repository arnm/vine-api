(ns vine.api
  (:require [clojure.string :as string]
            [vine.util.http :as http]))

(defmacro ^:private def-vine-endpoint
  "Constructs a function which requests the Vine resource path"
  [method resource-path]
  (let [http-func (symbol (str "http/v" method))
        fn-req-args (->> resource-path
                         (re-seq #"<(.*?)>")
                         (map last)
                         (map symbol)
                         vec)
        fn-args (conj fn-req-args '& 'args)
        fn-arg1 (conj fn-req-args '[opts callback])
        fn-arg2 (conj fn-req-args '[callback])
        fn-name (-> resource-path
                    (string/replace #"/" "-")
                    (string/replace #"<|>" "")
                    (string/replace #"-$" "")
                    symbol)]
    `(defn ~fn-name
       ~(str (.toUpperCase (name method)) " request to " (str http/endpoint resource-path))
       {:arglists (list '~fn-arg1 '~fn-arg2)}
       ~fn-args
       (let [built-path# (loop [path# ~resource-path idx# 0]
                           (if (> idx# (-> ~fn-req-args count dec))
                             path#
                             (recur (string/replace path#
                                                    (re-pattern (str "<" (name (nth '~fn-req-args idx#)) ">"))
                                                    (nth ~fn-req-args idx#))
                                    (inc idx#))))]
         (apply ~http-func built-path# ~'args)))))

;; channels
(def-vine-endpoint get "channels/featured")

;; posts
(def-vine-endpoint get "posts/search/<query>")

;; tags
(def-vine-endpoint get "tags/trending")
(def-vine-endpoint get "tags/search/<query>")

;; timelines
(def-vine-endpoint get "timelines/posts/<id>")
(def-vine-endpoint get "timelines/users/<id>")
(def-vine-endpoint get "timelines/users/<id>/likes")
(def-vine-endpoint get "timelines/tags/<name>")
(def-vine-endpoint get "timelines/popular")
(def-vine-endpoint get "timelines/promoted")
(def-vine-endpoint get "timelines/channels/<id>/popular")
(def-vine-endpoint get "timelines/channels/<id>/recent")
(def-vine-endpoint get "timelines/venues/<id>")

;; users
(def-vine-endpoint get "users/profiles/<id>")
(def-vine-endpoint get "users/search/<query>")

;; venues
(def-vine-endpoint get "venues/search/<query>")

