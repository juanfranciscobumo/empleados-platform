import { InventoryPage } from "../pages/InventoryPage";
import { CartPage } from "../pages/CartPage";

describe("Cart Tests", () => {
  const inventoryPage = new InventoryPage();
  const cartPage = new CartPage();

  beforeEach(() => {
    cy.loginAsStandardUser();
  });

  it("debería agregar producto y verlo en el carrito", () => {
    inventoryPage.addToCart(0);
    inventoryPage.goToCart();
    cartPage.isCartDisplayed().should("be.true");
    cartPage.getItemCount().should("eq", 1);
  });

  it("debería agregar múltiples productos al carrito", () => {
    inventoryPage.addToCart(0);
    inventoryPage.addToCart(1);
    inventoryPage.addToCart(2);
    inventoryPage.goToCart();
    cartPage.getItemCount().should("eq", 3);
  });

  it("debería eliminar producto del carrito", () => {
    inventoryPage.addToCart(0);
    inventoryPage.addToCart(1);
    inventoryPage.goToCart();
    cartPage.removeItem(0);
    cartPage.getItemCount().should("eq", 1);
  });

  it("debería continuar comprando", () => {
    inventoryPage.addToCart(0);
    inventoryPage.goToCart();
    cartPage.continueShopping();
    cy.url().should("include", "/inventory.html");
  });

  it("debería mostrar nombre del producto en el carrito", () => {
    inventoryPage.addToCart(0);
    inventoryPage.goToCart();
    cartPage.getItemNames().should("have.length", 1);
  });
});
