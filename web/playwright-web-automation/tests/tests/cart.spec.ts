import { test, expect, testData } from "../../fixtures/test-data";

test.describe("Cart Tests", () => {
  test.beforeEach(async ({ loginPage }) => {
    await loginPage.login(
      testData.users.standard.username,
      testData.users.standard.password
    );
  });

  test("debería agregar producto y verlo en el carrito", async ({ inventoryPage, cartPage }) => {
    await inventoryPage.addToCart(0);
    await inventoryPage.goToCart();
    const isDisplayed = await cartPage.isCartDisplayed();
    expect(isDisplayed).toBe(true);
    const count = await cartPage.getItemCount();
    expect(count).toBe(1);
  });

  test("debería agregar múltiples productos al carrito", async ({ inventoryPage, cartPage }) => {
    await inventoryPage.addToCart(0);
    await inventoryPage.addToCart(1);
    await inventoryPage.addToCart(2);
    await inventoryPage.goToCart();
    const count = await cartPage.getItemCount();
    expect(count).toBe(3);
  });

  test("debería eliminar producto del carrito", async ({ inventoryPage, cartPage }) => {
    await inventoryPage.addToCart(0);
    await inventoryPage.addToCart(1);
    await inventoryPage.goToCart();
    await cartPage.removeItem(0);
    const count = await cartPage.getItemCount();
    expect(count).toBe(1);
  });

  test("debería continuar comprando", async ({ inventoryPage, cartPage, page }) => {
    await inventoryPage.addToCart(0);
    await inventoryPage.goToCart();
    await cartPage.continueShopping();
    expect(page.url()).toContain("inventory.html");
  });

  test("debería mostrar nombre del producto en el carrito", async ({ inventoryPage, cartPage }) => {
    await inventoryPage.addToCart(0);
    await inventoryPage.goToCart();
    const names = await cartPage.getItemNames();
    expect(names.length).toBe(1);
  });
});
