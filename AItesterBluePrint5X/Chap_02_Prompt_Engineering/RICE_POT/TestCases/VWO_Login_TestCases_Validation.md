# VWO Login Test Cases: Anti-Hallucination Validation

**Output:** [VWO_Login_TestCases_Jira.csv](VWO_Login_TestCases_Jira.csv), 10 functional login test cases in a Jira CSV import format.
**Prompt used:** [PROMPT_RICE_POT_TestCases.md](PROMPT_RICE_POT_TestCases.md), the RICE-POT method plus the rules in `anti-hallucination.rules.md`.
**Date:** 24-Nov-2026

**Inputs used.** These are the only sources allowed by the anti-hallucination rules:

| Input type (per rules) | Source |
|---|---|
| PRD | `PRD_VWO.doc`, *Product Requirements Document: VWO Login Dashboard* |
| Logs | Live-page observation logs captured with Playwright Chromium against https://app.vwo.com/#/login and https://app.wingify.com/#/login on 24-Nov-2026, plus the automated suite run of 20-Nov-2026 (`PlaywriteFramework/Output`) |
| Test data | Synthetic data only. Emails use the reserved `example.com` domain, and passwords are invented strings. No real accounts. |

No other knowledge of VWO was used to write expected results.

---

## Verified Facts

### From the PRD

| ID | PRD section | Statement used |
|---|---|---|
| P-01 | Existing Features | Email address and password input fields; Remember Me checkbox; link to free-trial signup |
| P-02 | Login Process | Email and password based login with secure validation; optional 2FA; enterprise SSO |
| P-03 | User Input Validation | Field validation on blur; automatic email format validation; clear, actionable error messages |
| P-04 | Password Management | Forgot Password flow; email-based reset |
| P-05 | Interface Design | Auto-focus on the first input; clickable labels |
| P-06 | Accessibility Features | Full keyboard accessibility for all interactive elements |
| P-07 | Data Protection | HTTPS enforcement |
| P-08 | Compliance Standards | Rate limiting through request throttling |
| P-09 | Platform Integrations / Error Recovery Flow | Seamless transition to the main dashboard; clear indication of successful login |
| P-10 | Returning User Experience | Remembered credentials option (Quick Access) |

### From the live-page observation logs

| ID | Host, date | Observation |
|---|---|---|
| OBS-01 | app.vwo.com, 24-Nov | `http://app.vwo.com/` redirects to `https://app.vwo.com/#/login`. Tab title is "Login - Wingify". app.wingify.com serves the same page (same title and element IDs), and `http://` also redirects to `https://` there. |
| OBS-02 | app.vwo.com, 24-Nov | Visible controls: "Email address" (placeholder "Enter email ID"), "Password" (placeholder "Enter password"), eye icon "Toggle password visibility", "Forgot Password?", "Remember me", "Sign in", "Sign in with Google", "Sign in using SSO", "Sign in with Passkey", "Don't have an account? Start a free trial", and the text "By continuing, you agree to Wingify's Privacy policy & Terms." |
| OBS-03 | both hosts, 24-Nov | The cursor is in the Email address field on load. "Remember me" is unchecked by default. No error message is shown on load. |
| OBS-04 | app.vwo.com 24-Nov; app.wingify.com 20-Nov | Clicking the "Remember me" label toggles the checkbox. |
| OBS-05 | app.vwo.com 24-Nov; app.wingify.com 20-Nov | The password field is masked. The eye icon switches it to plain text, and a second click masks it again. |
| OBS-06 | see the list below | Every failed sign-in shows "Your email, password, IP address or location did not match" and the URL stays on `#/login`. |
| OBS-07 | both hosts, 24-Nov | After a failed sign-in, the email value is kept and the password field is cleared. On app.wingify.com, the error also stayed visible while the user edited the email. |
| OBS-08 | app.wingify.com, 24-Nov | Pressing Enter in the Password field submits the form. |
| OBS-09 | app.wingify.com, 24-Nov | After a malformed email and Tab (blur), no field-level message appears. The form has `novalidate`, and the field has no `aria-invalid`. |
| OBS-10 | 24-Nov | "Forgot Password?" opens a form with "Email address" (focused), "Back" and "Reset Password"; Back returns to login. Results of Reset Password:<br>• Empty: "Incorrect user input".<br>• `jane.doe.example.com`: "Invalid email" with the field, **and** the reset-email message.<br>• `qa.unregistered.user@example.com`: "If you are registered with us, you will receive a reset email from hello@wingify.com. If you don't see it in your inbox, please check your Spam folder." (both hosts) |
| OBS-11 | 24-Nov | The link addresses for "Start a free trial" (app.vwo.com), "Privacy policy" and "Terms" (both hosts) contain an unfilled `{{$root.VWO_DOMAIN...}}` placeholder. Clicking "Privacy policy" on app.wingify.com opened a browser error page. |
| OBS-12 | app.wingify.com, 24-Nov | Tab order from the email field: Password, eye icon, Forgot Password?, Sign in, Google, SSO, Passkey, free trial, Privacy policy, Terms. **"Remember me" is skipped**: the checkbox is `display:none` and its label is not focusable. |
| OBS-13 | app.wingify.com, 24-Nov | "Sign in using SSO" opens `#/sso`. Empty email: "Incorrect user input". An `example.com` email: "Single Sign-On (SSO) is not enabled for your account. Enter the password to sign in." |
| OBS-14 | app.wingify.com, 24-Nov | While a sign-in is processing, the Sign in button gets an `is-in-action` state for under 1 second; its text is unchanged. The error message container has no `aria-live` or `role`. No password-strength indicator and no Light/Dark Mode banner were found. |

