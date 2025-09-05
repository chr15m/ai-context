# General development conventions

- NEVER make changes the user hasn't asked for without checking.
- Please modify files as minimally as possible to accomplish the task.
- Favour concise code with fewer lines, though not at the expense of readability.
- Make liberal use of debug statements with clear labels (e.g., `console.log('Processing data:', data)`) that identify location and context.
- Don't try to do too many things at once.
- Fix one thing at a time and test the code between every change.
- Build functionality in small steps, testing in between.
- Prototype with libraries to get them working before integrating them.
- Match the existing code style, naming conventions, and patterns unless specifically asked to change them.
- Think like you're making a clean git commit - changes should be focused, related, and minimal.
- Make patches and code replacement specifications as small, granular, and focused as possible.
- Restrict your changes to ONLY what was asked for. Ask before going beyond what was instructed.
- Don't make superfluous changes, whitespace changes, or changes to code that don't relate to the current goal.
- Do not add trailing whitespace to the end of lines. Maintain consistent indentation and line endings.
- Before implementing changes, consider how to make the code simpler and more concise.
- Break long lines of code at 80 characters.
- Look for opportunities to refactor repeated code patterns ONLY in the code you are changing or adding, not in existing code.
- Prefer built-ins to library calls wherever possible.
- When code blocks get nested too deeply refactor into smaller idempotent functions.
- Keep error handling simple and minimal:
  - Let underlying system and library errors pass through unchanged.
  - Don't catch and re-throw with new messages unless absolutely necessary.
  - Only add new error handling for logic in the code being modified.
  - Be consistent with error types and minimize the number of different exceptions.
  - Only handle the most essential errors relevant to the codebase.
- IMPORTANT: if the task is unreasonable of infeasible, or if any of the tests are incorrect, please tell me.
- Do not hard-code any test cases. Tell me if the problem is unreasonable instead of hardcoding passing tests.
- Never clean up any code unless the user asks you to.

# Debugging

- To confirm a hypothesis about a bug always insert debugging statements.
- Ask the user to tell you what is printed from debugging statements to get insight.
- Never remove debugging statements unless the user asks you to clean up.

# Comment guidelines

- DO NOT add comments that explain changes made (like '// added x'). Comments should only explain what the code does.
- GOOD comment: `const x = 1; // set initial counter value`
- BAD comment:  `const x = 1; // added statement to set x to 1`
- Avoid adding comments unless they clarify non-obvious intent or complex logic.
- Do not add superfluous or verbose comments.

# Markdown style

- Always add one empty line after headings.

# Command line tools

- You can use Linux CLI tools to explore and understand the codebase:
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
- In LISP code be very careful about matching braces and check brace counts twice.
