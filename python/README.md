# Swag Labs Playwright Test Suite (Python)

Automated tests for [Swag Labs](https://www.saucedemo.com/) demo application using Playwright for Python with Page Object Model.

## Prerequisites

On a Windows machine you need **Python 3.14+** installed. If you don't have it:

1. Download the installer from https://www.python.org/downloads/ (latest 3.14.x).
2. Run the installer - check **"Add python.exe to PATH"** before clicking Install.
3. Restart your terminal, then verify:

```
py --version
```

## Setup & Run

```powershell
# Clone the repo and enter the Python project
git clone https://github.com/JacekSzwed/take-home_2016-04-15.git
cd take-home_2016-04-15/python

# Create virtual environment and activate it
py -3.14 -m venv .venv
.\.venv\Scripts\Activate.ps1

# Install dependencies
pip install -r requirements.txt

# Download browser binaries (Chromium + Firefox)
playwright install chromium firefox

# Run all tests (both browsers)
pytest

# Run on a single browser
pytest --browser chromium --override-ini="addopts="
pytest --browser firefox --override-ini="addopts="

# Open HTML report after run
Start-Process report.html
# There is also .\axe-reports\ directory created containing Axe accessibility reports
# also in HTML format
```

### Custom environment URL

```powershell
$env:BASE_URL="http://saucedemo.com/"; pytest
```

## Project Structure

```
├── pages/                        # Page Object Model
│   ├── login_page.py             # Login page
│   ├── products_page.py          # Product list (inventory)
│   ├── cart_page.py              # Shopping cart
│   └── checkout_page.py          # Checkout flow (step 1, 2, complete)
├── tests/
│   ├── test_auth.py              # Authentication (3 tests)
│   ├── test_products_cart.py     # Products & cart (4 tests)
│   ├── test_checkout.py          # E2E checkout (1 test)
│   ├── test_network.py           # Network interception with page.route (1 test)
│   └── test_accessibility.py     # axe-core a11y (2 tests)
├── conftest.py                   # Fixtures: authenticated_page, POM objects
├── pytest.ini                    # Config: 2 browsers, parallel, HTML report, base URL
├── requirements.txt              # Pinned dependencies
└── README.md
```

## Design Decisions

**Page Object Model** - each page has its own class with locators and actions. Tests compose these objects; no selector duplication. Pythonic naming (`snake_case`) throughout.

**Locator strategy** - `get_by_test_id()` with `set_test_id_attribute("data-test")` configured in a session-scoped fixture. Swag Labs consistently uses `data-test` attributes - these are the most stable selectors available.

**Fixtures** - `conftest.py` provides `authenticated_page` (logs in once per test) and individual POM fixtures (`login_page`, `products_page`, `cart_page`, `checkout_page`). No login duplication across tests.

**Network interception** - `page.route` blocks image requests, verifying that the inventory page degrades gracefully when assets fail to load.

**Accessibility** - `pytest-playwright-axe` scans login and inventory pages for critical violations. The `select-name` rule is disabled on the inventory page - this is a known Swag Labs defect (sort dropdown has no accessible label).

**No `time.sleep`** - all waits use Playwright's built-in auto-waiting and `expect` assertions with auto-retry.

**Parallel execution** - `pytest-xdist` with `-n auto` distributes tests across CPU cores. Each worker gets its own browser instance via pytest-playwright, so there's no shared state.

**Configuration** - `pytest.ini` sets both browsers, base URL, HTML report, and parallel mode as defaults. Override with CLI flags for single-browser runs or custom URLs via `BASE_URL` env var.
