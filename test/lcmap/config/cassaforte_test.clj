(ns lcmap.config.cassaforte-test
  (:require [clojure.test :refer :all]))

(deftest db-config-test
  (testing "hosts"
    (let [[hosts opts] (connect-opts {:hosts ["foo","bar"]})]
      (is (= ["foo","bar"] hosts))))
  (testing "credentials"
    (let [[hosts opts] (connect-opts {:user "cat" :pass "mew"})]
      (is (= "cat" (get-in opts [:credentials :username])))
      (is (= "mew" (get-in opts [:credentials :password]))))))
