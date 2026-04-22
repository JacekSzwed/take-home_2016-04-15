import os
import pytest
from playwright.sync_api import Page, Playwright
from pages import LoginPage, ProductsPage, CartPage, CheckoutPage


@pytest.fixture(scope="session", autouse=True)
def _set_test_id_attribute(playwright: Playwright) -> None:
    playwright.selectors.set_test_id_attribute("data-test")


@pytest.fixture(scope="session")
def base_url():
    return os.environ.get("BASE_URL", "https://www.saucedemo.com")


@pytest.fixture
def login_page(page: Page) -> LoginPage:
    return LoginPage(page)


@pytest.fixture
def products_page(page: Page) -> ProductsPage:
    return ProductsPage(page)


@pytest.fixture
def cart_page(page: Page) -> CartPage:
    return CartPage(page)


@pytest.fixture
def checkout_page(page: Page) -> CheckoutPage:
    return CheckoutPage(page)


@pytest.fixture
def authenticated_page(page: Page) -> ProductsPage:
    login = LoginPage(page)
    login.goto()
    login.login("standard_user", "secret_sauce")
    return ProductsPage(page)
