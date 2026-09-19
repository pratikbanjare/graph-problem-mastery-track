Feature: GraphCycleDetector

  Scenario: Graph with one cycle is detected as cyclic
    Given a graph whose vertex count is 3
    And edges
      | from | to |
      | 1    | 2 |
      | 2    | 3 |
      | 3    | 1 |
    When I check whether the graph has a cycle
    Then the result should be true

  Scenario: Graph without any cycle is detected as acyclic
    Given a graph whose vertex count is 4
    And edges
      | from | to |
      | 1    | 2 |
      | 2    | 3 |
      | 3    | 4 |
    When I check whether the graph has a cycle
    Then the result should be false

  Scenario: Graph with a self loop is detected as cyclic
    Given a graph whose vertex count is 2
    And edges
      | from | to |
      | 1    | 1 |
    When I check whether the graph has a cycle
    Then the result should be true

  Scenario: Disconnected graph with a cyclic component is detected as cyclic
    Given a graph whose vertex count is 5
    And edges
      | from | to |
      | 1    | 2 |
      | 2    | 3 |
      | 3    | 1 |
      | 4    | 5 |
    When I check whether the graph has a cycle
    Then the result should be true

  Scenario: Disconnected graph with no cycle is detected as acyclic
    Given a graph whose vertex count is 5
    And edges
      | from | to |
      | 1    | 2 |
      | 3    | 4 |
    When I check whether the graph has a cycle
    Then the result should be false