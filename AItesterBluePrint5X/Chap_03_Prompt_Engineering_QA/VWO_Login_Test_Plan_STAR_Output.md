# Functional Test Plan – VWO Login Module

| Field | Value |
|---|---|
| **Test Plan ID** | TP-VWO-LOGIN-001 |
| **Title** | Functional Test Plan – VWO Login Module |
| **Application URL** | https://app.vwo.com/ |
| **Version** | 0.1 (Draft for review) |
| **Date** | 26-Nov-2026 |
| **Prepared by** | Sailaxmi Sahu, QA |
| **Source prompt** | [STAR.md](STAR.md) |
| **Status** | Draft – pending review |

> **Labels used in this document**
> - **Requires clarification** – the approved requirements do not define this behavior. No expected result has been assumed.
> - **Recommendation** – a proposed criterion or practice. It is not an approved requirement.
> - **Inference** – drawn from the wording of a requirement. Confirm it before relying on it.

---

## Requirements Baseline

These are the only approved requirements for this plan. The REQ IDs were added here for traceability.

| Req ID | Approved Requirement |
|---|---|
| REQ-01 | Registered users can log in with valid credentials. |
| REQ-02 | Invalid credentials must not grant access. |
| REQ-03 | Email and password are mandatory. |
| REQ-04 | Successful login opens the authenticated dashboard. |

---

## 1. Objective

Verify that the login module at https://app.vwo.com/ meets REQ-01 to REQ-04:

- A registered user with valid credentials can log in and reach the authenticated dashboard.
- Access is not granted when credentials are invalid or when a mandatory field is empty.
- Any behavior the requirements do not define (messages, password rules, session handling) is identified and logged for clarification before sign-off.

---

## 2. Scope

### 2.1 In Scope

| Area | Requirement |
|---|---|
| Login with the valid credentials of a registered user | REQ-01 |
| Rejection of invalid credentials: wrong password, unregistered email, mismatched email/password pair, malformed email | REQ-02 |
| Mandatory field checks: empty email, empty password, both empty | REQ-03 |
| Empty (0 characters) vs. non-empty (1 character) boundary of each mandatory field | REQ-03 |
| Dashboard opens after a successful login | REQ-04 |
| Dashboard is not reachable after a failed login attempt | REQ-02 |

### 2.2 Out of Scope

| Area | Reason |
|---|---|
| Registration / sign-up | Not in the supplied requirements |
| Forgot password / password reset | Not in the supplied requirements |
| Logout | Not in the supplied requirements |
| Password rules (length, complexity, allowed characters) | Not defined – **Requires clarification** (Q-03) |
| Exact error and validation message text | Not defined – **Requires clarification** (Q-01, Q-02) |
| Session timeout, "remember me", session persistence | Not defined – **Requires clarification** (Q-06) |
| Account lockout, rate limiting, CAPTCHA | Not defined – **Requires clarification** (Q-05) |
| Other sign-in methods (e.g., SSO, social login) and MFA, if the page offers them | Not in the supplied requirements (Q-11) |
| Dashboard features beyond confirming that it opens | Belongs to a separate module |
| Performance, load, security/penetration, accessibility, localization | Non-functional; needs separate plans |
| API-level testing | This plan covers the UI only |

> Items marked **Requires clarification** can move into scope once the behavior is defined.

---

## 3. Test Approach

| Aspect | Approach |
|---|---|
| Test type | Functional, black-box testing through the UI |
| Design techniques | Requirement-based testing; equivalence partitioning (valid, invalid, and empty input classes); boundary value analysis limited to the empty (0 characters) vs. non-empty (1 character) boundary, because no length limits are defined |
| Scenario categories | Positive, Negative, Boundary |
| Common precondition | Tester is on the login page at https://app.vwo.com/ in a fresh browser session with no active login |
| Execution | Manual execution for the first cycle. **Recommendation:** after expected results are confirmed, automate the stable scenarios (TS-01, TS-05, TS-06, TS-10 to TS-12) for regression. |
| Traceability | Every scenario maps to at least one REQ ID (Section 4.6) |
| Handling undefined behavior | For scenarios with an undefined part, testers verify only the outcome the requirements define (for example, "access is not granted"). They record what they observe for the undefined part (messages, UI state) in the clarification log. That observation does not become the expected result until it is confirmed. |
| Defect logging | **Recommendation:** each defect includes steps, test data ID, expected vs. actual result, screenshot, and browser/version. |
| Defect severity | **Recommendation:** Critical – a valid user cannot log in, or invalid credentials grant access. High – a mandatory-field check fails. Medium / Low – other functional or cosmetic issues. |

