describe('Login Page - Visual Tests', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should capture login page baseline', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Login Page Baseline',
      batchName: 'Login Visual Tests',
    });

    cy.eyesCheckWindow('Login Page');

    cy.eyesClose();
  });

  it('should capture login error state', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Login Error State',
      batchName: 'Login Visual Tests',
    });

    cy.get('[data-test="login-button"]').click();
    cy.eyesCheckWindow('Login Error State');

    cy.eyesClose();
  });

  it('should capture locked out user error', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Locked Out User Error',
      batchName: 'Login Visual Tests',
    });

    cy.get('[data-test="username"]').type('locked_out_user');
    cy.get('[data-test="password"]').type('secret_sauce');
    cy.get('[data-test="login-button"]').click();
    cy.eyesCheckWindow('Locked Out User Error');

    cy.eyesClose();
  });
});
