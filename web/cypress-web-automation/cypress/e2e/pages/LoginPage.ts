export class LoginPage {
  private usernameInput = '[data-test="username"]';
  private passwordInput = '[data-test="password"]';
  private loginButton = '[data-test="login-button"]';
  private errorMessage = '[data-test="error"]';

  visit(): void {
    cy.visit("/");
  }

  enterUsername(username: string): void {
    cy.get(this.usernameInput).type(username);
  }

  enterPassword(password: string): void {
    cy.get(this.passwordInput).type(password);
  }

  clickLogin(): void {
    cy.get(this.loginButton).click();
  }

  login(username: string, password: string): void {
    this.enterUsername(username);
    this.enterPassword(password);
    this.clickLogin();
  }

  getErrorMessage(): Cypress.Chainable<string> {
    return cy.get(this.errorMessage).invoke("text");
  }

  isErrorDisplayed(): Cypress.Chainable<boolean> {
    return cy.get(this.errorMessage).should("be.visible").then(() => true);
  }
}
