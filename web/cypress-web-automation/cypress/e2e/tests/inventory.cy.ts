import { InventoryPage } from "../pages/InventoryPage";

describe("Inventory Tests", () => {
  const inventoryPage = new InventoryPage();

  beforeEach(() => {
    cy.loginAsStandardUser();
  });

  it("debería mostrar todos los productos", () => {
    inventoryPage.isInventoryDisplayed().should("be.true");
    inventoryPage.getItemCount().should("eq", 6);
  });

  it("debería mostrar nombres de productos", () => {
    inventoryPage.getItemNames().should("have.length", 6);
  });

  it("debería mostrar precios de productos", () => {
    inventoryPage.getItemPrices().should("have.length", 6);
  });

  it("debería agregar producto al carrito", () => {
    inventoryPage.addToCart(0);
    inventoryPage.getCartBadgeCount().should("eq", 1);
  });

  it("debería agregar múltiples productos al carrito", () => {
    inventoryPage.addToCart(0);
    inventoryPage.addToCart(1);
    inventoryPage.addToCart(2);
    inventoryPage.getCartBadgeCount().should("eq", 3);
  });

  it("debería ordenar productos por precio (menor a mayor)", () => {
    inventoryPage.sortBy("lohi");
    inventoryPage.getItemPrices().then((prices) => {
      const sorted = [...prices].sort((a, b) => a - b);
      expect(prices).to.deep.equal(sorted);
    });
  });

  it("debería ordenar productos por precio (mayor a menor)", () => {
    inventoryPage.sortBy("hilo");
    inventoryPage.getItemPrices().then((prices) => {
      const sorted = [...prices].sort((a, b) => b - a);
      expect(prices).to.deep.equal(sorted);
    });
  });

  it("debería ordenar productos por nombre (A-Z)", () => {
    inventoryPage.sortBy("az");
    inventoryPage.getItemNames().then((names) => {
      const sorted = [...names].sort();
      expect(names).to.deep.equal(sorted);
    });
  });
});
