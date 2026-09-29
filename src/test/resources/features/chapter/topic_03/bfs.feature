Feature: Find shortest paths with breadth-first search
  BFS explores the graph level by level.
  Because every edge has equal weight, the first time BFS reaches a vertex
  it has used the fewest possible edges to get there.
  That makes BFS a reliable way to compute both the shortest path
  and the shortest distance in an unweighted graph.

  Scenario: Choose the path with the fewest edges across branching neighbors
    Given a path graph with 6 vertices
    And the path graph has an edge between 1 and 2
    And the path graph has an edge between 1 and 3
    And the path graph has an edge between 2 and 4
    And the path graph has an edge between 3 and 5
    And the path graph has an edge between 4 and 6
    When I find the shortest BFS path from 1 to 6
    Then the BFS path should be
      | 1 | 2 | 4 | 6 |
    And the BFS path should form a valid traversal from 1 to 6
    And the BFS path should use 3 edges

  Scenario: Report the number of edges in the shortest path
    Given a path graph with 6 vertices
    And the path graph has an edge between 1 and 2
    And the path graph has an edge between 2 and 3
    And the path graph has an edge between 3 and 4
    And the path graph has an edge between 4 and 5
    And the path graph has an edge between 5 and 6
    When I measure the BFS shortest distance from 1 to 6
    Then the BFS distance should be 5

  Scenario: Return an empty path when the target is unreachable
    Given a path graph with 5 vertices
    And the path graph has an edge between 1 and 2
    And the path graph has an edge between 2 and 3
    And the path graph has an edge between 4 and 5
    When I find the shortest BFS path from 1 to 5
    Then the BFS path should be empty

  Scenario: Return minus one distance when the target is unreachable
    Given a path graph with 5 vertices
    And the path graph has an edge between 1 and 2
    And the path graph has an edge between 2 and 3
    And the path graph has an edge between 4 and 5
    When I measure the BFS shortest distance from 1 to 5
    Then the BFS distance should be -1

  Scenario: Return the source itself when source and target are the same
    Given a path graph with 3 vertices
    And the path graph has an edge between 1 and 2
    And the path graph has an edge between 2 and 3
    When I find the shortest BFS path from 2 to 2
    Then the BFS path should be
      | 2 |
    And the BFS path should form a valid traversal from 2 to 2
    And the BFS path should use 0 edges

  Scenario: Return zero distance when source and target are the same
    Given a path graph with 3 vertices
    And the path graph has an edge between 1 and 2
    And the path graph has an edge between 2 and 3
    When I measure the BFS shortest distance from 2 to 2
    Then the BFS distance should be 0
