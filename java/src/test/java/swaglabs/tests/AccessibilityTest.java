package swaglabs.tests;

import com.deque.html.axecore.playwright.AxeBuilder;
import org.junit.jupiter.api.Test;
import swaglabs.BaseTest;
import swaglabs.pages.ProductsPage;

import com.deque.html.axecore.results.AxeResults;
import com.deque.html.axecore.results.Rule;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccessibilityTest extends BaseTest {

    @Test
    void loginPageHasNoCriticalAccessibilityViolations() throws Exception {
        AxeResults results = new AxeBuilder(page).analyze();

        List<Rule> critical = results.getViolations().stream()
                .filter(v -> "critical".equals(v.getImpact()))
                .toList();
        assertTrue(critical.isEmpty(), "Critical violations found: " + critical);
    }

    @Test
    void inventoryPageHasNoCriticalAccessibilityViolations() throws Exception {
        loginAsStandardUser();
        ProductsPage productsPage = new ProductsPage(page);
        assertThat(productsPage.inventoryContainer).isVisible();

        AxeResults results = new AxeBuilder(page).analyze();

        List<Rule> critical = results.getViolations().stream()
                .filter(v -> "critical".equals(v.getImpact()))
                .toList();
        assertTrue(critical.isEmpty(), "Critical violations found: " + critical);
    }
}
