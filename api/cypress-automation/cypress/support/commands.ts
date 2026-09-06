/// <reference types="cypress" />

// ============================================
// Comandos de API
// ============================================

Cypress.Commands.add("apiGet", <T>(endpoint: string) => {
  return cy.request<T>({
    method: "GET",
    url: endpoint,
    headers: {
      "Content-Type": "application/json",
    },
  });
});

Cypress.Commands.add("apiPost", <T>(endpoint: string, body: unknown) => {
  return cy.request<T>({
    method: "POST",
    url: endpoint,
    body,
    headers: {
      "Content-Type": "application/json",
    },
  });
});

Cypress.Commands.add("apiPut", <T>(endpoint: string, body: unknown) => {
  return cy.request<T>({
    method: "PUT",
    url: endpoint,
    body,
    headers: {
      "Content-Type": "application/json",
    },
  });
});

Cypress.Commands.add("apiDelete", <T>(endpoint: string) => {
  return cy.request<T>({
    method: "DELETE",
    url: endpoint,
    headers: {
      "Content-Type": "application/json",
    },
  });
});

// ============================================
// Comandos de Autenticación
// ============================================

Cypress.Commands.add("login", (username: string, password: string) => {
  cy.session([username, password], () => {
    cy.visit("/login");
    cy.get('[data-cy="username"]').type(username);
    cy.get('[data-cy="password"]').type(password);
    cy.get('[data-cy="submit"]').click();
    cy.url().should("not.include", "/login");
  });
});

// ============================================
// Comandos de Fixtures
// ============================================

Cypress.Commands.add("getFixture", <T extends keyof Cypress.Fixtures>(fixture: T) => {
  return cy.fixture(fixture) as unknown as Cypress.Chainable<Cypress.Fixtures[T]>;
});
