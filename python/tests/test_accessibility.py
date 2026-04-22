from playwright.sync_api import Page, expect
from pytest_playwright_axe import Axe
from pages import LoginPage, ProductsPage


class TestAccessibility:
    def test_login_page_has_no_critical_accessibility_violations(
        self, login_page: LoginPage, page: Page
    ):
        login_page.goto()

        results = Axe().run(page, html_report_generated=True, json_report_generated=False)

        critical = [v for v in results.get("violations", []) if v.get("impact") == "critical"]
        assert critical == [], f"Critical violations found: {critical}"

    def test_inventory_page_has_no_critical_accessibility_violations(
        self, authenticated_page: ProductsPage, page: Page
    ):
        expect(authenticated_page.inventory_container).to_be_visible()

        results = Axe().run(page, html_report_generated=True, json_report_generated=False)

        critical = [v for v in results.get("violations", []) if v.get("impact") == "critical"]
        assert critical == [], f"Critical violations found: {critical}"
