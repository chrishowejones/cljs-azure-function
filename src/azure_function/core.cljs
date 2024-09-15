(ns azure-function.core
  (:require ["@azure/functions" :refer [app]]))

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


(comment

  (.then (hello-handler #js {:query #js {:get (constantly nil)}
                             :text (constantly
                                    (js/Promise. (fn [resolve _] (resolve "text"))))}
                        #js {})
         #(prn %))

  (.then (hello #js {:query #js {:get (constantly nil)}
                     :text (constantly
                            (js/Promise. (fn [resolve _] (resolve "text"))))})
         #(prn %))

  (defn count-2+ [url]
    ;; *** CORRECT
    (js/Promise.resolve
     (if (empty? url)
       0
       (-> (js/fetch url)
           (.then (fn [r] (let [text (.text r)]
                            (prn [:req r])
                            (prn [:text text])
                            text)))
           (.then (fn [s]
                    (->> s
                         (.parse js/JSON)
                         js->clj)))))))

  (.then (js/fetch "https://google.co.uk") (fn [r] (println "r=" r)))

  (.then (count-2+ "https://google.co.uk") #(println "Result:" %))

  (def result (atom {:people nil}))

  (def res
    (count-2+ "http://localhost:3000/api/people"))

  (.then res (fn [text] (-> text first (get "id") prn)))

  @result

  (println "Hello!")

  (enable-console-print!)

  ;;
  )



