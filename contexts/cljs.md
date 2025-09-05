# ClojureScript guidance

- Pay attention to the order of function declaration. Always put function definitions *before* calling them.
- Don't use `defn-` for private fns. Just make all functions public.
- Combine multiple swaps into a single swap.
  Instead of this:
  ```
  (swap! state assoc-in [:ui :busy :saving] false)
  (swap! state assoc :saved-beat (:beat @state))
  ```
  Do this:
  ```
  (swap! state
         #(-> %
              (assoc-in [:ui :busy :saving] false)
              (assoc :saved-beat (:beat @state))))
  ```
