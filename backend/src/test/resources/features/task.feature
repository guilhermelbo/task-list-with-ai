Feature: Task Management
  As a user
  I want to create and manage tasks
  So that I can keep track of what I need to do

  # Domain scenarios
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

  # REST API scenarios
  Scenario: Create a task via REST API
    Given the REST API is available
    When I POST to "/api/v1/tasks" with title "REST Task"
    Then the response status should be 201
    And the response should contain a task with title "REST Task"

  Scenario: Get a task by ID via REST API
    Given a task exists with title "Existing Task"
    When I GET "/api/v1/tasks/{id}"
    Then the response status should be 200
    And the response should contain title "Existing Task"

  Scenario: Get a non-existent task returns 404
    When I GET "/api/v1/tasks/non-existent-id"
    Then the response status should be 404

  Scenario: Delete a task via REST API returns 204
    Given a task exists with title "Task to Delete"
    When I DELETE "/api/v1/tasks/{id}"
    Then the response status should be 204

  Scenario: Delete a non-existent task returns 404
    When I DELETE "/api/v1/tasks/non-existent-id"
    Then the response status should be 404
