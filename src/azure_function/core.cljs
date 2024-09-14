(ns azure-function.core
  (:require [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            ["@azure/functions" :refer [app]]))

(defn format-response [name]
  (let [name (if (or (nil? name)
                     (= name ""))
               "World"
               name)]
    #js {:status 200
         :body (str "Hello, " name "!")}))

(defn hello [^js req ^js context]
  (js/Promise.resolve
   (-> req
       .text
       (.then (fn [text]
                (if (= text "")
                  (format-response (-> req .-query (.get "name")))
                  (format-response text)))))))

(app.http "hello" #js {:methods #js ["GET" "POST"]
                       :authLevel "anonymous"
                       :handler hello})




