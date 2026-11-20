package com.salesforce.tests;

import com.salesforce.base.BaseTest;
import com.salesforce.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Salesforce CRM")
@Feature("Login")
@Story("Valid login scenarios")
public class ValidLoginTest extends BaseTest {

    private static final String EMAIL_VIEW_TITLE = "Log In with Email | Salesforce";
    private static final String USERNAME_VIEW_TITLE = "Login | Salesforce";
    private static final String USERNAME_VIEW_URL_FRAGMENT = "login=1";
    private static final String WELL_FORMED_USERNAME = "qa.unregistered.user.rp5x@example.com";
    private static final String USERNAME_KEY = "VALID_USERNAME";
    private static final String PASSWORD_KEY = "VALID_PASSWORD";

    @Test(priority = 1, description = "Email login view renders all required controls")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifies title, heading, email field, Continue button, Remember Me and the 'Log In with Username' link on login.salesforce.com/?locale=in")
    public void verifyEmailLoginViewElements() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView();
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(loginPage.getPageTitle(), EMAIL_VIEW_TITLE, "Page title");
            softAssert.assertTrue(loginPage.isEmailLoginHeadingDisplayed(), "'Log In with Email' heading displayed");
            softAssert.assertTrue(loginPage.isEmailInputDisplayed(), "Email input displayed");
            softAssert.assertEquals(loginPage.getEmailInputType(), "email", "Email input type");
            softAssert.assertTrue(loginPage.isContinueButtonEnabled(), "Continue button enabled");
            softAssert.assertTrue(loginPage.isLogInWithUsernameLinkDisplayed(), "'Log In with Username' link displayed");
            softAssert.assertAll();
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Email login view verification failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 2, description = "Remember Me on the email view toggles on each click")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicks Remember Me twice on the email view and verifies the checkbox state inverts and then restores")
    public void verifyEmailRememberMeToggle() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView();
            boolean initialState = loginPage.isEmailRememberMeSelected();
            loginPage.toggleEmailRememberMe();
            Assert.assertEquals(loginPage.isEmailRememberMeSelected(), !initialState, "Remember Me state after first toggle");
            loginPage.toggleEmailRememberMe();
            Assert.assertEquals(loginPage.isEmailRememberMeSelected(), initialState, "Remember Me state after second toggle");
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Email view Remember Me toggle failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 3, description = "'Log In with Username' opens the username login view")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Navigates from the email view to the username view and verifies title, URL, username field, Log In button, Remember me and Forgot Your Password link")
    public void verifyUsernameLoginViewElements() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().openUsernameLogin();
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(loginPage.getPageTitle(), USERNAME_VIEW_TITLE, "Page title");
            softAssert.assertTrue(loginPage.getCurrentUrl().contains(USERNAME_VIEW_URL_FRAGMENT), "URL contains " + USERNAME_VIEW_URL_FRAGMENT);
            softAssert.assertTrue(loginPage.isUsernameInputDisplayed(), "Username input displayed");
            softAssert.assertTrue(loginPage.isLogInButtonDisplayed(), "Log In button displayed");
            softAssert.assertTrue(loginPage.isUsernameRememberMeDisplayed(), "Remember me checkbox displayed");
            softAssert.assertTrue(loginPage.isForgotPasswordLinkDisplayed(), "'Forgot Your Password?' link displayed");
            softAssert.assertAll();
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Username login view verification failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 4, description = "Remember me on the username view toggles on each click")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clicks Remember me twice on the username view and verifies the checkbox state inverts and then restores")
    public void verifyUsernameRememberMeToggle() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().openUsernameLogin();
            boolean initialState = loginPage.isUsernameRememberMeSelected();
            loginPage.toggleUsernameRememberMe();
            Assert.assertEquals(loginPage.isUsernameRememberMeSelected(), !initialState, "Remember me state after first toggle");
            loginPage.toggleUsernameRememberMe();
            Assert.assertEquals(loginPage.isUsernameRememberMeSelected(), initialState, "Remember me state after second toggle");
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Username view Remember me toggle failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 5, description = "Submitting a username reveals a masked password field")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Submits a well-formed username and verifies the password field becomes visible, is of type 'password' and the submitted username is shown on the identity card")
    public void verifyPasswordFieldIsMaskedAfterUsernameSubmission() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().openUsernameLogin()
                    .submitUsername(WELL_FORMED_USERNAME);
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertTrue(loginPage.isPasswordInputDisplayed(), "Password input displayed");
            softAssert.assertEquals(loginPage.getPasswordInputType(), "password", "Password input type");
            softAssert.assertEquals(loginPage.getSubmittedUsername(), WELL_FORMED_USERNAME, "Submitted username shown on identity card");
            softAssert.assertAll();
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Password field verification failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 6, description = "Valid credentials authenticate and dismiss the login form")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Logs in with VALID_USERNAME/VALID_PASSWORD resolved from -D system properties, OS environment variables or the .env file and verifies the login form is dismissed without an error")
    public void verifyValidLogin() {
        String username = resolveSetting(USERNAME_KEY);
        String password = resolveSetting(PASSWORD_KEY);
        if (username.isEmpty() || password.isEmpty()) {
            throw new SkipException("Insufficient information to determine. Supply " + USERNAME_KEY + " and "
                    + PASSWORD_KEY + " in .env, as environment variables or as -D system properties.");
        }
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().openUsernameLogin()
                    .loginWithUsername(username, password);
            Assert.assertTrue(loginPage.isLoginFormDismissed(), "Login form dismissed after submitting valid credentials");
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Valid login failed: " + e.getMessage(), e);
        }
    }
}
