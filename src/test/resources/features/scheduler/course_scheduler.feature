Feature: Course scheduling
  As a student
  I want to know whether all required courses can be completed
  So that I can plan my academic path

  Scenario: Courses in a valid dependency chain
    Given there are 4 courses
    And the prerequisite relationships are:
      | course | prerequisite |
      | 1      | 0            |
      | 2      | 1            |
      | 3      | 2            |
    When the scheduler checks whether the courses can be completed
    Then it should confirm that all courses can be finished
    And it should return a valid order of courses

  Scenario: Courses with no prerequisites
    Given there are 3 courses
    And there are no prerequisite relationships
    When the scheduler checks whether the courses can be completed
    Then it should confirm that all courses can be finished
    And it should return all courses in a valid order

  Scenario: Circular dependency prevents completion
    Given there are 2 courses
    And the prerequisite relationships are:
      | course | prerequisite |
      | 0      | 1            |
      | 1      | 0            |
    When the scheduler checks whether the courses can be completed
    Then it should report that the courses cannot be finished

  Scenario: A valid schedule exists for multiple independent paths
    Given there are 5 courses
    And the prerequisite relationships are:
      | course | prerequisite |
      | 1      | 0            |
      | 2      | 0            |
      | 3      | 1            |
      | 4      | 2            |
    When the scheduler checks whether the courses can be completed
    Then it should confirm that all courses can be finished
    And it should return a valid course order without violating prerequisites

  Scenario: Schedule is empty when completion is impossible
    Given there are 3 courses
    And the prerequisite relationships are:
      | course | prerequisite |
      | 0      | 1            |
      | 1      | 2            |
      | 2      | 0            |
    When the scheduler tries to produce a course schedule
    Then it should return no schedule