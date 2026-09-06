export class CartPage {
  private cartList = ".cart_list";
  private cartItem = ".cart_item";
  private itemName = ".inventory_item_name";
  private itemPrice = ".inventory_item_price";
  private removeButton = ".cart_button";
  private checkoutButton = '[data-test="checkout"]';
  private continueShoppingButton = '[data-test="continue-shopping"]';

  isCartDisplayed(): Cypress.Chainable<boolean> {
    return cy.get(this.cartList).should("be.visible").then(() => true);
  }

  getItemCount(): Cypress.Chainable<number> {
    return cy.get(this.cartItem).its("length");
  }

  getItemNames(): Cypress.Chainable<string[]> {
    return cy.get(this.itemName).invoke("text").then((text) => text.split("\n"));
  }

  removeItem(index: number): void {
    cy.get(this.cartItem).eq(index).find(this.removeButton).click();
  }

  checkout(): void {
    cy.get(this.checkoutButton).click();
    cy.url().should("include", "/checkout-step-one.html");
  }

  continueShopping(): void {
    cy.get(this.continueShoppingButton).click();
    cy.url().should("include", "/inventory.html");
  }
}
