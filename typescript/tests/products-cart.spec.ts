import { test, expect } from '../fixtures';

test.describe('Products & Cart', () => {
  test('products sort correctly by all options', async ({ authenticatedPage }) => {
    // A-Z (default)
    await authenticatedPage.sortBy('az');
    const namesAZ = await authenticatedPage.getItemNames();
    expect(namesAZ).toEqual([...namesAZ].sort());

    // Z-A
    await authenticatedPage.sortBy('za');
    const namesZA = await authenticatedPage.getItemNames();
    expect(namesZA).toEqual([...namesZA].sort().reverse());

    // Price low to high
    await authenticatedPage.sortBy('lohi');
    const pricesLow = await authenticatedPage.getItemPrices();
    expect(pricesLow).toEqual([...pricesLow].sort((a, b) => a - b));

    // Price high to low
    await authenticatedPage.sortBy('hilo');
    const pricesHigh = await authenticatedPage.getItemPrices();
    expect(pricesHigh).toEqual([...pricesHigh].sort((a, b) => b - a));
  });

  test('adding one product updates cart badge', async ({ authenticatedPage }) => {
    await expect(authenticatedPage.cartBadge).not.toBeVisible();

    await authenticatedPage.addToCartByName('Sauce Labs Backpack');

    await expect(authenticatedPage.cartBadge).toHaveText('1');
  });

  test('adding multiple products shows correct items in cart', async ({ authenticatedPage, cartPage }) => {
    const products = ['Sauce Labs Backpack', 'Sauce Labs Bike Light', 'Sauce Labs Bolt T-Shirt'];

    for (const product of products) {
      await authenticatedPage.addToCartByName(product);
    }
    await expect(authenticatedPage.cartBadge).toHaveText('3');

    await authenticatedPage.goToCart();
    const cartItems = await cartPage.getItemNames();
    expect(cartItems).toEqual(expect.arrayContaining(products));
    expect(cartItems).toHaveLength(products.length);
  });

  test('removing a product from cart', async ({ authenticatedPage, cartPage }) => {
    await authenticatedPage.addToCartByName('Sauce Labs Backpack');
    await authenticatedPage.addToCartByName('Sauce Labs Bike Light');
    await authenticatedPage.goToCart();

    await cartPage.removeByName('Sauce Labs Backpack');

    const remaining = await cartPage.getItemNames();
    expect(remaining).toEqual(['Sauce Labs Bike Light']);
  });
});
