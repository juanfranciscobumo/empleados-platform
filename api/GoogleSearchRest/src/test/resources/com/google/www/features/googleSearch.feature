#Author: juanfranciscobumo@gmail.com

Feature: Rest service automation
  I want automatize the execution and response of a rest service

  Background:
    Given 'Juan' enter the endpoint of the rest service

  Scenario Outline: Request
    When enter the dates
      | key   | cx   | q   |
      | <key> | <cx> | <q> |
    Then he user will verify the response message and code <statusCode>
    And should see the result of the search the title '<title>' and '<searchTerms>'

    Examples:
      | key                                     | cx                                | q        | title                           | searchTerms | statusCode |
      | ${GOOGLE_API_KEY} | 017576662512468239146:omuauf_lfve | facebook | Google Custom Search - facebook | facebook    | 200        |
      | ${GOOGLE_API_KEY} | 017576662512468239146:omuauf_lfve | twiter   | Google Custom Search - twiter   | twiter      | 200        |
