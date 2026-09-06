import "cypress-axe";

describe("Accessibility Tests", () => {
  beforeEach(() => {
    cy.visit("/");
  });

  it("Login page should have no critical accessibility violations", () => {
    cy.injectAxe();
    cy.checkA11y(null, {
      runOnly: {
        type: "tag",
        values: ["critical", "serious"],
      },
    });
  });

  it("Login form inputs should be focusable", () => {
    cy.get('[data-test="username"]').focus();
    cy.focused().should("have.attr", "data-test", "username");

    cy.get('[data-test="password"]').focus();
    cy.focused().should("have.attr", "data-test", "password");

    cy.get('[data-test="login-button"]').focus();
    cy.focused().should("have.attr", "data-test", "login-button");
  });

  it("Error messages should be visible after failed login", () => {
    cy.get('[data-test="login-button"]').click();
    cy.get('[data-test="error"]')
      .should("be.visible")
      .and("contain.text", "Username is required");
  });

  it("Page should have proper heading hierarchy", () => {
    cy.get("h1").should("have.length.at.most", 1);
  });

  it("Login inputs should have accessible names", () => {
    cy.get('[data-test="username"]').should("have.attr", "placeholder");
    cy.get('[data-test="password"]').should("have.attr", "placeholder");
  });
});