---

## 4. Test Scenarios

**Status legend**

| Status | Meaning |
|---|---|
| Ready | Expected result is fully defined by the requirements. |
| Ready – partial | The requirement-backed outcome can be verified. Part of the behavior (e.g., message text) **Requires clarification** and will be recorded as observed. |
| Requires clarification | The expected result is not defined. Blocked until the linked question is answered. |

### 4.1 Valid Login

| ID | Scenario | Type | Req | Test Data | Expected Result | Status |
|---|---|---|---|---|---|---|
| TS-01 | Log in with a registered email and its correct password | Positive | REQ-01, REQ-04 | TD-01 | Login succeeds and the authenticated dashboard opens. The element or URL that identifies the dashboard **Requires clarification** (Q-10). | Ready |
| TS-02 | Log in with a second registered account | Positive | REQ-01, REQ-04 | TD-02 | Login succeeds and the authenticated dashboard opens. | Ready |
| TS-03 | Log in with the registered email typed in a different letter case (e.g., all uppercase) and the correct password | Positive or Negative (TBD) | REQ-01 | TD-08 (email), TD-01 (password) | **Requires clarification** – email case sensitivity is not defined (Q-04). | Requires clarification |
| TS-04 | Log in with leading/trailing spaces around the registered email and the correct password | Positive or Negative (TBD) | REQ-01 | TD-09, TD-01 (password) | **Requires clarification** – whether spaces are trimmed is not defined (Q-04). | Requires clarification |

### 4.2 Invalid Credentials

| ID | Scenario | Type | Req | Test Data | Expected Result | Status |
|---|---|---|---|---|---|---|
| TS-05 | Registered email with an incorrect password | Negative | REQ-02 | TD-01 (email), TD-04 | Access is not granted and the dashboard does not open. Message text **Requires clarification** (Q-01). | Ready – partial |
| TS-06 | Unregistered email with any password | Negative | REQ-02 | TD-03, TD-04 | Access is not granted and the dashboard does not open. Message text **Requires clarification** (Q-01, Q-12). | Ready – partial |
| TS-07 | Registered email with the password of a different registered account | Negative | REQ-02 | TD-01 (email), TD-02 (password) | Access is not granted and the dashboard does not open. | Ready |
| TS-08 | Registered email with the correct password typed in a different letter case | Negative (TBD) | REQ-02 | TD-01 (email), TD-08 (password) | **Requires clarification** – password case sensitivity is not defined (Q-03). | Requires clarification |
| TS-09 | Malformed email (no "@", no domain, no local part) with the account's correct password | Negative | REQ-02 | TD-05, TD-01 (password) | Access is not granted. Whether a format validation message appears **Requires clarification** (Q-04). | Ready – partial |

### 4.3 Empty Fields

| ID | Scenario | Type | Req | Test Data | Expected Result | Status |
|---|---|---|---|---|---|---|
| TS-10 | Email empty, valid password entered | Negative | REQ-03 | TD-01 (password) | Login is not completed and access is not granted. How the mandatory field is indicated **Requires clarification** (Q-02). | Ready – partial |
| TS-11 | Password empty, registered email entered | Negative | REQ-03 | TD-01 (email) | Login is not completed and access is not granted. Indication **Requires clarification** (Q-02). | Ready – partial |
| TS-12 | Both email and password empty | Negative | REQ-03 | None | Login is not completed and access is not granted. Indication **Requires clarification** (Q-02). | Ready – partial |
| TS-13 | Email contains only spaces, valid password entered | Negative | REQ-03, REQ-02 | TD-06, TD-01 (password) | Access is not granted. Whether spaces-only counts as "empty" **Requires clarification** (Q-02, Q-04). | Ready – partial |
| TS-14 | Password contains only spaces, registered email entered | Negative | REQ-03, REQ-02 | TD-01 (email), TD-06 | Access is not granted, because spaces-only is not the account's password. Whether it counts as "empty" **Requires clarification** (Q-02). | Ready – partial |

