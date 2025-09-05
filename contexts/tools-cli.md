# tools.cli

Tools for working with command line arguments in Clojure.

## Quick Start

The primary function is `parse-opts`, which parses command line arguments based on an options specification.

```clojure
(ns my.program
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ;; A non-idempotent option (:default is applied first)
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :update-fn inc]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn -main [& args]
  ;; parse-opts takes the command-line args and the options spec
  (parse-opts args cli-options))

(defonce started
  (apply -main (not-empty (js->clj (.slice js/process.argv 2)))))
```

Executing the command line:

    clojure -M -m my.program -vvvp8080 foo --help --invalid-opt

Produces a map like this:

```clojure
{:options   {:port 8080
             :verbosity 3
             :help true}
 ;; Non-option arguments
 :arguments ["foo"]
 ;; A string summarizing the options, useful for help messages
 :summary   "  -p, --port PORT  80  Port number\n  -v                   Verbosity level\n  -h, --help"
 ;; A vector of errors encountered during parsing
 :errors    ["Unknown option: \"--invalid-opt\""]}
```

**Note:** Exceptions are _not_ thrown on parse errors. Check the `:errors` key for a truthy value and handle errors explicitly.

## Example Usage

This example demonstrates more features of the options specification.

```clojure
(ns cli-example.core
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]])
  (:import (java.net InetAddress))
  (:gen-class))

(def cli-options
  [;; Short opt, long opt with arg, description
   ["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]

   ["-H" "--hostname HOST" "Remote host"
    :default (InetAddress/getByName "localhost")
    ;; :default-desc provides a cleaner string for the summary
    :default-desc "localhost"
    :parse-fn #(InetAddress/getByName %)]

   ;; Boolean option (no arg description)
   [nil "--detach" "Detach from controlling process"]

   ["-v" nil "Verbosity level; may be specified multiple times"
    ;; :id is required if no long option
    :id :verbosity
    :default 0
    ;; :update-fn for non-idempotent options
    :update-fn inc]

   ["-f" "--file NAME" "File names to read"
    ;; :multi allows multiple occurrences
    :multi true
    :default []
    ;; :update-fn receives existing value(s) and new value
    :update-fn conj]

   ["-t" nil "Timeout in seconds"
    :id :timeout
    ;; :required ensures an argument is provided
    :required "TIMEOUT"
    :parse-fn parse-long]

   ;; Boolean option with explicit --no-daemon form
   ["-d" "--[no-]daemon" "Daemonize the process" :default true]

   ["-h" "--help"]])

;; Function to generate usage instructions using the summary
(defn usage [options-summary]
  (->> ["Usage: program-name [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  start    Start a new server"
        "  stop     Stop an existing server"
        "  status   Print a server's status"]
       (string/join \newline)))

;; Function to format error messages
(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

;; Function to validate arguments after parsing
(defn validate-args
  "Validate command line arguments. Returns a map indicating exit or action."
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      ;; Handle help flag
      (:help options)
      {:exit-message (usage summary) :ok? true}

      ;; Handle parsing errors
      errors
      {:exit-message (error-msg errors)}

      ;; Validate arguments based on program logic
      (and (= 1 (count arguments))
           (#{"start" "stop" "status"} (first arguments)))
      {:action (first arguments) :options options}

      ;; Invalid arguments
      :else
      {:exit-message (usage summary)})))

;; Example of using validate-args in a main function
(defn -main [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (do ;; Simplified exit logic for example
        (println exit-message)
        (js/process.exit (if ok? 0 1)))
      (case action
        "start"  (println "Starting server with options:" options) ;; Replace with actual logic
        "stop"   (println "Stopping server with options:" options) ;; Replace with actual logic
        "status" (println "Checking server status with options:" options) ;; Replace with actual logic
        ))))
```

## Generating Help Messages

A common requirement for command-line tools is to display usage instructions when a specific flag, like `-h` or `--help`, is provided. `tools.cli` facilitates this through the `:summary` key returned by `parse-opts`.

1.  **Define a Help Option:** Include a standard boolean option for help in your `cli-options` definition. The long option name (e.g., `help`) will become the key in the parsed `:options` map.
    ```clojure
    (def cli-options
      [;; ... other options ...
       ["-h" "--help"]]) ; Defines :help in the options map
    ```

2.  **Parse Arguments:** Call `parse-opts` as usual to get the parsed options, errors, and the summary.
    ```clojure
    (let [{:keys [options errors summary]} (parse-opts args cli-options)]
      ;; ... process results ...
      )
    ```

3.  **Check for the Help Option:** After parsing, check if the `:help` key (derived from the `--help` long option) is true in the `options` map.

4.  **Display Usage using Summary:** If the help option is present, use the `:summary` string, which contains a pre-formatted list of all defined options, their defaults, and descriptions. You typically combine this summary with other static usage text (like program description or command examples).

**Example Implementation:**

The `validate-args` function from the example above demonstrates this pattern effectively:

```clojure
;; Function to generate usage instructions using the summary
(defn usage [options-summary]
  (->> ["Usage: program-name [options] action" ; Program-specific usage info
        ""
        "Options:"
        options-summary ; The summary string from parse-opts is inserted here
        ""
        "Actions:" ; More program-specific info
        "  start    Start a new server"
        "  stop     Stop an existing server"
        "  status   Print a server's status"]
       (string/join \newline)))

;; Function to validate arguments after parsing
(defn validate-args
  "Validate command line arguments. Returns a map indicating exit or action."
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      ;; Handle help flag by calling usage with the summary
      (:help options)
      {:exit-message (usage summary) :ok? true}

      ;; Handle parsing errors
      errors
      {:exit-message (error-msg errors)}

      ;; ... other validation logic ...

      ;; Invalid arguments - also show usage
      :else
      {:exit-message (usage summary)})))

;; Example main function using validate-args
(defn -main [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (do ;; Simplified exit logic for example
        (println exit-message) ; Print the usage or error message
        (js/process.exit (if ok? 0 1))) ; Exit with appropriate code
      (case action
        ;; ... handle actions ...
        ))))
```

This approach ensures that your help message automatically reflects the options defined in `cli-options`, including their descriptions and defaults, reducing the effort needed to keep help messages synchronized with the actual options.
