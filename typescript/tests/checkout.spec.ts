import { test, expect } from '../fixtures';

test.describe('End-to-End Checkout', () => {
  test('complete purchase flow from login to order confirmation', async ({
    authenticatedPage,
    cartPage,
    checkoutPage,
    page,
  }) => {
    await authenticatedPage.addToCartByName('Sauce Labs Backpack');
    await authenticatedPage.goToCart();

    await expect(cartPage.cartItems).toHaveCount(1);
    await cartPage.checkout();

    await checkoutPage.fillInfo('Jan', 'Kowalski', '00-001');

    await expect(page).toHaveURL(/checkout-step-two/);
    await expect(page.getByTestId('inventory-item-name')).toHaveText('Sauce Labs Backpack');
    await checkoutPage.finish();

    await expect(page).toHaveURL(/checkout-complete/);
    await expect(checkoutPage.completeHeader).toHaveText('Thank you for your order!');
  });
});
