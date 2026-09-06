import { test, expect, testData } from "../../fixtures/test-data";

test.describe("Checkout Tests", () => {
  test.beforeEach(async ({ loginPage, inventoryPage, cartPage }) => {
    await loginPage.login(
      testData.users.standard.username,
      testData.users.standard.password
    );
    await inventoryPage.addToCart(0);
    await inventoryPage.goToCart();
    await cartPage.checkout();
  });

  test("debería completar el checkout exitosamente", async ({ checkoutPage }) => {
    await checkoutPage.fillCheckoutInfo(
      testData.checkout.firstName,
      testData.checkout.lastName,
      testData.checkout.postalCode
    );
    await checkoutPage.continueToOverview();
    await checkoutPage.finishCheckout();
    const message = await checkoutPage.getCompleteMessage();
    expect(message).toContain("Thank you for your order");
  });

  test("debería cancelar el checkout", async ({ checkoutPage, page }) => {
    await checkoutPage.cancelCheckout();
    expect(page.url()).toContain("cart.html");
  });

  test("debería mostrar error si falta el nombre", async ({ checkoutPage }) => {
    await checkoutPage.fillCheckoutInfo("", testData.checkout.lastName, testData.checkout.postalCode);
    await checkoutPage.continueToOverview();
    const error = await checkoutPage.getErrorMessage();
    expect(error).toContain("First Name is required");
  });

  test("debería mostrar error si falta el apellido", async ({ checkoutPage }) => {
    await checkoutPage.fillCheckoutInfo(testData.checkout.firstName, "", testData.checkout.postalCode);
    await checkoutPage.continueToOverview();
    const error = await checkoutPage.getErrorMessage();
    expect(error).toContain("Last Name is required");
  });

  test("debería mostrar error si falta el código postal", async ({ checkoutPage }) => {
    await checkoutPage.fillCheckoutInfo(testData.checkout.firstName, testData.checkout.lastName, "");
    await checkoutPage.continueToOverview();
    const error = await checkoutPage.getErrorMessage();
    expect(error).toContain("Postal Code is required");
  });

  test("debería volver a productos después de completar compra", async ({ checkoutPage, page }) => {
    await checkoutPage.fillCheckoutInfo(
      testData.checkout.firstName,
      testData.checkout.lastName,
      testData.checkout.postalCode
    );
    await checkoutPage.continueToOverview();
    await checkoutPage.finishCheckout();
    await checkoutPage.backToProducts();
    expect(page.url()).toContain("inventory.html");
  });
});
