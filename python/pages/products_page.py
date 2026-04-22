from playwright.sync_api import Page


class ProductsPage:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.inventory_container = page.get_by_test_id("inventory-container")
        self.inventory_items = page.get_by_test_id("inventory-item")
        self.sort_dropdown = page.get_by_test_id("product-sort-container")
        self.cart_link = page.get_by_test_id("shopping-cart-link")
        self.cart_badge = page.get_by_test_id("shopping-cart-badge")

    def sort_by(self, value: str) -> None:
        self.sort_dropdown.select_option(value)

    def add_to_cart_by_name(self, product_name: str) -> None:
        item = self.inventory_items.filter(has_text=product_name)
        item.get_by_role("button", name="Add to cart").click()

    def item_names(self) -> list[str]:
        return self.page.get_by_test_id("inventory-item-name").all_text_contents()

    def item_prices(self) -> list[float]:
        return [
            float(t.replace("$", ""))
            for t in self.page.get_by_test_id("inventory-item-price").all_text_contents()
        ]

    def go_to_cart(self) -> None:
        self.cart_link.click()
