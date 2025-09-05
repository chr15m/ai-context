#!/usr/bin/env -S npx nbb
(ns nbb-cli-template
  {:clj-kondo/config '{:lint-as {promesa.core/let clojure.core/let}}}
  (:require
    [clojure.tools.cli :as cli]
    [promesa.core :as p]
	["fs" :as fs]
    ; Add other required dependencies here
    ))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :default 8000
    :parse-fn js/Number
    :validate [#(< 1024 % 0x10000) "Must be a number between 1024 and 65536"]]
   
   ;; Boolean option (no argument)
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :update-fn inc] ;; Allows for -vvv to set verbosity to 3
   
   ;; Another example with validation
   ["-d" "--dir DIR" "Directory path"
    :default "./"
    :validate [fs/existsSync "Directory must exist"]]
   
   ;; Help option
   ["-h" "--help" "Show this help"]])

(defn print-usage [summary]
  (println "Usage:")
  (println "  npx nbb script.cljs [options]")
  (println)
  (println "Options:")
  (println summary))

(defn main [& args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      errors
      (do
        (doseq [error errors]
          (println error))
        (print-usage summary)
        (js/process.exit 1))
      
      (:help options)
      (do
        (print-usage summary)
        (js/process.exit 0))
      
      :else
	  (let [port (:port options)
	        dir (:dir options)
	        verbosity (:verbosity options)]
	    
	    (when (> verbosity 0)
	      (println "Running with options:" (pr-str options))
	      (println "Arguments:" (pr-str arguments)))
	    
	    (println "Starting program with port" port "and directory" dir)
	    
	    (p/let [result (p/resolved "async operation completed")]
	      (println result))))))

(defn get-args [argv]
  (if *file*
    (let [argv-vec (mapv
                     #(try (fs-sync/realpathSync %)
                           (catch :default _e %))
                     (js->clj argv))
          script-idx (.indexOf argv-vec *file*)]
      (when (>= script-idx 0)
        (not-empty (subvec argv-vec (inc script-idx)))))
    (not-empty (js->clj (.slice argv
                                (if
                                  (or
                                    (.endsWith
                                      (or (aget argv 1) "")
                                      "node_modules/nbb/cli.js")
                                    (.endsWith
                                      (or (aget argv 1) "")
                                      "/bin/nbb"))
                                  3 2))))))

(defonce started
  (apply main (not-empty (get-args js/process.argv))))
