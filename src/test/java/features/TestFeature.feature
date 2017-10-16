Feature: Test feature
  User should have ability to use purchases grid

  Scenario: Find purchases summ

    Given I have opened purchases page

    When  I filled filter by values
      | startPriceFrom   | publishDateFrom | publishDateTo  |
      | 0                | 20.09.2017      | 21.09.2017     |

    When I click Search button and get new results

    Then I can evaluate summ of all purchases