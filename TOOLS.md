Use these shell commands to accomplish specific tasks.

`search.prompt "A description of what you want to search for."` - conducts and LLM-driven web search from a plain text description.
`research.prompt "A description of the thing you want researched."` - conducts in-depth research and outputs a report from a plain text description.
`url-to-md URL` - fetches URL and converts it to Markdown.
`bremote ARGS` - remote control a browser instance with playwright - see below
`chromium-browser --remote-debugging-port=9222 --remote-allow-origins=*` - launch a browser instance with remote debugging on.
`pi @file-1 @file-2 "Some prompt here"` - launch a sub-agent.

### `bremote` usage

`bremote --check` - check if browser is running and connectable.
`bremote --snapshot` - print a text snapshot of the current page (interactive elements get numeric IDs).
`bremote --screenshot PATH` - save a screenshot of the current page to a file.
`bremote --resize WIDTH HEIGHT` - resize window to width and height.
`bremote --classes` - include class attributes in the snapshot output (use with --snapshot).
`bremote --dump` - dump the raw innerText of the page and frames.
`bremote --click ID` - click an element by numeric ID or CSS selector.
`bremote --type ID TEXT` - type text into an element by numeric ID or CSS selector.
`bremote --press KEY` - press a keyboard key (e.g. `Enter`, `Tab`).
`bremote --js SCRIPT` - evaluate JavaScript on the page and print the result.
`bremote --pw '.some_chained_command("...")' - eval a playwright chained command onto .context[0].page[-1].
`bremote --sleep MS` - sleep for a number of milliseconds.
`bremote --wait-network` - wait for the page network to be idle.
`bremote --wait-element SELECTOR` - wait for a CSS selector to appear.
`bremote --dump` - dump the raw inner text of the page and all frames.
`bremote --close` - close the browser.

Requires a running Chromium instance with remote debugging enabled:
- `agentic-browser` (simple script to launch it) or;
- `chromium-browser --remote-debugging-port=9222 --remote-allow-origins=*`
