Feature: Task Management
  As a user
  I want to create and manage tasks
  So that I can keep track of what I need to do

  Scenario: Creating a new task successfully
    Given I want to create a task with the title "Buy Groceries"
    When I create the task
    Then the task should have the title "Buy Groceries"
    And the task should not be completed

  Scenario: Completing a task
    Given I have a task with the title "Pay Bills"
    When I mark the task as completed
    Then the task should be completed

  Scenario: Fails to create a task without a title
    Given I want to create a task with an empty title
    When I try to create the task
    Then I should receive a validation error
