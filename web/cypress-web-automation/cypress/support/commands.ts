/// <reference types="cypress" />

// ============================================
// Comandos de Login
// ============================================

Cypress.Commands.add("login", (username: string, password: string) => {
  cy.visit("/");
  cy.get('[data-test="username"]').type(username);
  cy.get('[data-test="password"]').type(password);
  cy.get('[data-test="login-button"]').click();
  cy.url().should("include", "/inventory.html");
});

Cypress.Commands.add("loginAsStandardUser", () => {
  cy.login("standard_user", "secret_sauce");
});

Cypress.Commands.add("loginAsProblemUser", () => {
  cy.login("problem_user", "secret_sauce");
});

Cypress.Commands.add("loginAsLockedOutUser", () => {
  cy.login("locked_out_user", "secret_sauce");
});

// ============================================
// Comandos de Productos
// ============================================

Cypress.Commands.add("addToCart", (index: number) => {
  cy.get(".inventory_item").eq(index).find("button").click();
});

Cypress.Commands.add("goToCart", () => {
  cy.get('[data-test="shopping-cart-link"]').click();
  cy.url().should("include", "/cart.html");
});

// ============================================
// Comandos de Selectores
// ============================================

Cypress.Commands.add("getByDataTest", (selector: string) => {
  return cy.get(`[data-test="${selector}"]`);
});
