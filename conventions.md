# General development conventions

- NEVER make changes the user hasn't asked for without checking.
- Please modify files as minimally as possible to accomplish the task.
- Favour concise code with fewer lines, though not at the expense of readability.
- Make liberal use of debug statements, especially after errors are found.
- Don't try to do too many things at once.
- Avoid adding lots of verbose comments unless asked.
- Try to write code that is DRY.
- Fix one thing at a time and test the code between every change.
- Make patches and code replacement specifications as small, granular, and focused as possible. Try not to change huge blocks of code at once.
- Restrict your changes to ONLY what was asked for. Ask before going beyond what was instructed.
- Don't make superfluous changes, whitespace changes, or changes to code that don't relate to the current goal.
- Do not add trailing whitespace to the end of lines. Keep patches tidy.
- Before implementing changes aggressively refactor and think about how to make the code simpler and more concise.
- Break long lines of code at 80 characters.
- Comments should only be to explain code, not to explain what you have changed in a SEARCH/REPLACE block.

# Command line tools

- You can use CLI tools to explore and understand the codebase:
  - `grep STRING FILESPEC` to find STRING in files.
  - `cat FILE` to get a file's contents.
  - `echo "" > FILE` to zero out a file (useful for overwriting).
  - `ls PATH` to list files on a path.
  - `tree PATH` to display a tree of files.
  - And you can call other Linux commands too like `find`.

# Communication style

- You DO NOT need to tell me (unless asked):
  - How to open index.html in the browser.
  - How to run a webserver to serve HTML.
  - To run the dev server.
  - To run `make watch`.

# Common bugs to avoid

- You can't put comments in JSON importmaps.
- Avoid the string "data" with a colon directly after it. Assemble this string if you need it.

