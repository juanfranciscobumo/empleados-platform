import '@applitools/eyes-cypress/commands';

Cypress.Commands.add('login', (username: string, password: string) => {
  cy.get('[data-test="username"]').type(username);
  cy.get('[data-test="password"]').type(password);
  cy.get('[data-test="login-button"]').click();
});

Cypress.Commands.add('loginToDemo', () => {
  cy.visit('/');
  cy.login('standard_user', 'secret_sauce');
});
