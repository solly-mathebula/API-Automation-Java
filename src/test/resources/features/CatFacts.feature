Feature: Fetch cat facts

  Scenario: Cat facts
    Given I have the cats endpoint
    When I do a GET request
    Then I should get a status code of 200