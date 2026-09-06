# Author: juanfranciscobumo@gmail.com

Feature: Find employees

  Scenario Outline: Find employees by id
    Given the user wants to find an employee
    When the user enters the id <id> of the employee
    Then the user should see the employee
      | id   | name   | role   |
      | <id> | <name> | <role> |

    Examples: Datos
      | id | name          | role    |
      | 1  | Bilbo Baggins | burglar |
