from playwright.sync_api import Page, expect
from pages import LoginPage, ProductsPage


class TestNetwork:
    def test_inventory_page_works_when_images_fail_to_load(
        self, login_page: LoginPage, products_page: ProductsPage, page: Page
    ):
        page.route("**/*.{png,jpg,jpeg,svg}", lambda route: route.abort())

        login_page.goto()
        login_page.login("standard_user", "secret_sauce")

        expect(products_page.inventory_container).to_be_visible()
        assert len(products_page.item_names()) > 0
