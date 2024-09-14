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

(defn hello [^js req]
  (if-let [name (-> req .-query (.get "name"))]
     (format-response name)
     (-> req .text (.then #(format-response %)))))

(defn hello-handler [^js req ^js _context]
  (js/Promise.resolve (hello req)))

(app.http "hello" #js {:methods #js ["GET" "POST"]
                       :authLevel "anonymous"
                       :handler hello-handler})




