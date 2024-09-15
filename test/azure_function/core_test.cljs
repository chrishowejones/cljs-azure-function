(ns azure-function.core-test
  (:require [cljs.test :refer-macros [async deftest is testing]]
            [azure-function.core :refer [hello-handler]]))

(deftest when-name-in-query-string-return-greeting
  (let [cxt #js {}
        req #js {:query #js {:get (constantly "query")}
                 :text (constantly nil)}]
    (async done
     (-> (hello-handler req cxt)
         (.then (fn [res]
                  (is (= (js->clj res)
                         {"status" 200 "body" "Hello, query!"}))
                  (done)))))))

(deftest when-name-in-body-return-greeting
  (let [cxt #js {}
        req #js {:query #js {:get (constantly nil)}
                 :text (constantly
                        (js/Promise.
                         (fn [resolve _] (resolve "body"))))}]
    (async done
     (-> (hello-handler req cxt)
         (.then (fn [res]
                  (is (= (js->clj res)
                         {"status" 200 "body" "Hello, body!"}))
                  (done)))))))
