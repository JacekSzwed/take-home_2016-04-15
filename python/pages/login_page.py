from playwright.sync_api import Page


class LoginPage:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.username = page.get_by_test_id("username")
        self.password = page.get_by_test_id("password")
        self.login_button = page.get_by_test_id("login-button")
        self.error_message = page.get_by_test_id("error")

    def goto(self) -> None:
        self.page.goto("/")

    def login(self, user: str, password: str) -> None:
        self.username.fill(user)
        self.password.fill(password)
        self.login_button.click()
