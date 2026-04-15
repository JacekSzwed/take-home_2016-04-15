import { test, expect } from '../fixtures';

test.describe('Network', () => {
  test('inventory page works when images fail to load', async ({ loginPage, productsPage, page }) => {
    // Block all image requests to simulate network issues
    await page.route('**/*.{png,jpg,jpeg,svg}', route => route.abort());

    await loginPage.goto();
    await loginPage.login('standard_user', 'secret_sauce');

    await expect(productsPage.inventoryContainer).toBeVisible();
    const names = await productsPage.getItemNames();
    expect(names.length).toBeGreaterThan(0);
  });
});
