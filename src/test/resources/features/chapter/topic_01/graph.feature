Feature: Manage graph vertices and edges

  Scenario: Create an undirected graph with empty adjacency lists
    Given an undirected graph with 4 vertices
    Then the graph should have 4 vertices
    And the graph type should be "UNDIRECTED"
    And every vertex should have no neighbors

  Scenario: Add an unweighted edge to an undirected graph
    Given an undirected graph with 4 vertices
    When I add an edge from 1 to 2
    Then the edge from 1 to 2 should exist
    And the edge from 2 to 1 should exist
    And the neighbors of vertex 1 should be "[2]"
    And the neighbors of vertex 2 should be "[1]"
    And the graph should contain 1 edges
    And the edge from 1 to 2 should have weight 0

  Scenario: Add a weighted edge
    Given an undirected graph with 4 vertices
    When I add an edge from 1 to 2 with weight 5
    Then the weighted neighbors of vertex 1 should be:
      | vertex | weight |
      | 2      | 5      |
    And the weighted neighbors of vertex 2 should be:
      | vertex | weight |
      | 1      | 5      |

  Scenario: Do not add the same edge twice
    Given an undirected graph with 3 vertices
    When I add an edge from 1 to 2
    And I add an edge from 1 to 2 with weight 7
    Then the graph should contain 1 edges
    And the edge from 1 to 2 should have weight 0

  Scenario: Preserve direction in a directed graph
    Given a directed graph with 3 vertices
    When I add an edge from 1 to 2 with weight 7
    Then the edge from 1 to 2 should exist
    And the edge from 2 to 1 should not exist
    And the neighbors of vertex 1 should be "[2]"
    And the neighbors of vertex 2 should be "[]"

  Scenario: Remove an edge
    Given an undirected graph with 3 vertices
    And I add an edge from 1 to 2
    When I remove the edge from 1 to 2
    Then the edge from 1 to 2 should not exist
    And the edge from 2 to 1 should not exist
    And the graph should contain 0 edges

  Scenario: Reject an invalid vertex count
    When I create a graph with 0 vertices
    Then a graph exception should be raised
    And the exception message should be "Invalid Vertices"

  Scenario: Reject a vertex outside the graph
    Given an undirected graph with 3 vertices
    When I try to add an edge from 0 to 1
    Then a graph exception should be raised
    And the exception message should contain "Vertex must be between 1 and 3"
