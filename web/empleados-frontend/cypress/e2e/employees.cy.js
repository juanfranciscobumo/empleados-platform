describe('Employee CRUD Operations', () => {
  let createdEmployeeId;

  beforeEach(() => {
    cy.visit('/');
  });

  describe('Page Load', () => {
    it('should display the page title', () => {
      cy.get('header h1').should('contain', 'Gestión de Empleados');
    });

    it('should show API status indicator', () => {
      cy.get('.api-status').should('exist');
      cy.get('.status-dot').should('exist');
      cy.get('.status-text').should('exist');
    });

    it('should display the employee form', () => {
      cy.get('#employeeForm').should('exist');
      cy.get('#name').should('exist');
      cy.get('#role').should('exist');
      cy.get('#submitBtn').should('exist');
    });

    it('should display the employee table', () => {
      cy.get('#employeeTable').should('exist');
      cy.get('#employeeTable thead th').should('have.length', 4);
    });
  });

  describe('Create Employee', () => {
    it('should create a new employee', () => {
      const testName = `Test Employee ${Date.now()}`;
      const testRole = 'Tester';

      cy.get('#name').type(testName);
      cy.get('#role').type(testRole);
      cy.get('#submitBtn').click();

      cy.get('.toast').should('be.visible');
      cy.get('.toast').should('contain', 'Empleado creado');

      // Verify the employee appears in the table
      cy.get('#employeeTableBody').should('contain', testName);
    });

    it('should clear form after creation', () => {
      cy.get('#name').type('Test Employee');
      cy.get('#role').type('Tester');
      cy.get('#submitBtn').click();

      cy.get('#name').should('have.value', '');
      cy.get('#role').should('have.value', '');
    });
  });

  describe('Read Employees', () => {
    it('should display existing employees', () => {
      cy.get('#employeeTableBody tr').should('have.length.greaterThan', 0);
    });

    it('should show employee count in stats', () => {
      cy.get('#stats').should('contain', 'Total:');
    });
  });

  describe('Update Employee', () => {
    it('should enter edit mode when clicking edit button', () => {
      cy.get('#employeeTableBody tr').first().find('.btn-edit').click();

      cy.get('#formTitle').should('contain', 'Editar Empleado');
      cy.get('#submitBtn').should('contain', 'Actualizar');
      cy.get('#cancelBtn').should('be.visible');
    });

    it('should update an employee', () => {
      // First create an employee
      cy.createEmployee('Employee to Update', 'Tester').then((response) => {
        createdEmployeeId = response.body.id;
        cy.visit('/');
        
        // Find and click edit button
        cy.get('#employeeTableBody tr').contains('Employee to Update')
          .parent('tr').find('.btn-edit').click();

        // Update the name
        cy.get('#name').clear().type('Updated Employee');
        cy.get('#submitBtn').click();

        cy.get('.toast').should('contain', 'Empleado actualizado');
        cy.get('#employeeTableBody').should('contain', 'Updated Employee');

        // Cleanup
        cy.deleteEmployee(createdEmployeeId);
      });
    });

    it('should cancel edit mode', () => {
      cy.get('#employeeTableBody tr').first().find('.btn-edit').click();
      cy.get('#cancelBtn').click();

      cy.get('#formTitle').should('contain', 'Agregar Empleado');
      cy.get('#submitBtn').should('contain', 'Guardar');
      cy.get('#cancelBtn').should('not.be.visible');
    });
  });

  describe('Delete Employee', () => {
    it('should delete an employee', () => {
      // Create an employee to delete
      cy.createEmployee('Employee to Delete', 'Tester').then((response) => {
        createdEmployeeId = response.body.id;
        cy.visit('/');

        // Stub the confirm dialog
        cy.on('window:confirm', () => true);

        // Find and click delete button
        cy.get('#employeeTableBody tr').contains('Employee to Delete')
          .parent('tr').find('.btn-danger').click();

        cy.get('.toast').should('contain', 'Empleado eliminado');
        cy.get('#employeeTableBody').should('not.contain', 'Employee to Delete');
      });
    });
  });

  describe('Form Validation', () => {
    it('should not submit empty form', () => {
      cy.get('#submitBtn').click();
      cy.get('#name').then(($input) => {
        expect($input[0].validity.valid).to.be.false;
      });
    });

    it('should not submit without role', () => {
      cy.get('#name').type('Test Employee');
      cy.get('#submitBtn').click();
      cy.get('#role').then(($input) => {
        expect($input[0].validity.valid).to.be.false;
      });
    });
  });

  describe('API Status', () => {
    it('should show online status when API is available', () => {
      cy.get('.status-dot').should('have.class', 'online');
      cy.get('.status-text').should('contain', 'API Conectada');
    });
  });
});
