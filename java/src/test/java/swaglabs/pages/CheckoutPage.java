package swaglabs.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutPage {

    private final Page page;
    public final Locator firstName;
    public final Locator lastName;
    public final Locator postalCode;
    public final Locator continueButton;
    public final Locator finishButton;
    public final Locator completeHeader;
    public final Locator backToProductsButton;
    public final Locator errorMessage;

    public CheckoutPage(Page page) {
        this.page = page;
        this.firstName = page.getByTestId("firstName");
        this.lastName = page.getByTestId("lastName");
        this.postalCode = page.getByTestId("postalCode");
        this.continueButton = page.getByTestId("continue");
        this.finishButton = page.getByTestId("finish");
        this.completeHeader = page.getByTestId("complete-header");
        this.backToProductsButton = page.getByTestId("back-to-products");
        this.errorMessage = page.getByTestId("error");
    }

    public void fillInfo(String first, String last, String zip) {
        firstName.fill(first);
        lastName.fill(last);
        postalCode.fill(zip);
        continueButton.click();
    }

    public void finish() {
        finishButton.click();
    }
}
