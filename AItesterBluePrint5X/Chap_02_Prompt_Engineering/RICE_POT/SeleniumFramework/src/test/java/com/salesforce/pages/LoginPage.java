package com.salesforce.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(25);

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//h1[normalize-space()='Log In with Email']")
    private WebElement emailLoginHeading;

    @FindBy(xpath = "//input[@name='identifier']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@name='rememberMe']")
    private WebElement emailRememberMeCheckbox;

    @FindBy(xpath = "//label[.//span[normalize-space()='Remember Me']]")
    private WebElement emailRememberMeLabel;

    @FindBy(xpath = "//button[normalize-space()='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//div[@input-error and normalize-space()]")
    private WebElement emailErrorMessage;

    @FindBy(xpath = "//a[normalize-space()='Log In with Username']")
    private WebElement logInWithUsernameLink;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameInput;

    @FindBy(xpath = "//span[@id='idcard-identity']")
    private WebElement submittedUsernameCard;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='Login']")
    private WebElement logInButton;

    @FindBy(xpath = "//input[@id='rememberUn']")
    private WebElement usernameRememberMeCheckbox;

    @FindBy(xpath = "//a[@id='forgot_password_link']")
    private WebElement forgotPasswordLink;

    @FindBy(xpath = "//div[@id='error' and normalize-space()]")
    private WebElement usernameErrorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        this.wait.ignoring(StaleElementReferenceException.class);
        PageFactory.initElements(driver, this);
    }

    @Step("Wait for the 'Log In with Email' view to load")
    public LoginPage waitForEmailLoginView() {
        waitForVisible(emailInput, "Email input");
        waitForClickable(continueButton, "Continue button");
        return this;
    }

    public String getPageTitle() {
        try {
            return driver.getTitle();
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to read the page title", e);
        }
    }

    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to read the current URL", e);
        }
    }

    public boolean isEmailLoginHeadingDisplayed() {
        return isVisible(emailLoginHeading);
    }

    public boolean isEmailInputDisplayed() {
        return isVisible(emailInput);
    }

    public String getEmailInputType() {
        return readDomAttribute(emailInput, "type", "Email input");
    }

    public boolean isContinueButtonEnabled() {
        return isVisible(continueButton) && isEnabled(continueButton, "Continue button");
    }

    public boolean isLogInWithUsernameLinkDisplayed() {
        return isVisible(logInWithUsernameLink);
    }

    public boolean isEmailRememberMeSelected() {
        return isSelected(emailRememberMeCheckbox, "Email view 'Remember Me' checkbox");
    }

    @Step("Toggle 'Remember Me' on the email view")
    public LoginPage toggleEmailRememberMe() {
        click(emailRememberMeLabel, "Email view 'Remember Me' label");
        return this;
    }

    @Step("Enter email '{email}'")
    public LoginPage enterEmail(String email) {
        type(emailInput, email, "Email input");
        return this;
    }

    @Step("Click 'Continue'")
    public LoginPage clickContinue() {
        click(continueButton, "Continue button");
        return this;
    }

    @Step("Continue with email '{email}'")
    public LoginPage continueWithEmail(String email) {
        return enterEmail(email).clickContinue();
    }

    @Step("Read the email view validation message")
    public String getEmailErrorMessage() {
        return readText(emailErrorMessage, "Email view validation message");
    }

    @Step("Open 'Log In with Username'")
    public LoginPage openUsernameLogin() {
        click(logInWithUsernameLink, "'Log In with Username' link");
        waitForVisible(usernameInput, "Username input");
        return this;
    }

    public boolean isUsernameInputDisplayed() {
        return isVisible(usernameInput);
    }

    public boolean isLogInButtonDisplayed() {
        return isVisible(logInButton);
    }

    public boolean isForgotPasswordLinkDisplayed() {
        return isVisible(forgotPasswordLink);
    }

    public boolean isUsernameRememberMeDisplayed() {
        return isVisible(usernameRememberMeCheckbox);
    }

    public boolean isUsernameRememberMeSelected() {
        return isSelected(usernameRememberMeCheckbox, "Username view 'Remember me' checkbox");
    }

    @Step("Toggle 'Remember me' on the username view")
    public LoginPage toggleUsernameRememberMe() {
        click(usernameRememberMeCheckbox, "Username view 'Remember me' checkbox");
        return this;
    }

    @Step("Enter username '{username}'")
    public LoginPage enterUsername(String username) {
        type(usernameInput, username, "Username input");
        return this;
    }

    public LoginPage enterPassword(String password) {
        Allure.step("Enter password", () -> type(passwordInput, password, "Password input"));
        return this;
    }

    @Step("Click 'Log In'")
    public LoginPage clickLogIn() {
        click(logInButton, "Log In button");
        return this;
    }

    @Step("Submit username '{username}' and wait for the password field")
    public LoginPage submitUsername(String username) {
        enterUsername(username).clickLogIn();
        waitForVisible(passwordInput, "Password input");
        return this;
    }

    public LoginPage loginWithUsername(String username, String password) {
        Allure.step("Log in with username '" + username + "'",
                () -> submitUsername(username).enterPassword(password).clickLogIn());
        return this;
    }

    public boolean isPasswordInputDisplayed() {
        return isVisible(passwordInput);
    }

    public String getPasswordInputType() {
        return readDomAttribute(passwordInput, "type", "Password input");
    }

    public String getSubmittedUsername() {
        return readText(submittedUsernameCard, "Submitted username identity card");
    }

    @Step("Read the username view error message")
    public String getUsernameErrorMessage() {
        return readText(usernameErrorMessage, "Username view error message");
    }

    @Step("Verify the login form is dismissed after authentication")
    public boolean isLoginFormDismissed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOf(logInButton));
        } catch (TimeoutException e) {
            return false;
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to determine the login form state", e);
        }
    }

    private WebElement waitForVisible(WebElement element, String elementName) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (WebDriverException e) {
            throw new LoginPageException(elementName + " was not visible within " + DEFAULT_TIMEOUT.toSeconds() + "s", e);
        }
    }

    private WebElement waitForClickable(WebElement element, String elementName) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (WebDriverException e) {
            throw new LoginPageException(elementName + " was not clickable within " + DEFAULT_TIMEOUT.toSeconds() + "s", e);
        }
    }

    private void click(WebElement element, String elementName) {
        try {
            waitForClickable(element, elementName).click();
        } catch (LoginPageException e) {
            throw e;
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to click " + elementName, e);
        }
    }

    private void type(WebElement element, String value, String elementName) {
        try {
            WebElement field = waitForVisible(element, elementName);
            field.clear();
            field.sendKeys(value);
        } catch (LoginPageException e) {
            throw e;
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to type into " + elementName, e);
        }
    }

    private String readText(WebElement element, String elementName) {
        try {
            return waitForVisible(element, elementName).getText().trim();
        } catch (LoginPageException e) {
            throw e;
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to read text from " + elementName, e);
        }
    }

    private String readDomAttribute(WebElement element, String attribute, String elementName) {
        try {
            return waitForVisible(element, elementName).getDomAttribute(attribute);
        } catch (LoginPageException e) {
            throw e;
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to read '" + attribute + "' from " + elementName, e);
        }
    }

    private boolean isVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to determine element visibility", e);
        }
    }

    private boolean isEnabled(WebElement element, String elementName) {
        try {
            return element.isEnabled();
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to read enabled state of " + elementName, e);
        }
    }

    private boolean isSelected(WebElement element, String elementName) {
        try {
            return waitForVisible(element, elementName).isSelected();
        } catch (LoginPageException e) {
            throw e;
        } catch (WebDriverException e) {
            throw new LoginPageException("Unable to read selected state of " + elementName, e);
        }
    }

    public static final class LoginPageException extends RuntimeException {
        public LoginPageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
