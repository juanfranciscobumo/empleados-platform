export class InventoryPage {
  private inventoryContainer = ".inventory_list";
  private inventoryItem = ".inventory_item";
  private itemName = ".inventory_item_name";
  private itemPrice = ".inventory_item_price";
  private addToCartButton = ".btn btn_inventory";
  private cartLink = '[data-test="shopping-cart-link"]';
  private cartBadge = ".shopping_cart_badge";

  isInventoryDisplayed(): Cypress.Chainable<boolean> {
    return cy.get(this.inventoryContainer).should("be.visible").then(() => true);
  }

  getItemCount(): Cypress.Chainable<number> {
    return cy.get(this.inventoryItem).its("length");
  }

  getItemNames(): Cypress.Chainable<string[]> {
    const names: string[] = [];
    return cy
      .get(this.itemName)
      .each(($el) => {
        names.push($el.text());
      })
      .then(() => names);
  }

  getItemPrices(): Cypress.Chainable<number[]> {
    const prices: number[] = [];
    return cy
      .get(this.itemPrice)
      .each(($el) => {
        prices.push(parseFloat($el.text().replace("$", "")));
      })
      .then(() => prices);
  }

  addToCart(index: number): void {
    cy.get(this.inventoryItem).eq(index).find("button").click();
  }

  removeFromCart(index: number): void {
    cy.get(this.inventoryItem).eq(index).find("button").click();
  }

  goToCart(): void {
    cy.get(this.cartLink).click();
    cy.url().should("include", "/cart.html");
  }

  getCartBadgeCount(): Cypress.Chainable<number> {
    return cy.get(this.cartBadge).invoke("text").then(Number);
  }

  sortBy(option: string): void {
    cy.get(".product_sort_container").select(option);
  }
}