### 4.4 Boundary

| ID | Scenario | Type | Req | Test Data | Expected Result | Status |
|---|---|---|---|---|---|---|
| TS-15 | Email with exactly 1 character (just above empty), valid password | Boundary | REQ-03, REQ-02 | TD-07, TD-01 (password) | The field is not empty, but the credentials are invalid, so access is not granted. | Ready |
| TS-16 | Password with exactly 1 character (just above empty), registered email | Boundary | REQ-03, REQ-02 | TD-01 (email), TD-07 | The field is not empty, but the credentials are invalid, so access is not granted. | Ready |
| TS-17 | Type the email, clear it back to 0 characters, enter a valid password, then submit | Boundary | REQ-03 | TD-01 | The email field is treated as empty; login is not completed and access is not granted. | Ready |
| TS-18 | Minimum and maximum length of email and password | Boundary | None defined | None | **Requires clarification** – no length limits are defined (Q-03, Q-04). | Requires clarification |

### 4.5 Dashboard Access

| ID | Scenario | Type | Req | Test Data | Expected Result | Status |
|---|---|---|---|---|---|---|
| TS-19 | After a failed login (as in TS-05), open the dashboard URL directly | Negative | REQ-02 | TD-01 (email), TD-04; dashboard URL noted during TS-01 | The dashboard is not displayed and access is not granted. Redirect target or message **Requires clarification** (Q-07). | Ready – partial |
| TS-20 | Without logging in, open the dashboard URL in a fresh browser session | Negative | REQ-04 (Inference) | Dashboard URL noted during TS-01 | **Inference:** "authenticated dashboard" suggests a login is required, so the dashboard should not be displayed. Exact behavior **Requires clarification** (Q-07). | Requires clarification |
| TS-21 | One failed login attempt, then log in with the correct credentials | Positive | REQ-01, REQ-04 | TD-04, then TD-01 | Login succeeds and the authenticated dashboard opens. Only one failed attempt is used, because the lockout policy **Requires clarification** (Q-05). | Ready |

### 4.6 Traceability Matrix

| Req ID | Scenarios |
|---|---|
| REQ-01 | TS-01, TS-02, TS-03, TS-04, TS-21 |
| REQ-02 | TS-05, TS-06, TS-07, TS-08, TS-09, TS-13, TS-14, TS-15, TS-16, TS-19 |
| REQ-03 | TS-10, TS-11, TS-12, TS-13, TS-14, TS-15, TS-16, TS-17 |
| REQ-04 | TS-01, TS-02, TS-20 (Inference), TS-21 |
| None | TS-18 – no requirement defines length limits |

### 4.7 Scenario Summary

| Category | Count | Status | Count |
|---|---|---|---|
| Positive | 3 | Ready | 7 |
| Negative | 12 | Ready – partial | 9 |
| Boundary | 4 | Requires clarification | 5 |
| Positive or Negative (TBD) | 2 | | |
| **Total** | **21** | **Total** | **21** |

---

## 5. Environment and Test Data

### 5.1 Test Environment

| Item | Detail | Status |
|---|---|---|
| Application URL | https://app.vwo.com/ | Given |
| Environment type (production, staging, QA) | Not stated | **Requires clarification** (Q-08) |
| Browsers and versions | **Recommendation:** latest stable Google Chrome as the baseline for cycle 1 | Supported list **Requires clarification** (Q-09) |
| Operating system / devices | **Recommendation:** one desktop OS for cycle 1 | Supported list **Requires clarification** (Q-09) |
| Browser state | **Recommendation:** use a private/incognito window, or clear cookies and cache, before each scenario so an earlier session does not affect results | Recommendation |
| Tools | **Recommendation:** a defect tracker (e.g., Jira), a test management sheet, and a screenshot tool | Recommendation |
| Network | Stable internet access to the application URL | Required |

