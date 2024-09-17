(ns azure-function.core
  ;; Have to use :as or shadow-cljs doesn't extern the functions refs
  ;; Note: just using :refer didn't work
  (:require ["@azure/functions" :as funcs]))

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

;; Register the Azure function as an HTTP function against URL /api/hello
(funcs/app.http "hello" #js {:methods #js ["GET" "POST"]
                       :authLevel "anonymous"
                       :handler hello-handler})


(comment

  (.then (hello-handler #js {:query #js {:get (constantly nil)}
                             :text (constantly
                                    (js/Promise. (fn [resolve _] (resolve "name in body"))))}
                        #js {})
         #(prn %))

  (.then (hello-handler #js {:query #js {:get (constantly "name in query")}}
                        #js {})
         #(prn %))


  (enable-console-print!)

  ;;
  )



