import re
from playwright.sync_api import Page, expect
from pages import ProductsPage, CartPage, CheckoutPage


class TestCheckout:
    def test_complete_purchase_flow_from_login_to_order_confirmation(
        self,
        authenticated_page: ProductsPage,
        cart_page: CartPage,
        checkout_page: CheckoutPage,
        page: Page,
    ):
        authenticated_page.add_to_cart_by_name("Sauce Labs Backpack")
        authenticated_page.go_to_cart()

        expect(cart_page.cart_items).to_have_count(1)
        cart_page.checkout()

        checkout_page.fill_info("Jan", "Kowalski", "00-001")

        expect(page).to_have_url(re.compile(r"checkout-step-two"))
        expect(page.get_by_test_id("inventory-item-name")).to_have_text("Sauce Labs Backpack")
        checkout_page.finish()

        expect(page).to_have_url(re.compile(r"checkout-complete"))
        expect(checkout_page.complete_header).to_have_text("Thank you for your order!")
