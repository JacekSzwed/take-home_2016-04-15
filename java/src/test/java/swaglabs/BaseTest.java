package swaglabs;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public abstract class BaseTest {

    static Playwright playwright;
    static Browser browser;

    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void launchBrowser(TestInfo testInfo) {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");

        String browserName = System.getProperty("browser", "chromium");
        browser = switch (browserName) {
            case "firefox" -> playwright.firefox().launch();
            case "webkit" -> playwright.webkit().launch();
            default -> playwright.chromium().launch();
        };
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate(baseUrl() + "/");
    }

    @AfterEach
    void closeContext() {
        if (context != null) context.close();
    }

    static String baseUrl() {
        String env = System.getenv("BASE_URL");
        if (env != null && !env.isBlank()) return env;
        return System.getProperty("base.url", "https://www.saucedemo.com");
    }

    /** Logs in and navigates to inventory - use in tests that need authenticated state. */
    protected void login(String user, String pass) {
        page.getByTestId("username").fill(user);
        page.getByTestId("password").fill(pass);
        page.getByTestId("login-button").click();
    }

    protected void loginAsStandardUser() {
        login("standard_user", "secret_sauce");
    }
}
