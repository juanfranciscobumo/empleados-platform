describe('Products Page - Visual Tests', () => {
  beforeEach(() => {
    cy.visit('/');
    cy.loginToDemo();
  });

  it('should capture products page baseline', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Products Page Baseline',
      batchName: 'Products Visual Tests',
    });

    cy.eyesCheckWindow('Products Page');

    cy.eyesClose();
  });

  it('should capture product detail view', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Product Detail View',
      batchName: 'Products Visual Tests',
    });

    cy.get('[data-test="inventory-item"]').first().click();
    cy.eyesCheckWindow('Product Detail');

    cy.eyesClose();
  });

  it('should capture product sort options', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Product Sort Options',
      batchName: 'Products Visual Tests',
    });

    cy.get('[data-test="product-sort-container"]').select('az');
    cy.eyesCheckWindow('Products Sorted A-Z');

    cy.get('[data-test="product-sort-container"]').select('za');
    cy.eyesCheckWindow('Products Sorted Z-A');

    cy.eyesClose();
  });

  it('should capture responsive layout - mobile', () => {
    cy.viewport(375, 667);

    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Products Page Mobile',
      batchName: 'Products Visual Tests',
    });

    cy.eyesCheckWindow('Products Page - Mobile');

    cy.eyesClose();
  });
});
