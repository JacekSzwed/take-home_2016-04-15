from playwright.sync_api import Page


class CartPage:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.cart_list = page.get_by_test_id("cart-list")
        self.cart_items = page.get_by_test_id("inventory-item")
        self.checkout_button = page.get_by_test_id("checkout")
        self.continue_shopping_button = page.get_by_test_id("continue-shopping")

    def remove_by_name(self, product_name: str) -> None:
        item = self.cart_items.filter(has_text=product_name)
        item.get_by_role("button", name="Remove").click()

    def item_names(self) -> list[str]:
        return self.page.get_by_test_id("inventory-item-name").all_text_contents()

    def checkout(self) -> None:
        self.checkout_button.click()
