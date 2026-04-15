import { test, expect } from '../fixtures';

test.describe('Authentication', () => {
  test('successful login redirects to inventory', async ({ loginPage, page }) => {
    await loginPage.goto();
    await loginPage.login('standard_user', 'secret_sauce');

    await expect(page).toHaveURL(/inventory/);
    await expect(page.getByTestId('inventory-container')).toBeVisible();
  });

  test('locked out user sees error message', async ({ loginPage }) => {
    await loginPage.goto();
    await loginPage.login('locked_out_user', 'secret_sauce');

    await expect(loginPage.errorMessage).toContainText('Sorry, this user has been locked out');
  });

  test('session persists after page reload', async ({ authenticatedPage, page }) => {
    await expect(page).toHaveURL(/inventory/);

    await page.reload();

    await expect(page).toHaveURL(/inventory/);
    await expect(authenticatedPage.inventoryContainer).toBeVisible();
  });
});
