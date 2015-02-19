(ns vine.test.api
  (:require [clojure.data.json :as json]
            [environ.core :refer [env]])
  (:use [clojure.test]
        [vine.api]))

(def user-id (env :user-id))
(def post-id (env :post-id))
(def channel-id (env :channel-id))
(def venue-id (env :venue-id))
(def current-user (atom {}))
(def user-creds (atom {}))

(defn update-values [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn before-tests []
  (let [res (->> @(users-authenticate :post
                                      {:form-params
                                       {:username (env :email)
                                        :password (env :password)}})
                 :body
                 ((fn [s] (json/read-str s :key-fn keyword))))]
    (reset! current-user (update-values (:data res) str))
    (reset! user-creds {:headers {"vine-session-id" (:key @current-user)}})))

(defn after-tests []
  @(users-authenticate :delete @user-creds))

(defn authenticate [f]
  (before-tests)
  (f)
  (after-tests))

(use-fixtures :once authenticate)

(deftest test-channels
  (is (= 200 (:status @(channels-featured :get)))))

(deftest test-posts
  (is (= 200 (:status @(posts-id-likes :post
                                       (env :post-id)
                                       @user-creds))))
  (is (= 200 (:status @(posts-id-likes :delete
                                       (env :post-id)
                                       @user-creds))))
  (is (= 200 (:status @(posts-search-query :get "test")))))

(deftest test-tags
  (is (= 200 (:status @(tags-trending :get))))
  (is (= 200 (:status @(tags-search-name :get "random")))))

(deftest test-timelines
  (is (= 200 (:status @(timelines-graph :get @user-creds))))
  (is (= 200 (:status @(timelines-posts-id :get post-id))))
  (is (= 200 (:status @(timelines-users-id :get user-id))))
  (is (= 200 (:status @(timelines-users-id-likes :get user-id))))
  (is (= 200 (:status @(timelines-tags-name :get "test"))))
  (is (= 200 (:status @(timelines-popular :get))))
  (is (= 200 (:status @(timelines-promoted :get))))
  (is (= 200 (:status @(timelines-channels-id-popular :get channel-id))))
  (is (= 200 (:status @(timelines-channels-id-recent :get channel-id))))
  (is (= 200 (:status @(timelines-venues-id :get venue-id)))))

(deftest test-users
  ;; TODO: POST - users
  (is (= 200 (:status @(users-me :get @user-creds))))
  (is (= 200 (:status @(users-id-blocked-uid :post
                                             (:userId @current-user)
                                             user-id
                                             @user-creds))))
  (is (= 200 (:status @(users-id-blocked-uid :delete
                                             (:userId @current-user)
                                             user-id
                                             @user-creds))))
  (is (= 200 (:status @(users-id-pendingNotificationsCount :get
                                                           (:userId @current-user)
                                                           @user-creds))))
  (is (= 200 (:status @(users-id-notifications :get
                                               (:userId @current-user)
                                               @user-creds))))
  (is (= 200 (:status @(users-id-followers :get
                                           (:userId @current-user)
                                           @user-creds))))
  (is (= 200 (:status @(users-id-following :get
                                           (:userId @current-user)
                                           @user-creds))))
  (is (= 200 (:status @(users-profiles-id :get user-id))))
  (is (= 200 (:status @(users-search-query :get "bob")))))

(deftest test-venues
  (venues-search-name :get "texas" (fn [r] (is (= 200 (:status r))))))
