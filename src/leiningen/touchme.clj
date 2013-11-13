(ns leiningen.touchme)

(defn get-files-with-ext
  [dir ext]
  (let [fs (file-seq dir)
        ext? #(.endsWith (.getName %) ext)]
    (filter ext? fs)))

(defn get-files-state
  [path ext]
  (let [dir (clojure.java.io/file path)
        dir-path (.getAbsolutePath dir)
        dir-path-length (+ 1 (count dir-path))]
    (reduce #(assoc %
                    (subs (.getAbsolutePath %2) dir-path-length)
                    (.lastModified %2))
            {}
            (get-files-with-ext dir ext))))

(defn modified-files
  [current-state last-state]
  (filter #(not (= (get current-state %) (get last-state %))) (keys current-state)))

(defn touch-file
  [file]
  (.setLastModified file (System/currentTimeMillis)))

(defn touch
  [file-path]
  (touch-file (clojure.java.io/file file-path)))

(defn update-file
  [file-path _ _]
  (touch file-path))

(defn get-clj-file
  [path ext]
  (clojure.string/replace path ext ".clj"))

(defn update-files
  [base-path ext modified]
  (apply touch (map #(get-clj-file (str base-path "/" %) ext) modified)))

(defn touchme
  "Touch clj file when resource is updated."
  [project & args]
  ((println "Wait for changes…")
    (let [conf (:touchme-config project)
          path-to-observe (:path-to-observe conf)
          extension-to-observe (:extension-to-observe conf)
          update (if (:file-to-touch conf)
                    (partial update-file (str (first (:source-paths project)) "/" (:file-to-touch conf)))
                    (partial update-files (str (first (:source-paths project)) "/" (:path-of-files-to-touch conf)) extension-to-observe))]
      (loop [current-state (get-files-state path-to-observe extension-to-observe)
             changes current-state]
        (when (not (= current-state changes))
          (println ".")
          (update (modified-files current-state changes)))
        (Thread/sleep 1000)
        (recur changes (get-files-state path-to-observe extension-to-observe))))))
