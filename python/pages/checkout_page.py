from playwright.sync_api import Page


class CheckoutPage:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.first_name = page.get_by_test_id("firstName")
        self.last_name = page.get_by_test_id("lastName")
        self.postal_code = page.get_by_test_id("postalCode")
        self.continue_button = page.get_by_test_id("continue")
        self.finish_button = page.get_by_test_id("finish")
        self.complete_header = page.get_by_test_id("complete-header")
        self.back_to_products_button = page.get_by_test_id("back-to-products")
        self.error_message = page.get_by_test_id("error")

    def fill_info(self, first: str, last: str, zip_code: str) -> None:
        self.first_name.fill(first)
        self.last_name.fill(last)
        self.postal_code.fill(zip_code)
        self.continue_button.click()

    def finish(self) -> None:
        self.finish_button.click()
