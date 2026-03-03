package com.tasklist.api.steps;

import com.tasklist.api.domain.Task;
import com.tasklist.api.domain.TaskPriority;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class DomainTaskSteps {

    private String titleInput;
    private Task currentTask;
    private Exception caughtException;

    @Given("I want to create a task with the title {string}")
    public void i_want_to_create_a_task_with_the_title(String title) {
        this.titleInput = title;
        this.caughtException = null;
    }

    @Given("I want to create a task with an empty title")
    public void i_want_to_create_a_task_with_an_empty_title() {
        this.titleInput = "";
        this.caughtException = null;
    }

    @Given("I have a task with the title {string}")
    public void i_have_a_task_with_the_title(String title) {
        this.currentTask = new Task(title);
        this.currentTask = new Task("id-123", title, false, null, TaskPriority.MEDIUM, null, null);
    }

    @When("I create the task")
    public void i_create_the_task() {
        try {
            this.currentTask = new Task(titleInput);
            this.currentTask.setId("id-123");
            this.currentTask.setDescription(null);
            this.currentTask.setPriority(TaskPriority.MEDIUM);
        } catch (Exception e) {
            this.caughtException = e;
        }
    }

    @When("I try to create the task")
    public void i_try_to_create_the_task() {
        i_create_the_task();
    }

    @When("I mark the task as completed")
    public void i_mark_the_task_as_completed() {
        this.currentTask.complete();
    }

    @Then("the task should have the title {string}")
    public void the_task_should_have_the_title(String expectedTitle) {
        Assertions.assertNotNull(currentTask);
        Assertions.assertEquals(expectedTitle, currentTask.getTitle());
    }

    @Then("the task should not be completed")
    public void the_task_should_not_be_completed() {
        Assertions.assertFalse(currentTask.isCompleted());
    }

    @Then("the task should be completed")
    public void the_task_should_be_completed() {
        Assertions.assertTrue(currentTask.isCompleted());
    }

    @Then("I should receive a validation error")
    public void i_should_receive_a_validation_error() {
        Assertions.assertNotNull(caughtException);
        Assertions.assertTrue(caughtException instanceof IllegalArgumentException);
    }
}
