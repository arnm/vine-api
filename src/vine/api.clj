(ns vine.api
  (:require [clojure.string :as string]
            [clojure.core.match :refer [match]]
            [vine.util.http :as http]))

(defmacro ^:private def-vine-endpoint
  "Constructs a function which requests the Vine resource path"
  [requires-auth resource-path]
  (let [fn-req-args (->> resource-path
                         (re-seq #"<(.*?)>")
                         (map last)
                         (map symbol)
                         (#(cons 'method %))
                         vec)
        fn-args (conj fn-req-args '& 'args)
        fn-arg1 (conj fn-req-args '& '[opts callback])
        fn-arg2 (conj fn-req-args '& '[callback])
        fn-name (-> resource-path
                    (string/replace #"/" "-")
                    (string/replace #"<|>" "")
                    (string/replace #"-$" "")
                    symbol)]
    `(defn ~fn-name
       ~(str (when requires-auth
               "Requires authentication. ")
             "Requests "
             (str http/endpoint resource-path) ".")
       {:arglists (list '~fn-arg1 '~fn-arg2)}
       ~fn-args
       (let [built-path# (loop [path# ~resource-path idx# 0]
                           (if (> idx# (-> ~fn-req-args count dec))
                             path#
                             (recur (string/replace-first path#
                                                          (re-pattern (str "<" (name (nth '~fn-req-args idx#)) ">"))
                                                          (nth ~fn-req-args idx#))
                                    (inc idx#))))]
         ;; TODO: generate function symbol and evaluate it during function call
         (match [~'method]
                [:get] (apply ~'http/vget built-path# ~'args)
                [:post] (apply ~'http/vpost built-path# ~'args)
                [:delete] (apply ~'http/vdelete built-path# ~'args))))))

;; channels
(def-vine-endpoint false "channels/featured")

;; posts
(def-vine-endpoint true "posts/")
(def-vine-endpoint true "posts/<id>")
(def-vine-endpoint true "posts/<id>/likes")
(def-vine-endpoint true "posts/<id>/comments")
(def-vine-endpoint true "posts/<id>/comments/<cid>")
(def-vine-endpoint true "posts/<id>/repost")
(def-vine-endpoint true "posts/<id>/repost/<rid>")
(def-vine-endpoint true "posts/<id>/complaints")
(def-vine-endpoint false "posts/search/<query>")

;; tags
(def-vine-endpoint false "tags/trending")
(def-vine-endpoint false "tags/search/<name>")

;; timelines
(def-vine-endpoint true "timelines/graph")
(def-vine-endpoint false "timelines/posts/<id>")
(def-vine-endpoint false "timelines/users/<id>")
(def-vine-endpoint false "timelines/users/<id>/likes")
(def-vine-endpoint false "timelines/tags/<name>")
(def-vine-endpoint false "timelines/popular")
(def-vine-endpoint false "timelines/promoted")
(def-vine-endpoint false "timelines/channels/<id>/popular")
(def-vine-endpoint false "timelines/channels/<id>/recent")
(def-vine-endpoint false "timelines/venues/<id>")

;; users
(def-vine-endpoint true "users")
(def-vine-endpoint true "users/authenticate")
(def-vine-endpoint true "users/me")
(def-vine-endpoint true "users/<id>/blocked/<uid>")
(def-vine-endpoint true "users/<id>/pendingNotificationsCount")
(def-vine-endpoint true "users/<id>/notifications")
(def-vine-endpoint true "users/<id>/followers")
(def-vine-endpoint true "users/<id>/following")
(def-vine-endpoint false "users/profiles/<id>")
(def-vine-endpoint false "users/search/<query>")

;; venues
(def-vine-endpoint false "venues/search/<name>")

