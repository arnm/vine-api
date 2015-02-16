(ns vine.test.api
  (:use [clojure.test]
        [vine.api]))

(def user-id "955681242773340160")
(def post-id "1178768651474640896")
(def channel-id "1070175184667013120")
(def venue-id "4e72b98a62e1a77aac80bb04")

(deftest test-channels
  (is (= 200 (:status @(channels-featured)))))

(deftest test-posts
  (is (= 200 (:status @(posts-search-query "test")))))

(deftest test-tags
  (is (= 200 (:status @(tags-trending))))
  (is (= 200 (:status @(tags-search-query "random")))))

(deftest test-timelines
  (is (= 200 (:status @(timelines-posts-id post-id))))
  (is (= 200 (:status @(timelines-users-id user-id))))
  (is (= 200 (:status @(timelines-users-id-likes user-id))))
  (is (= 200 (:status @(timelines-tags-name "test"))))
  (is (= 200 (:status @(timelines-popular))))
  (is (= 200 (:status @(timelines-promoted))))
  (is (= 200 (:status @(timelines-channels-id-popular channel-id))))
  (is (= 200 (:status @(timelines-channels-id-recent channel-id))))
  (is (= 200 (:status @(timelines-venues-id venue-id)))))

(deftest test-users
  (is (= 200 (:status @(users-profiles-id user-id))))
  (is (= 200 (:status @(users-search-query "bob")))))

(deftest test-venues
  (venues-search-query "texas" (fn [r] (is (= 200 (:status r))))))
