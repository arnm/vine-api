(ns vine.test.api
  (:use [clojure.test]
        [vine.api]))

(deftest get-post-test
  (testing "Valid response"
    @(timelines-popular {:query-params {:size 1}} (fn [{:keys [status body error]}]
                             (is (= 200 status))
                             (println body)))))