**OBS-06 coverage.** These failed sign-ins all showed the same message:
- Both fields empty: app.vwo.com 24-Nov; app.wingify.com 20-Nov.
- Empty email with a password: app.wingify.com 24-Nov.
- Valid-format email with an empty password: app.wingify.com 20-Nov.
- Five malformed emails: app.wingify.com 24-Nov. Row 1 was also tried on app.vwo.com.
- Unregistered email with a wrong password: both hosts.

---

## Missing / Unknown Information

**Total: 30 gaps.** In addition, there are **2 candidate defects**.

| Category | Count | Gap IDs |
|---|---|---|
| Blocks execution | 2 | GAP-01 to GAP-02 |
| The PRD conflicts with the live page | 7 | GAP-03 to GAP-09 |
| Not specified anywhere (open questions) | 15 | GAP-10 to GAP-24 |
| On the page but missing from the PRD | 3 | GAP-25 to GAP-27 |
| PRD document quality | 3 | GAP-28 to GAP-30 |

### Blocks execution

| ID | Gap | Affects |
|---|---|---|
| GAP-01 | No registered VWO test account (email and password) was provided | TC-002, TC-003, TC-009 (not executed) |
| GAP-02 | The PRD does not specify the post-login landing page: no dashboard URL and no element that confirms a successful login | TC-002, TC-003 |

### The PRD conflicts with the live page

| ID | PRD says | Live page shows | Affects |
|---|---|---|---|
| GAP-03 | Real-time validation on blur | No field-level message on blur (OBS-09) | TC-007 |
| GAP-04 | Automatic email format validation | Malformed emails are submitted and get the generic message (OBS-06) | TC-007 |
| GAP-05 | "Clear, actionable" error messages | One generic message for every failure type (OBS-06) | TC-004 to TC-008 |
| GAP-06 | Social login with Google, Microsoft and others | Only Google, plus Passkey (OBS-02) | (none of the 10) |
| GAP-07 | A Light/Dark Mode announcement banner | Not found (OBS-14) | (none of the 10) |
| GAP-08 | VWO branding | Title and consent text say "Wingify" (OBS-01, OBS-02) | TC-001 |
| GAP-09 | Password strength indicators | None on the login form (OBS-14) | (none of the 10) |

### Not specified anywhere (marked "Insufficient information to determine.")

| ID | Unknown | Affects |
|---|---|---|
| GAP-10 | The error text for a registered email with a wrong password (an inference is used) | TC-009 |
| GAP-11 | The rate-limit or lockout threshold, lock duration and message | TC-009 (attempts limited) |
| GAP-12 | What "Remember me" keeps and for how long. The PRD describes it both as "persistent login sessions" and as "remembered credentials". | TC-003 |
| GAP-13 | Session timeout values ("configurable timeout periods") | (none of the 10) |
| GAP-14 | The 2FA flow: how it is enabled, OTP channel, OTP length and expiry, error texts | Preconditions of TC-002, TC-003 |
| GAP-15 | The SSO flow after the email lookup, and the supported protocols per account | (none of the 10) |
| GAP-16 | The intended Forgot Password responses (for example, whether a malformed email should show both messages), reset-token expiry and the content of the reset email | TC-010 |
| GAP-17 | Field rules: maximum length, whitespace trimming, whether the email is case-sensitive | (none of the 10) |
| GAP-18 | Password complexity rules (no values given) | (none of the 10) |
| GAP-19 | How the loading state should look ("clear feedback") | (none of the 10) |
| GAP-20 | Supported browser, OS and device matrix, and responsive breakpoints | Environment column |
| GAP-21 | The definition of a "standard connection" and how to measure the 2-second load target | (none of the 10) |
| GAP-22 | Accessibility details: how High Contrast Mode is turned on, and whether errors must be announced to screen readers. The error container has no `aria-live`, so screen readers may not announce it: Inference (low confidence). | (none of the 10) |
| GAP-23 | Only the production URL was given, so there is no safe environment for lockout, security or load tests | TC-009 |
| GAP-24 | Which host is canonical. app.vwo.com and app.wingify.com serve the same app with small text differences. | Environment column |

### On the page but missing from the PRD

| ID | Item | Affects |
|---|---|---|
| GAP-25 | "Sign in with Passkey": the PRD defines no scope or behaviour for it | TC-001 (presence only) |
| GAP-26 | The password visibility eye icon | TC-001 |
| GAP-27 | The Privacy policy and Terms links and the consent text: their destinations are not specified | DEF-01 |

### PRD document quality

