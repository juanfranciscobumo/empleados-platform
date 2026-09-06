@api
Feature: Employee API CRUD Operations
  As a user of the Employee API
  I want to perform CRUD operations on employees
  So that I can manage employee data

  Background:
    Given the API is available at "http://localhost:8081"

  Scenario: Get all employees
    When I send a GET request to "/employees"
    Then the response status code should be 200
    And the response should be a list of employees

  Scenario: Create a new employee
    When I send a POST request to "/employees" with body:
      """
      {
        "name": "John Doe",
        "role": "Developer"
      }
      """
    Then the response status code should be 200
    And the response should contain employee data
    And the employee name should be "John Doe"
    And the employee role should be "Developer"

  Scenario: Get employee by ID
    Given I have an employee with ID 1
    When I send a GET request to "/employees/1"
    Then the response status code should be 200
    And the response should contain employee data
    And the employee ID should be 1

  Scenario: Update an employee
    Given I have an employee with ID 1
    When I send a PUT request to "/employees/1" with body:
      """
      {
        "name": "Jane Smith",
        "role": "Senior Developer"
      }
      """
    Then the response status code should be 200
    And the employee name should be "Jane Smith"
    And the employee role should be "Senior Developer"

  Scenario: Delete an employee
    Given I create an employee with name "To Delete" and role "Tester"
    When I send a DELETE request to "/employees/{id}"
    Then the response status code should be 200
    And the employee should be deleted

  Scenario Outline: Validate employee data
    When I send a POST request to "/employees" with body:
      """
      {
        "name": "<name>",
        "role": "<role>"
      }
      """
    Then the response status code should be <status>
    And the response should contain error message "<error>"

    Examples:
      | name  | role      | status | error                    |
      |       | Developer | 400    | Name is required         |
      | John  |           | 400    | Role is required         |
      | John  | Developer | 409    | Employee already exists  |
