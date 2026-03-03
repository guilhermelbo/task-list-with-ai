package com.tasklist.api.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestTaskSteps {

    @Autowired
    private MockMvc mockMvc;

    private ResultActions lastResult;
    private String lastCreatedId;

    @Given("the REST API is available")
    public void theRestApiIsAvailable() {
        // Spring context is already started via SpringIntegrationTest
    }

    @Given("a task exists with title {string}")
    public void aTaskExistsWithTitle(String title) throws Exception {
        String body = "{\"title\": \"" + title + "\"}";
        MvcResult result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        lastCreatedId = JsonPath.read(responseBody, "$.id");
    }

    @When("I POST to {string} with title {string}")
    public void postToEndpointWithTitle(String path, String title) throws Exception {
        String body = "{\"title\": \"" + title + "\"}";
        lastResult = mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        MvcResult result = lastResult.andReturn();
        String responseBody = result.getResponse().getContentAsString();
        if (result.getResponse().getStatus() == 201) {
            lastCreatedId = JsonPath.read(responseBody, "$.id");
        }
    }

    @When("I GET {string}")
    public void getEndpoint(String path) throws Exception {
        String resolvedPath = resolvePath(path);
        lastResult = mockMvc.perform(get(resolvedPath)
                .accept(MediaType.APPLICATION_JSON));
    }

    @When("I DELETE {string}")
    public void deleteEndpoint(String path) throws Exception {
        String resolvedPath = resolvePath(path);
        lastResult = mockMvc.perform(delete(resolvedPath));
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int status) throws Exception {
        lastResult.andExpect(status().is(status));
    }

    @Then("the response should contain a task with title {string}")
    public void responseContainsTaskWithTitle(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Then("the response should contain title {string}")
    public void responseShouldContainTitle(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$.title").value(expectedTitle));
    }

    private String resolvePath(String path) {
        if (lastCreatedId != null) {
            return path.replace("{id}", lastCreatedId);
        }
        return path;
    }
}
