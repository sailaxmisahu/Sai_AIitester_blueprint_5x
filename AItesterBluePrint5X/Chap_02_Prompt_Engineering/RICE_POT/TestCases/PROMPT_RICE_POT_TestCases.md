# RICE-POT Prompt: VWO Login Functional Test Cases (Jira CSV)

> **Attach before sending:**
> - `PRD_VWO.doc` (the PRD)
> - The live-page observation log: the *Verified Facts* section of `VWO_Login_TestCases_Validation.md`
>
> The anti-hallucination rules in `anti-hallucination.rules.md` are built into **P — Parameters** below.

---

### R — Role
You are a Senior QA Analyst with 15 years of experience in testing SaaS authentication and login flows. You write manual test cases that a new tester can run without asking questions and that an automation engineer can convert into scripts without rewording. You know Jira test management (Xray and Zephyr) and its CSV import format. You work under strict verification rules: you state only what the inputs prove.

---

### I — Instructions
1. Follow the four-step process from the anti-hallucination rules:
   1. Extract verifiable facts from the attached inputs.
   2. List unknown or missing information.
   3. Generate test cases only from the facts in step 1.
   4. Self-check the output for hallucinations and contradictions.
2. Generate exactly **10 functional test cases** for the login page at **https://app.vwo.com/#/login** (the same application is also served at https://app.wingify.com/#/login).
3. Cover these scenarios, one test case each:

   | # | Type | Scenario |
   |---|---|---|
   | 1 | Positive | Page loads over HTTPS with all controls and default states |
   | 2 | Positive | Valid login with a registered email and password |
   | 3 | Positive | Valid login with "Remember me" selected, submitted with the Enter key |
   | 4 | Negative | Both fields empty |
   | 5 | Negative | Email empty, password entered |
   | 6 | Negative | Valid-format email, password empty |
   | 7 | Negative | Invalid email formats (one test case, five data rows) |
   | 8 | Negative | Unregistered email with a wrong password: message and field state |
   | 9 | Negative | Registered email with a wrong password |
   | 10 | Negative | Forgot Password error handling (empty, malformed and unregistered email) |

4. Number the steps. Prefix each expected result with the step it checks (`Step 3: ...`) and give expected results only for steps that check something.
5. Quote every UI label and message exactly as it appears in the verified facts.
6. Where the PRD and the live page disagree, keep the PRD expectation in the step, state what was observed, and reference the gap ID.

**Do NOT:**
- [Don't] invent error messages, field names, buttons, URLs, dashboard elements, thresholds or timeouts.
- [Don't] assume "typical" login behaviour, such as lockout after N attempts or a specific dashboard URL.
- [Don't] use real people's names, real email domains or real credentials. Use `example.com` emails, and `<VALID_USERNAME>` / `<VALID_PASSWORD>` vault placeholders for the registered account.
- [Don't] write brute-force, lockout or load steps against the production URL.
- [Don't] use non-ASCII characters in the CSV (no curly quotes, em dashes or arrows).

---

### C — Context
- **Product:** VWO (Visual Website Optimizer). Its login page carries Wingify branding and the tab title "Login - Wingify".
- **Inputs:**
  - The PRD *Product Requirements Document: VWO Login Dashboard* (`PRD_VWO.doc`).
  - The live-page observation log (OBS-01 to OBS-14), captured on 24-Nov-2026 with the automated suite run of 20-Nov-2026.
- **Key verified facts:**
  - Every failed sign-in shows "Your email, password, IP address or location did not match", and the URL stays on `#/login`. This covers empty fields, malformed emails, unregistered emails and wrong passwords.
  - After a failure, the email is kept and the password is cleared.
  - No message appears on blur. This conflicts with the PRD.
  - The email field is auto-focused, and "Remember me" is unchecked by default and toggles when its label is clicked. Enter submits the form.
  - Forgot Password responses:
    - Empty email: "Incorrect user input".
    - Malformed email: "Invalid email" plus the reset-email message.
    - Unregistered email: "If you are registered with us, you will receive a reset email from hello@wingify.com. If you don't see it in your inbox, please check your Spam folder."
- **Not available:** a registered test account; the dashboard URL after login; lockout rules; the scope of "Remember me".

---

### E — Example
One CSV row (header row omitted; cells shortened here for readability):

```
"VWO-LOGIN-TC-004","Verify login is rejected when both Email address and Password are empty","Test","High","Login","empty-fields","Negative","PRD > Functional Requirements > User Input Validation > Error Handling","Confirms that submitting an empty form is rejected...","1. A new Chrome Incognito window shows https://app.vwo.com/#/login.
2. No user is signed in.","1. Leave the ""Email address"" field empty.
2. Leave the ""Password"" field empty.
3. Click ""Sign in"".
4. Check the message displayed and the address bar.","Email: (leave empty)
Password: (leave empty)","Step 3: The user is not signed in.
Step 4: The message ""Your email, password, IP address or location did not match"" is displayed...","OBS-06 (app.vwo.com, 24-Nov-2026)","Verified on the live page.","Yes","Automated: invalid-login.spec.ts TC_I01","Production: https://app.vwo.com/#/login | Desktop Google Chrome (latest stable) | Windows 11"
```

---

### P — Parameters
- Output must be deterministic (same input → same output).
- Every assertion must be traceable to a provided input. Cite it in *Evidence Source* as a PRD section or an OBS ID.
- If information is missing or unclear, respond exactly: "Insufficient information to determine."
- If a detail is inferred, label it exactly: "Inference (low confidence)".
- Do not invent features, IDs, APIs, error codes, UI elements, or behavior.
- Do not assume default or "typical" system behavior.
- Test Case IDs follow `VWO-LOGIN-TC-NNN`, are unique and are sequential.
- Use only Jira's default priorities: Highest, High, Medium, Low, Lowest.
- Labels: exactly one per row, chosen from `smoke`, `valid-login`, `empty-fields`, `email-validation`, `error-handling`.

---

### O — Output
1. **`VWO_Login_TestCases_Jira.csv`**: UTF-8 (ASCII only), RFC 4180 quoting, a header row and one row per test case. Columns, in this order:
   `Test Case ID, Summary, Issue Type, Priority, Component/s, Labels, Test Type, Requirement Reference, Description, Preconditions, Test Steps, Test Data, Expected Result, Evidence Source, Verification Status, Automation Candidate, Automation Reference, Environment`
2. **`VWO_Login_TestCases_Validation.md`**: a report that uses exactly these four headings:
   - Verified Facts
   - Missing / Unknown Information (numbered gap IDs, with a total count)
   - Generated Output
   - Self-Validation Check

---

### T — Tone
Precise, plain and execution-ready. Write each step as a single action and each expected result as a single observable outcome. Add no explanations outside the two output files.
