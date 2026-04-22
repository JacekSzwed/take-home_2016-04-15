# Swag Labs Playwright Test Suite (Java)

Automated tests for [Swag Labs](https://www.saucedemo.com/) demo application using Playwright for Java with Page Object Model.

## Prerequisites

On a Windows machine you need **Java 17+** and **Maven** installed.

### Java 17

1. Download Microsoft OpenJDK 17 from https://learn.microsoft.com/en-us/java/openjdk/download (`.msi` installer).
2. Run the installer - ensure "Add to PATH" and "Set JAVA_HOME" are checked.
3. Restart your terminal, then verify:

```
java -version
```

### Maven

1. Install via [Chocolatey](https://chocolatey.org/install):

```powershell
choco install maven -y
```

Or download manually from https://maven.apache.org/download.cgi, extract, and add `bin/` to PATH.

2. Verify:

```
mvn -version
```

## Setup & Run

```powershell
# Clone the repo and enter the Java project
git clone https://github.com/JacekSzwed/take-home_2016-04-15.git
cd take-home_2016-04-15/java

# Install Playwright browsers (Chromium + Firefox)
mvn exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install chromium firefox" "-Dexec.classpathScope=test"

# Run all tests (both browsers)
mvn test

# Run on a single browser
mvn test "-Dbrowser=chromium"
mvn test "-Dbrowser=firefox"

# Open HTML report after run
mvn surefire-report:report; Start-Process target\reports\surefire.html
```

### Custom environment URL

```powershell
# Windows PowerShell
$env:BASE_URL="http://saucedemo.com/"; mvn test

# Or via JVM system property
mvn test "-Dbase.url=http://saucedemo.com/"
```

## Project Structure

```
├── src/test/java/swaglabs/
│   ├── BaseTest.java                  # Base class: browser lifecycle, login helpers
│   ├── pages/
│   │   ├── LoginPage.java             # Login page
│   │   ├── ProductsPage.java          # Product list (inventory)
│   │   ├── CartPage.java              # Shopping cart
│   │   └── CheckoutPage.java          # Checkout flow (step 1, 2, complete)
│   └── tests/
│       ├── AuthTest.java              # Authentication (3 tests)
│       ├── ProductsCartTest.java      # Products & cart (4 tests)
│       ├── CheckoutTest.java          # E2E checkout (1 test)
│       ├── NetworkTest.java           # Network interception with page.route (1 test)
│       └── AccessibilityTest.java     # axe-core a11y (2 tests)
├── pom.xml                            # Maven config: dependencies, surefire plugin
└── README.md
```

## Design Decisions

**Page Object Model** - each page has its own class with locators and actions. Tests compose these objects; no selector duplication.

**Locator strategy** - `getByTestId()` with `setTestIdAttribute("data-test")` configured in BaseTest. Swag Labs consistently uses `data-test` attributes - these are the most stable selectors available.

**BaseTest** - abstract base class managing Playwright/Browser/BrowserContext lifecycle via JUnit 5 `@BeforeAll`/`@BeforeEach`/`@AfterEach`/`@AfterAll`. Each test gets a fresh browser context (isolation). `loginAsStandardUser()` eliminates repeated login setup - equivalent to the TypeScript `authenticatedPage` fixture.

**Browser selection** - `mvn test` runs both Chromium and Firefox via Maven profiles (two surefire executions). Pass `-Dbrowser=chromium` or `-Dbrowser=firefox` to run a single browser.

**Network interception** - `page.route` blocks image requests, verifying that the inventory page degrades gracefully when assets fail to load.

**Accessibility** - `axe-core-maven-html` (Playwright module) scans login and inventory pages for critical violations.

**No `Thread.sleep`** - all waits use Playwright's built-in auto-waiting and `assertThat` assertions with auto-retry.

**Parallel execution** - JUnit 5 parallel mode enabled via `junit-platform.properties`. Test classes run concurrently (`concurrent`), methods within a class run sequentially (`same_thread`). Each class gets its own Browser + BrowserContext, so there's no shared state between parallel classes.
