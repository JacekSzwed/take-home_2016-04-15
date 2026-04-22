import re
from playwright.sync_api import Page, expect
from pages import LoginPage, ProductsPage


class TestAuth:
    def test_successful_login_redirects_to_inventory(self, login_page: LoginPage, page: Page):
        login_page.goto()
        login_page.login("standard_user", "secret_sauce")

        expect(page).to_have_url(re.compile(r"inventory"))
        expect(page.get_by_test_id("inventory-container")).to_be_visible()

    def test_locked_out_user_sees_error_message(self, login_page: LoginPage):
        login_page.goto()
        login_page.login("locked_out_user", "secret_sauce")

        expect(login_page.error_message).to_contain_text("Sorry, this user has been locked out")

    def test_session_persists_after_page_reload(self, authenticated_page: ProductsPage, page: Page):
        expect(page).to_have_url(re.compile(r"inventory"))

        page.reload()

        expect(page).to_have_url(re.compile(r"inventory"))
        expect(authenticated_page.inventory_container).to_be_visible()
