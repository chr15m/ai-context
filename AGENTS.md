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
- IMPORTANT: if the task is unreasonable of infeasible, or if any of the tests are incorrect, please tell the user.
- Do not hard-code any test cases. Tell the user if the problem is unreasonable instead of hardcoding passing tests.
- Never clean up any code unless the user asks you to.
- Favour small, functional, idempotent and immutabile components.
- Decomplect. Break things out into smaller functions each with a single purpose.

The user is a detail oriented anal-retentive control freak. Only do things they specifically ask you to do.

# --- DEBUGGING (CRITICAL RULES) ---

- Based on the code, start with one or more hypotheses about what's causing the bug. Do not jump to conclusions.
- If there is a test suite add one or more test cases that replicate the bug.
- Insert debugging statements liberally in order to confirm or falsify your hypotheses about the bug.
- Run & test the code again, check the logging output, or ask the user to do this.
- Iterate until you are sure you know how to fix the bug.
- Only once you know the cause of the bug should you issue a patch to fix it.
- Run the test cases, or ask the user to verify the bug is fixed.

## PRESERVING LOGS IS MANDATORY

This is a strict, non-negotiable rule.

1. **ALWAYS** keep all debugging and `console.log` statements that are added during the debugging process.
2. **NEVER** remove, comment out, or "clean up" these statements in any subsequent code patch.
3. The only exception is if the user **explicitly and verbatim** asks you to "remove the debugging statements". Do not infer this request.
4. If you believe the task is complete, present the code with the debugging statements still in place and wait for the user's next instruction. Do not move on to a "cleanup" step.

# Comment guidelines

- DO NOT add comments that explain changes made (like '// added x'). Comments should only explain what the code does.
- GOOD comment: `const x = 1; // set initial counter value`
- BAD comment:  `const x = 1; // added statement to set x to 1`
- Avoid adding comments unless they clarify non-obvious intent or complex logic.
- Do not add superfluous or verbose comments.

# Markdown style

- Always add one empty line after headings.

# Command line tools

If the user asks you to behave in an agentic manner, performing tasks, use the "```bash" block technique to run commands.

- You can use Linux CLI tools like these:
  - `grep STRING FILESPEC` to find STRING in files.
  - `cat FILE` to get a file's contents.
  - `echo "" > FILE` to zero out a file (useful for overwriting).
  - `ls PATH` to list files on a path.
  - `tree PATH` to display a tree of files.
  - `curl URL` to see the contents of a URL.
  - And you can call other Linux commands too like `find`.

You can also write more complex scripts to perform tasks and then run them with a bash block.

# Communication style

- You DO NOT need to tell the user (unless asked):
  - How to open index.html in the browser.
  - How to run a webserver to serve HTML.
  - To run the dev server.
  - To run `make watch`.

# Common bugs to avoid

- You can't put comments in JSON importmaps.
- Avoid the string "data" with a colon directly after it. Assemble this string if you need it.
- In LISP code be very careful about matching braces and check brace counts twice.
- Don't name vars after built-ins like 'val'.

# STRONGLY FAVOURED PARADIGMS

- Immutability
- Idempotency
- Functional programming
- DRY
- Single source of truth
- Minimal deps
- Under-engineering
