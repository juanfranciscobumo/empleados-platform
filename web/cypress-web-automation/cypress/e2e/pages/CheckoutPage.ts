export class CheckoutPage {
  private firstNameInput = '[data-test="firstName"]';
  private lastNameInput = '[data-test="lastName"]';
  private postalCodeInput = '[data-test="postalCode"]';
  private continueButton = '[data-test="continue"]';
  private finishButton = '[data-test="finish"]';
  private cancelButton = '[data-test="cancel"]';
  private completeHeader = '[data-test="complete-header"]';
  private backHomeButton = '[data-test="back-to-products"]';

  enterFirstName(firstName: string): void {
    cy.get(this.firstNameInput).type(firstName);
  }

  enterLastName(lastName: string): void {
    cy.get(this.lastNameInput).type(lastName);
  }

  enterPostalCode(postalCode: string): void {
    cy.get(this.postalCodeInput).type(postalCode);
  }

  fillCheckoutInfo(firstName: string, lastName: string, postalCode: string): void {
    this.enterFirstName(firstName);
    this.enterLastName(lastName);
    this.enterPostalCode(postalCode);
  }

  continueToOverview(): void {
    cy.get(this.continueButton).click();
  }

  finishCheckout(): void {
    cy.get(this.finishButton).click();
    cy.url().should("include", "/checkout-complete.html");
  }

  cancelCheckout(): void {
    cy.get(this.cancelButton).click();
    cy.url().should("include", "/cart.html");
  }

  getCompleteMessage(): Cypress.Chainable<string> {
    return cy.get(this.completeHeader).invoke("text");
  }

  backToProducts(): void {
    cy.get(this.backHomeButton).click();
    cy.url().should("include", "/inventory.html");
  }
}
