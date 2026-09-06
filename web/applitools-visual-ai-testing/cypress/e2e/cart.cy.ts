describe('Cart Flow - Visual Tests', () => {
  beforeEach(() => {
    cy.visit('/');
    cy.loginToDemo();
  });

  it('should capture empty cart state', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Empty Cart State',
      batchName: 'Cart Visual Tests',
    });

    cy.get('[data-test="shopping-cart-link"]').click();
    cy.eyesCheckWindow('Empty Cart');

    cy.eyesClose();
  });

  it('should capture cart with items', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Cart With Items',
      batchName: 'Cart Visual Tests',
    });

    cy.get('[data-test="add-to-cart-sauce-labs-backpack"]').click();
    cy.get('[data-test="add-to-cart-sauce-labs-bike-light"]').click();
    cy.get('[data-test="shopping-cart-link"]').click();
    cy.eyesCheckWindow('Cart With 2 Items');

    cy.eyesClose();
  });

  it('should capture checkout step one', () => {
    cy.get('[data-test="add-to-cart-sauce-labs-backpack"]').click();
    cy.get('[data-test="shopping-cart-link"]').click();

    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Checkout Step One',
      batchName: 'Cart Visual Tests',
    });

    cy.get('[data-test="checkout"]').click();
    cy.eyesCheckWindow('Checkout Step One');

    cy.eyesClose();
  });
});
