# Swag Labs Playwright Test Suite

Automated tests for [Swag Labs](https://www.saucedemo.com/) demo application using Playwright with Page Object Model.

## Prerequisites

On a Windows machine you need **Node.js** installed. If you don't have it:

1. Download the installer from https://nodejs.org/ (LTS version).
2. Run the installer - accept defaults, ensure "Add to PATH" is checked.
3. Restart your terminal, then verify:

```
node -v
npm -v
```

## Setup & Run

```
# Clone the repo and enter the TypeScript project
git clone https://github.com/JacekSzwed/take-home_2016-04-15.git
cd take-home_2016-04-15/typescript

# Install dependencies
npm install

# Download browser binaries (Chromium + Firefox)
npx playwright install chromium firefox

# Run all tests (both browsers, parallel)
npm test

# Run on a single browser
npx playwright test --project=chromium
npx playwright test --project=firefox

# Open HTML report after run
npx playwright show-report
```

### Custom environment URL

```
# Windows PowerShell
$env:BASE_URL="http://saucedemo.com/"; npx playwright test
```

## Project Structure

```
├── pages/                    # Page Object Model
│   ├── login.page.ts         # Login page
│   ├── products.page.ts      # Product list (inventory)
│   ├── cart.page.ts          # Shopping cart
│   └── checkout.page.ts      # Checkout flow (step 1, 2, complete)
├── fixtures/
│   └── index.ts              # Playwright fixtures - shared setup
├── tests/
│   ├── auth.spec.ts          # Authentication (3 tests)
│   ├── products-cart.spec.ts # Products & cart (4 tests)
│   ├── checkout.spec.ts      # E2E checkout (1 test)
│   ├── network.spec.ts       # Network interception with page.route (1 test)
│   └── accessibility.spec.ts # axe-core a11y (2 tests)
├── playwright.config.ts      # Config: 2 browsers, parallel, HTML report
└── README.md
```

## Design Decisions

**Page Object Model** - each page has its own class with locators and actions. Tests compose these objects; no selector duplication.

**Locator strategy** - `getByTestId()` with `testIdAttribute: 'data-test'` configured in Playwright. Swag Labs consistently uses `data-test` attributes - these are the most stable selectors available.

**Fixtures** - custom `authenticatedPage` fixture logs in once per test, eliminating repeated login setup. Individual POM fixtures (`loginPage`, `productsPage`, `cartPage`, `checkoutPage`) are also available for tests that need unauthenticated access or specific page objects.

**Network interception** - `page.route` is used to block image requests, verifying that the inventory page degrades gracefully when assets fail to load.

**Accessibility** - `@axe-core/playwright` scans login and inventory pages for critical violations.

**No `waitForTimeout`** - all waits use Playwright's built-in auto-waiting and `expect` assertions with auto-retry.

**Parallel execution** - `fullyParallel: true` ensures maximum speed; each test gets a fresh browser context, so there's no shared state to cause flakiness.