### 5.2 Test Data

> **Recommendation:** do not write real passwords in this plan, in test reports, or in screenshots. Keep credentials in a secure vault and refer to them only by Data ID. Use dedicated test accounts, never personal or customer accounts.

| Data ID | Description | Example / Source | Used In |
|---|---|---|---|
| TD-01 | Registered account A: email and correct password | From the account owner/admin; stored in the vault | TS-01, TS-03, TS-04, TS-05, TS-07, TS-08, TS-09, TS-10, TS-11, TS-13, TS-14, TS-15, TS-16, TS-17, TS-19, TS-21 |
| TD-02 | Registered account B: email and correct password | From the account owner/admin; stored in the vault | TS-02, TS-07 |
| TD-03 | Unregistered email in a valid format | e.g., `not.registered.qa@example.com` (confirm it is not registered) | TS-06 |
| TD-04 | Incorrect password (does not match account A) | e.g., `Wrong_Pass_001` | TS-05, TS-06, TS-19, TS-21 |
| TD-05 | Malformed email values | `userexample.com` (no "@"), `user@` (no domain), `@example.com` (no local part) | TS-09 |
| TD-06 | Spaces-only value | 3 space characters | TS-13, TS-14 |
| TD-07 | Single-character value | `a` | TS-15, TS-16 |
| TD-08 | Case-altered values of account A | Email in all uppercase; password with letter case swapped | TS-03, TS-08 |
| TD-09 | Account A email with leading and trailing spaces | `"  <TD-01 email>  "` | TS-04 |

---

## 6. Entry and Exit Criteria

> All criteria in this section are **Recommendations** for the team to accept or change.

### 6.1 Entry Criteria

| ID | Criterion |
|---|---|
| EN-01 | Requirements REQ-01 to REQ-04 are approved and baselined. |
| EN-02 | The test environment is identified (Q-08) and reachable. |
| EN-03 | Test accounts TD-01 and TD-02 are created and confirmed working. |
| EN-04 | This test plan and its scenarios are reviewed and approved. |
| EN-05 | Open questions have been reviewed with the product owner. Unanswered items are accepted as known gaps. |
| EN-06 | The defect tracker is set up and accessible to the team. |

### 6.2 Exit Criteria

| ID | Criterion |
|---|---|
| EX-01 | All 16 scenarios with status "Ready" or "Ready – partial" have been executed. |
| EX-02 | Each of REQ-01 to REQ-04 has at least one executed, passed scenario. |
| EX-03 | No Critical or High defects remain open. Open Medium/Low defects are accepted by the product owner. |
| EX-04 | Each "Requires clarification" item is either resolved (and its scenario executed) or recorded as deferred with an owner. |
| EX-05 | Observed behavior for all "Ready – partial" scenarios is recorded in the clarification log. |
| EX-06 | The test summary report is reviewed and signed off. |

### 6.3 Suspension and Resumption

| Type | Criterion |
|---|---|
| Suspend | Test accounts become locked or unusable, the environment is unavailable, or a Critical defect blocks login entirely. |
| Resume | The blocking issue is fixed or a workaround is agreed, and affected scenarios are re-run. |

---

## 7. Deliverables

| Deliverable | Description | When |
|---|---|---|
| Test plan | This document | Before execution |
| Test cases | Detailed steps for each scenario (TS-01 to TS-21) | Before execution |
| Traceability matrix | Requirement-to-scenario mapping (Section 4.6), updated with results | Before and after execution |
| Test data sheet | Data IDs and descriptions; credentials kept in the vault | Before execution |
| Clarification log | Open questions, answers, and observed behavior | Throughout |
| Defect reports | One record per defect in the tracker | During execution |
| Execution status report | Pass / fail / blocked count per scenario | During execution |
| Test summary report | Results, open defects, deferred items, sign-off | After execution |

---

