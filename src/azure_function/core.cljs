(ns azure-function.core
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            ["@azure/functions" :refer [app]]))

(defn format-response [name]
  (let [name (if (= name "") "World" name)]
   #js {:status 200
        :body (str "Hello, " name "!")}))

(defn hello [^js req ^js context]
  (js/Promise.
   (fn [resolve _]
     (.log context "Hello function processed request for url" (.-url req))
     (if-let [query-name (-> req .-query (.get "name"))]
       (do
         (println "Name=" query-name)
         (resolve (format-response query-name)))
       (go
         (let [body-name (<p! (.then (.text req)))]
           (println "Name from body text=" body-name)
           (resolve (format-response body-name))))))))

(app.http "hello" #js {:methods #js ["GET" "POST"]
                       :authLevel "anonymous"
                       :handler hello})