| ID | Issue |
|---|---|
| GAP-28 | There are no requirement IDs or acceptance criteria, so traceability can only use section names. There is also no version, owner or date. |
| GAP-29 | Stray citation numbers are glued to sentences (for example "secure validation56" and "immediate feedback7") with no reference list. |
| GAP-30 | Several statements can't be measured, such as "intuitive", "professional, trustworthy appearance" and "90%+ user satisfaction". |

### Candidate defects found during verification

| ID | Severity (proposed) | Finding | Evidence |
|---|---|---|---|
| DEF-01 | High | On app.vwo.com, the "Start a free trial", "Privacy policy" and "Terms" links point to addresses containing an unfilled `{{$root.VWO_DOMAIN...}}` placeholder. This breaks the PRD's free-trial registration link. On app.wingify.com, Privacy policy and Terms are broken the same way, and clicking Privacy policy opened a browser error page. | OBS-11 |
| DEF-02 | Medium | Keyboard users cannot reach or toggle "Remember me". This conflicts with PRD *Keyboard Navigation* and *WCAG 2.1 AA*. | OBS-12 |

---

## Generated Output

[VWO_Login_TestCases_Jira.csv](VWO_Login_TestCases_Jira.csv) contains 10 test cases (3 positive, 7 negative) and 18 columns. The file is ASCII-only and RFC 4180 quoted, with multi-line cells.

| ID | Summary | Type | Priority | Status |
|---|---|---|---|---|
| VWO-LOGIN-TC-001 | Login page loads over HTTPS with all controls and correct defaults | Positive | High | Verified |
| VWO-LOGIN-TC-002 | Successful login with a valid registered email and password | Positive | Highest | Not executed (GAP-01) |
| VWO-LOGIN-TC-003 | Successful login with "Remember me" selected, submitted with Enter | Positive | High | Partly verified (GAP-01) |
| VWO-LOGIN-TC-004 | Both fields empty | Negative | High | Verified |
| VWO-LOGIN-TC-005 | Email empty, password entered | Negative | High | Verified |
| VWO-LOGIN-TC-006 | Valid-format email, password empty | Negative | High | Verified |
| VWO-LOGIN-TC-007 | Five invalid email formats, plus the PRD's on-blur check | Negative | High | Verified (step 3 deviates from the PRD) |
| VWO-LOGIN-TC-008 | Unregistered email with a wrong password: message and field state | Negative | Highest | Verified |
| VWO-LOGIN-TC-009 | Registered email with a wrong password | Negative | Highest | Not executed (GAP-01) |
| VWO-LOGIN-TC-010 | Forgot Password with empty, malformed and unregistered email | Negative | High | Verified |

**Columns:** Test Case ID, Summary, Issue Type, Priority, Component/s, Labels, Test Type, Requirement Reference, Description, Preconditions, Test Steps, Test Data, Expected Result, Evidence Source, Verification Status, Automation Candidate, Automation Reference, Environment.

**Jira import mapping:**
1. Use *Jira Settings > System > External System Import > CSV*, or your Xray or Zephyr test importer.
2. Map these columns to the Jira fields of the same name: *Summary*, *Issue Type*, *Priority*, *Component/s*, *Labels* and *Description*.
3. Map the remaining columns to custom fields. If your project has no such fields, append them to Description.
4. `Issue Type = Test` needs Xray or Zephyr. Without either, change it to `Task` before importing.
5. The `Login` component must already exist, or you must let the importer create it.

---

## Self-Validation Check

| Check | Result |
|---|---|
| Every expected result traces to a PRD item (P-xx) or a live observation (OBS-xx) | Pass. All 10 rows cite PRD or OBS in *Evidence Source*. |
| No invented UI text or error messages | Pass. All 16 quoted labels and messages in the CSV were matched, character for character, against the raw observation logs. |
| Unknowns stated as "Insufficient information to determine." | Pass. It is used 5 times (GAP-02 twice, GAP-03/04, GAP-12, GAP-16). |
| Inferences labelled "Inference (low confidence)" | Pass. There is 1 inference: the TC-009 message text (GAP-10). |
| No assumed "typical" behaviour | Pass. Behaviour the PRD claims but the page doesn't show is reported as a conflict (GAP-03 to GAP-09), not written as a passing expectation. |
| PRD conflicts not hidden | Pass. TC-007 step 3 keeps the PRD expectation and flags the observed deviation. |
| Deterministic and repeatable | Pass. IDs, data, step order and wording are fixed. Data rows run in a stated order on a freshly opened page. |
| Unique IDs, positive and negative coverage | Pass. VWO-LOGIN-TC-001 to TC-010 are unique: 3 positive and 7 negative. They cover valid login, empty fields, invalid email formats and error handling. |
| No real credentials or personal data | Pass. Valid credentials are vault placeholders (`<VALID_USERNAME>`, `<VALID_PASSWORD>`). Synthetic emails use `example.com`. |
| No unsafe testing of production | Pass. There are no brute-force or lockout steps, and TC-009 limits attempts (GAP-11). |
| CSV well-formed | Pass. 10 records with 18 fields each, confirmed by an independent parser. |
