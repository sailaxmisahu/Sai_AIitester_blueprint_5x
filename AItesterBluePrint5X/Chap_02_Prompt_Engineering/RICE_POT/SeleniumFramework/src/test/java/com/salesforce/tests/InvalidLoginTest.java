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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Salesforce CRM")
@Feature("Login")
@Story("Invalid login scenarios")
public class InvalidLoginTest extends BaseTest {

    private static final String INVALID_EMAIL_MESSAGE = "Enter a valid email address.";
    private static final String UNKNOWN_EMAIL_MESSAGE = "We couldn't find your login details.";
    private static final String BLANK_USERNAME_MESSAGE = "Error: Please enter your username.";
    private static final String BLANK_PASSWORD_MESSAGE = "Error: Please enter your password.";
    private static final String INVALID_CREDENTIALS_MESSAGE =
            "Error: Please check your username and password. If you still can't log in, contact your Salesforce administrator.";
    private static final String UNREGISTERED_USERNAME = "qa.unregistered.user.rp5x@example.com";
    private static final String INVALID_PASSWORD = "Invalid@Pass#2026";

    @DataProvider(name = "invalidEmails")
    public Object[][] invalidEmails() {
        return new Object[][]{
                {"Blank email", "", INVALID_EMAIL_MESSAGE},
                {"Malformed email", "not-an-email", INVALID_EMAIL_MESSAGE},
                {"Unregistered email", UNREGISTERED_USERNAME, UNKNOWN_EMAIL_MESSAGE}
        };
    }

    @Test(priority = 1, dataProvider = "invalidEmails", description = "Email view rejects invalid email input")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Submits blank, malformed and unregistered email values on the email view and verifies the inline validation message")
    public void verifyInvalidEmailIsRejected(String scenario, String email, String expectedMessage) {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().continueWithEmail(email);
            Assert.assertEquals(loginPage.getEmailErrorMessage(), expectedMessage, scenario + " validation message");
            Assert.assertTrue(loginPage.isEmailInputDisplayed(), scenario + " keeps the user on the email view");
        } catch (LoginPage.LoginPageException e) {
            Assert.fail(scenario + " verification failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 2, description = "Username view rejects a blank username")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicks Log In on the username view without a username and verifies the error message")
    public void verifyBlankUsernameIsRejected() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().openUsernameLogin().clickLogIn();
            Assert.assertEquals(loginPage.getUsernameErrorMessage(), BLANK_USERNAME_MESSAGE, "Blank username error message");
            Assert.assertTrue(loginPage.isUsernameInputDisplayed(), "Username input still displayed");
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Blank username verification failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 3, description = "Username view rejects a blank password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Submits a username, leaves the password blank, clicks Log In and verifies the error message")
    public void verifyBlankPasswordIsRejected() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().openUsernameLogin()
                    .submitUsername(UNREGISTERED_USERNAME).clickLogIn();
            Assert.assertEquals(loginPage.getUsernameErrorMessage(), BLANK_PASSWORD_MESSAGE, "Blank password error message");
            Assert.assertTrue(loginPage.isLogInButtonDisplayed(), "Log In button still displayed");
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Blank password verification failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 4, description = "Username view rejects invalid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Logs in with an unregistered username and an invalid password and verifies the authentication error message")
    public void verifyInvalidCredentialsAreRejected() {
        try {
            LoginPage loginPage = new LoginPage(getDriver()).waitForEmailLoginView().openUsernameLogin()
                    .loginWithUsername(UNREGISTERED_USERNAME, INVALID_PASSWORD);
            Assert.assertEquals(loginPage.getUsernameErrorMessage(), INVALID_CREDENTIALS_MESSAGE, "Invalid credentials error message");
            Assert.assertTrue(loginPage.isLogInButtonDisplayed(), "Log In button still displayed");
        } catch (LoginPage.LoginPageException e) {
            Assert.fail("Invalid credentials verification failed: " + e.getMessage(), e);
        }
    }
}
