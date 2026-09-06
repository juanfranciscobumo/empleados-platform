@frontend
Feature: Employee Frontend CRUD Operations
  As a user of the Employee Management System
  I want to manage employees through the web interface
  So that I can easily add, view, update, and delete employees

  Background:
    Given I open the Employee Management page

  Scenario: Page loads correctly
    Then the page title should be "Gestión de Empleados"
    And the API status indicator should be visible
    And the employee form should be displayed
    And the employee table should be displayed

  Scenario: Display existing employees
    Then the employee table should contain at least one employee
    And the stats should show the total number of employees

  Scenario: Create a new employee
    When I fill in the name field with "Test Employee"
    And I fill in the role field with "Tester"
    And I click the save button
    Then a success toast should appear with message "Empleado creado"
    And the employee "Test Employee" should appear in the table
    And the form should be cleared

  Scenario: Edit an employee
    Given there is an employee named "Employee to Edit"
    When I click the edit button for "Employee to Edit"
    Then the form title should change to "Editar Empleado"
    And the save button text should change to "Actualizar"
    When I clear the name field and type "Updated Employee"
    And I click the save button
    Then a success toast should appear with message "Empleado actualizado"
    And the employee "Updated Employee" should appear in the table

  Scenario: Delete an employee
    Given there is an employee named "Employee to Delete"
    When I click the delete button for "Employee to Delete"
    And I confirm the deletion
    Then a success toast should appear with message "Empleado eliminado"
    And the employee "Employee to Delete" should not appear in the table

  Scenario: Cancel edit mode
    Given there is an employee named "Employee to Edit"
    When I click the edit button for "Employee to Edit"
    And I click the cancel button
    Then the form title should change to "Agregar Empleado"
    And the save button text should change to "Guardar"
    And the form should be cleared

  Scenario Outline: Form validation
    When I fill in the name field with "<name>"
    And I fill in the role field with "<role>"
    And I click the save button
    Then the form should not submit

    Examples:
      | name  | role      |
      |       | Developer |
      | John  |           |

  Scenario: API status shows online
    Then the API status dot should have class "online"
    And the API status text should contain "API Conectada"
