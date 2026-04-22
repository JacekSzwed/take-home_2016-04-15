package swaglabs.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;
    public final Locator username;
    public final Locator password;
    public final Locator loginButton;
    public final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.username = page.getByTestId("username");
        this.password = page.getByTestId("password");
        this.loginButton = page.getByTestId("login-button");
        this.errorMessage = page.getByTestId("error");
    }

    public void goto_() {
        page.navigate("/");
    }

    public void login(String user, String pass) {
        username.fill(user);
        password.fill(pass);
        loginButton.click();
    }
}
