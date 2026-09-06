import { InventoryPage } from "../pages/InventoryPage";
import { CartPage } from "../pages/CartPage";
import { CheckoutPage } from "../pages/CheckoutPage";

describe("Checkout Tests", () => {
  const inventoryPage = new InventoryPage();
  const cartPage = new CartPage();
  const checkoutPage = new CheckoutPage();

  beforeEach(() => {
    cy.loginAsStandardUser();
    inventoryPage.addToCart(0);
    inventoryPage.goToCart();
    cartPage.checkout();
  });

  it("debería completar el checkout exitosamente", () => {
    checkoutPage.fillCheckoutInfo("Juan", "Perez", "12345");
    checkoutPage.continueToOverview();
    checkoutPage.finishCheckout();
    checkoutPage.getCompleteMessage().should("contain", "Thank you for your order");
  });

  it("debería cancelar el checkout", () => {
    checkoutPage.cancelCheckout();
    cy.url().should("include", "/cart.html");
  });

  it("debería mostrar error si falta el nombre", () => {
    checkoutPage.enterLastName("Perez");
    checkoutPage.enterPostalCode("12345");
    checkoutPage.continueToOverview();
    cy.get('[data-test="error"]').should("contain", "First Name is required");
  });

  it("debería mostrar error si falta el apellido", () => {
    checkoutPage.enterFirstName("Juan");
    checkoutPage.enterPostalCode("12345");
    checkoutPage.continueToOverview();
    cy.get('[data-test="error"]').should("contain", "Last Name is required");
  });

  it("debería mostrar error si falta el código postal", () => {
    checkoutPage.enterFirstName("Juan");
    checkoutPage.enterLastName("Perez");
    checkoutPage.continueToOverview();
    cy.get('[data-test="error"]').should("contain", "Postal Code is required");
  });

  it("debería volver a productos después de completar compra", () => {
    checkoutPage.fillCheckoutInfo("Juan", "Perez", "12345");
    checkoutPage.continueToOverview();
    checkoutPage.finishCheckout();
    checkoutPage.backToProducts();
    cy.url().should("include", "/inventory.html");
  });
});
