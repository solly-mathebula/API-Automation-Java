Feature: Fetch cat facts

  Scenario: Positive response Cat facts
    Given I have the cats endpoint
    When I do a GET request
    Then I should get a status code of 200

  Scenario: Negative response Cat facts
    Given I have the cats endpoint
    When I do a GET request
    Then I should get a status code of 205