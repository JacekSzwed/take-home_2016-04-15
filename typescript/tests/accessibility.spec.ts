import { test, expect } from '../fixtures';
import AxeBuilder from '@axe-core/playwright';

test.describe('Accessibility', () => {
  test('login page has no critical accessibility violations', async ({ loginPage, page }) => {
    await loginPage.goto();

    const results = await new AxeBuilder({ page }).analyze();

    expect(results.violations.filter(v => v.impact === 'critical')).toEqual([]);
  });

  test('inventory page has no critical accessibility violations', async ({ authenticatedPage, page }) => {
    await expect(authenticatedPage.inventoryContainer).toBeVisible();

    const results = await new AxeBuilder({ page })
      .analyze();

    expect(results.violations.filter(v => v.impact === 'critical')).toEqual([]);
  });
});
