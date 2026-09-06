import { test, expect, testData } from "../../fixtures/test-data";

test.describe("Inventory Tests", () => {
  test.beforeEach(async ({ loginPage }) => {
    await loginPage.login(
      testData.users.standard.username,
      testData.users.standard.password
    );
  });

  test("debería mostrar todos los productos", async ({ inventoryPage }) => {
    const isDisplayed = await inventoryPage.isInventoryDisplayed();
    expect(isDisplayed).toBe(true);
    const count = await inventoryPage.getItemCount();
    expect(count).toBe(6);
  });

  test("debería mostrar nombres de productos", async ({ inventoryPage }) => {
    const names = await inventoryPage.getItemNames();
    expect(names.length).toBe(6);
  });

  test("debería mostrar precios de productos", async ({ inventoryPage }) => {
    const prices = await inventoryPage.getItemPrices();
    expect(prices.length).toBe(6);
  });

  test("debería agregar producto al carrito", async ({ inventoryPage }) => {
    await inventoryPage.addToCart(0);
    const count = await inventoryPage.getCartBadgeCount();
    expect(count).toBe(1);
  });

  test("debería agregar múltiples productos al carrito", async ({ inventoryPage }) => {
    await inventoryPage.addToCart(0);
    await inventoryPage.addToCart(1);
    await inventoryPage.addToCart(2);
    const count = await inventoryPage.getCartBadgeCount();
    expect(count).toBe(3);
  });

  test("debería ordenar productos por precio (menor a mayor)", async ({ inventoryPage }) => {
    await inventoryPage.sortBy("lohi");
    const prices = await inventoryPage.getItemPrices();
    const sorted = [...prices].sort((a, b) => a - b);
    expect(prices).toEqual(sorted);
  });

  test("debería ordenar productos por precio (mayor a menor)", async ({ inventoryPage }) => {
    await inventoryPage.sortBy("hilo");
    const prices = await inventoryPage.getItemPrices();
    const sorted = [...prices].sort((a, b) => b - a);
    expect(prices).toEqual(sorted);
  });

  test("debería ordenar productos por nombre (A-Z)", async ({ inventoryPage }) => {
    await inventoryPage.sortBy("az");
    const names = await inventoryPage.getItemNames();
    const sorted = [...names].sort();
    expect(names).toEqual(sorted);
  });
});
