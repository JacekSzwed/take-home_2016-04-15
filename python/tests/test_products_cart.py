from playwright.sync_api import Page, expect
from pages import ProductsPage, CartPage


class TestProductsCart:
    def test_products_sort_correctly_by_all_options(self, authenticated_page: ProductsPage):
        # A-Z (default)
        authenticated_page.sort_by("az")
        names_az = authenticated_page.item_names()
        assert names_az == sorted(names_az)

        # Z-A
        authenticated_page.sort_by("za")
        names_za = authenticated_page.item_names()
        assert names_za == sorted(names_za, reverse=True)

        # Price low to high
        authenticated_page.sort_by("lohi")
        prices_low = authenticated_page.item_prices()
        assert prices_low == sorted(prices_low)

        # Price high to low
        authenticated_page.sort_by("hilo")
        prices_high = authenticated_page.item_prices()
        assert prices_high == sorted(prices_high, reverse=True)

    def test_adding_one_product_updates_cart_badge(self, authenticated_page: ProductsPage):
        expect(authenticated_page.cart_badge).not_to_be_visible()

        authenticated_page.add_to_cart_by_name("Sauce Labs Backpack")

        expect(authenticated_page.cart_badge).to_have_text("1")

    def test_adding_multiple_products_shows_correct_items_in_cart(
        self, authenticated_page: ProductsPage, cart_page: CartPage
    ):
        products = ["Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"]

        for product in products:
            authenticated_page.add_to_cart_by_name(product)
        expect(authenticated_page.cart_badge).to_have_text("3")

        authenticated_page.go_to_cart()
        cart_items = cart_page.item_names()
        assert len(cart_items) == len(products)
        assert set(cart_items) == set(products)

    def test_removing_product_from_cart(
        self, authenticated_page: ProductsPage, cart_page: CartPage
    ):
        authenticated_page.add_to_cart_by_name("Sauce Labs Backpack")
        authenticated_page.add_to_cart_by_name("Sauce Labs Bike Light")
        authenticated_page.go_to_cart()

        cart_page.remove_by_name("Sauce Labs Backpack")

        assert cart_page.item_names() == ["Sauce Labs Bike Light"]
