package com.salesforce.base;

import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;

public abstract class BaseTest {

    protected static final String LOGIN_URL = "https://login.salesforce.com/?locale=in";
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(60);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().ignoreIfMalformed().load();

    protected final String browser = System.getProperty("browser", "chrome").trim().toLowerCase(Locale.ROOT);
    protected final boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

    @BeforeTest(alwaysRun = true)
    public void writeAllureEnvironment() {
        Path resultsDir = Paths.get(System.getProperty("allure.results.directory", "target/allure-results"));
        Properties environment = new Properties();
        environment.setProperty("Application", "Salesforce Login");
        environment.setProperty("URL", LOGIN_URL);
        environment.setProperty("Browser", browser);
        environment.setProperty("Headless", String.valueOf(headless));
        environment.setProperty("Java", System.getProperty("java.version"));
        environment.setProperty("OS", System.getProperty("os.name"));
        try {
            Files.createDirectories(resultsDir);
            try (Writer writer = Files.newBufferedWriter(resultsDir.resolve("environment.properties"), StandardCharsets.UTF_8)) {
                environment.store(writer, null);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write Allure environment file to " + resultsDir, e);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        try {
            WebDriver driver = createDriver();
            DRIVER.set(driver);
            driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
            if (!headless) {
                driver.manage().window().maximize();
            }
            driver.get(LOGIN_URL);
        } catch (WebDriverException e) {
            quitDriver();
            throw new IllegalStateException("Browser session could not be started for '" + browser + "'", e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure screenshot - " + result.getName(), "image/png",
                        new ByteArrayInputStream(screenshot), "png");
                Allure.addAttachment("Failure URL", driver.getCurrentUrl());
            }
        } catch (WebDriverException e) {
            Allure.addAttachment("Screenshot capture error", e.getMessage());
        } finally {
            quitDriver();
        }
    }

    protected String resolveSetting(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = DOTENV.get(key);
        }
        return value == null ? "" : value.trim();
    }

    protected WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialised for the current thread");
        }
        return driver;
    }

    private WebDriver createDriver() {
        switch (browser) {
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications", "--window-size=1366,900");
                if (headless) {
                    options.addArguments("--headless=new");
                }
                return new ChromeDriver(options);
            }
            case "edge": {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--disable-notifications", "--window-size=1366,900");
                if (headless) {
                    options.addArguments("--headless=new");
                }
                return new EdgeDriver(options);
            }
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser + ". Supported values: chrome, edge");
        }
    }

    private void quitDriver() {
        WebDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (WebDriverException e) {
            Allure.addAttachment("Driver quit error", e.getMessage());
        } finally {
            DRIVER.remove();
        }
    }
}
