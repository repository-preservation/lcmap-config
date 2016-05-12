(ns lcmap.config.cassaforte-test
  (:require [clojure.test :refer :all]
            [lcmap.config.cassaforte :refer :all]))

(deftest db-config-test
  (testing "hosts"
    (let [[hosts opts] (connect-opts {:db-hosts ["foo","bar"]})]
      (is (= ["foo","bar"] hosts))))
  (testing "credentials"
    (let [[hosts opts] (connect-opts {:db-user "cat" :db-pass "mew"})]
      (is (= "cat" (get-in opts [:credentials :username])))
      (is (= "mew" (get-in opts [:credentials :password]))))))
