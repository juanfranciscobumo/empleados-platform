describe('Cross-Browser Visual Consistency', () => {
  beforeEach(() => {
    cy.visit('/');
    cy.loginToDemo();
  });

  it('should maintain visual consistency across viewports', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Cross-Viewport Consistency',
      batchName: 'Cross-Browser Tests',
    });

    // Desktop
    cy.viewport(1280, 720);
    cy.eyesCheckWindow('Desktop 1280x720');

    // Tablet
    cy.viewport(768, 1024);
    cy.eyesCheckWindow('Tablet 768x1024');

    // Mobile
    cy.viewport(375, 667);
    cy.eyesCheckWindow('Mobile 375x667');

    cy.eyesClose();
  });

  it('should detect layout shifts on inventory page', () => {
    cy.eyesOpen({
      appName: 'SauceDemo Visual Tests',
      testName: 'Layout Shift Detection',
      batchName: 'Cross-Browser Tests',
      matchLevel: 'Layout',
    });

    cy.eyesCheckWindow('Inventory Page - Layout Check');

    // Interact with elements
    cy.get('[data-test="product-sort-container"]').select('price.desc');
    cy.eyesCheckWindow('After Sort - Layout Check');

    cy.eyesClose();
  });
});
