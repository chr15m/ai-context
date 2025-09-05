---
title: Node.js ClojureScript runtime (nbb)
description: Execute ad-hoc Node.js scripts using the ClojureScript runtime nbb for enhanced efficiency
tags: [lang:clojure]
---

# Node.js ClojureScript runtime (nbb)

`nbb` is a ClojureScript runtime that runs on Node.js for doing ad-hoc scripting.

You can import and use Node libraries and internals like this:

```clojure
(ns example
  (:require ["csv-parse/sync" :as csv]
            ["fs" :as fs]
            ["path" :as path]
            ["shelljs$default" :as sh]
            ["term-size$default" :as term-size]
            ["zx" :refer [$]]
            ["zx$fs" :as zxfs]
            [nbb.core :refer [*file*]]))

(prn (path/resolve "."))

(prn (term-size))

(println (count (str (fs/readFileSync *file*))))

(prn (sh/ls "."))

(prn (csv/parse "foo,bar"))

(prn (zxfs/existsSync *file*))

($ #js ["ls"])
```

The Reagent library is available with `[reagent.core :as r]` and you can render hiccup to HTML using e.g. `r/render-to-static-markup`.

The promesa library is available with `[promesa.core :as p]`.

The js-interop library is available with `[applied-science.js-interop :as j]`.

The Clojure `tools.cli` library is also available if you need to write command line utilities and display usage etc.

You can use `js/fetch` to access JavaScript's native `fetch` function.

To access datastructures returned by native JS calls (like `js/fetch`) you will need to use `aget` or `j/get` rather than `get`.

A basic main file:

```clojure
(ns example
  (:require
    [nbb.core :refer [*file* invoked-file]]))

(defn main [args]
  ; ... do something
  )

(when (= *file* (invoked-file))
  (main (j/get process :argv)))
```

### Running nbb

You can run nbb scripts with `npx` exactly as follows:

```
npx nbb scriptname.cljs
```

The `node_modules` dir is already added to the classpath by default - DO NOT add `-cp node_modules`.

The `package.json` may contain a shorthand for running `nbb` with special configuration, like this:

```
npm run nbb
```

If you know that's available in `package.json` then use it.

# Testing snippets

If you need to try something out, you can just nbb to test ClojureScript snippets with a command like this:

```
npx nbb -e '(require \'[promesa.core :as p] (print p))'
```
