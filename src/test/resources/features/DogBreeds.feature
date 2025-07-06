Feature: Fetch Dog Breeds

  Scenario: Dog breeds
    Given I have the cats endpoint
    When I do a GET request
    Then I should get a status code of 200