## 8. Risks and Open Questions

### 8.1 Risks

| ID | Risk | Impact | Mitigation (Recommendation) |
|---|---|---|---|
| R-01 | The requirements are minimal. Messages, password rules, and session behavior are not defined. | Several expected results cannot be confirmed, and defects may be disputed. | Resolve Q-01 to Q-14 before execution. Treat observed behavior as evidence only. |
| R-02 | Only a URL is given; the environment may be production. | Repeated failed logins could affect real accounts or trigger protective controls whose behavior is unknown. | Confirm the environment (Q-08). Use dedicated test accounts only. |
| R-03 | The lockout / rate-limit policy is unknown (Q-05). | Test accounts may get locked during negative tests and block execution. | Keep a second test account and an unlock contact. Spread negative tests out over time. |
| R-04 | Test accounts are not available, or are shared with other testers. | Positive scenarios are blocked, or results conflict. | Provision dedicated accounts before entry (EN-03). |
| R-05 | No agreed indicator of the "authenticated dashboard" (Q-10). | Pass/fail for TS-01, TS-02, and TS-21 becomes subjective. | Agree on an identifying element or URL before execution. |
| R-06 | The page may have extra sign-in controls (e.g., CAPTCHA, MFA) not covered by the requirements (Q-11). | Manual or automated runs may be blocked. | Confirm with the product owner and adjust scope. |
| R-07 | Requirements change during testing. | Rework of scenarios and results. | Baseline the requirements and use change control. |

### 8.2 Open Questions

Fill in the **Owner** and **Answer** columns during review.

| ID | Question | Affects | Owner | Answer |
|---|---|---|---|---|
| Q-01 | What exact error message appears for invalid credentials, and where is it shown? | TS-05, TS-06, TS-09, TS-19 | TBD | |
| Q-02 | How are empty mandatory fields handled (inline message, disabled login button, or message after submit)? What is the text? Do spaces-only values count as empty? | TS-10 to TS-14, TS-17 | TBD | |
| Q-03 | What are the password rules: minimum/maximum length, allowed characters, case sensitivity? | TS-08, TS-16, TS-18 | TBD | |
| Q-04 | What are the email rules: maximum length, format validation, case sensitivity, trimming of spaces? | TS-03, TS-04, TS-09, TS-13, TS-15, TS-18 | TBD | |
| Q-05 | Is there account lockout, rate limiting, or CAPTCHA after failed attempts? If so, after how many attempts? | TS-21, R-02, R-03 | TBD | |
| Q-06 | What are the session timeout and session persistence rules (page refresh, new tab, browser restart, "remember me")? | Out of scope until defined | TBD | |
| Q-07 | What happens when a user who is not logged in opens the dashboard URL directly (redirect target, message)? | TS-19, TS-20 | TBD | |
| Q-08 | Which environment should be tested (production, staging, QA), and who provides the test accounts? | EN-02, EN-03, R-02 | TBD | |
| Q-09 | Which browsers, operating systems, and devices are supported? | Section 5.1 | TBD | |
| Q-10 | What identifies the authenticated dashboard (URL, page title, or a specific element)? | TS-01, TS-02, TS-21 | TBD | |
| Q-11 | Does the login page include other sign-in methods, MFA, or CAPTCHA? Are they in scope? | Section 2, R-06 | TBD | |
| Q-12 | Should the message be the same for a wrong password and an unregistered email, so it does not reveal which emails are registered? | TS-05, TS-06 | TBD | |
| Q-13 | What is the expected behavior for registered accounts that are inactive, suspended, or unverified? | REQ-01 coverage | TBD | |
| Q-14 | Can the login form be submitted with the Enter key as well as the login button? | All scenarios | TBD | |

---

## Review and Sign-off

| Role | Name | Decision | Date |
|---|---|---|---|
| QA Lead | | | |
| Product Owner | | | |
| Development Lead | | | |

## Revision History

| Version | Date | Author | Change |
|---|---|---|---|
| 0.1 | 26-Nov-2026 | Sailaxmi Sahu | Initial draft generated from the STAR prompt |
