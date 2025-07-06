Feature: Fetch Dog Breeds

  Scenario: Positive Dog breeds
    Given I have the dogs endpoint
    When I do a GET request
    Then I should get a status code of 200

  Scenario: Negative Dog breeds
    Given I have the dogs endpoint
    When I do a GET request
    Then I should get a status code of 230

