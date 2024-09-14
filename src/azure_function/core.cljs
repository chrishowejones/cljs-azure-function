(ns azure-function.core
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            ["@azure/functions" :refer [app]]))

(defn format-response [name]
  (let [name (if (= name "") "World" name)]
   #js {:status 200
        :body (str "Hello, " name "!")}))

(defn ^:export hello [^js req ^js _context]
  (js/Promise.
   (fn [resolve _]
     (println "Http function processed request for url:" (.-url req))
     (if-let [name (-> req .-query (.get "name"))]
       (resolve (format-response name))
       (.then (.text req) #(resolve (format-response %)))))))

(app.http "hello" #js {:methods #js ["GET" "POST"]
                       :authLevel "anonymous"
                       :handler hello})




