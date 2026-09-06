// ***********************************************
// Custom commands for Employee CRUD tests
// ***********************************************

Cypress.Commands.add('createEmployee', (name, role) => {
  const apiUrl = Cypress.env('API_URL') || 'http://localhost:8081';
  
  return cy.request({
    method: 'POST',
    url: `${apiUrl}/employees`,
    body: { name, role },
    failOnStatusCode: false,
  });
});

Cypress.Commands.add('deleteEmployee', (id) => {
  const apiUrl = Cypress.env('API_URL') || 'http://localhost:8081';
  
  return cy.request({
    method: 'DELETE',
    url: `${apiUrl}/employees/${id}`,
    failOnStatusCode: false,
  });
});

Cypress.Commands.add('getEmployees', () => {
  const apiUrl = Cypress.env('API_URL') || 'http://localhost:8081';
  
  return cy.request({
    method: 'GET',
    url: `${apiUrl}/employees`,
  });
});
