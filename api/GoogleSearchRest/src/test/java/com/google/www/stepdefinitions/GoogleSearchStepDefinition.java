package com.google.www.stepdefinitions;

import com.google.www.exceptions.ManejoDeExceptions;
import com.google.www.questions.TheResponseDate;
import com.google.www.tasks.RealiceSearch;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static com.google.www.exceptions.ManejoDeExceptions.DATA;
import static com.google.www.exceptions.ManejoDeExceptions.RESPONSE_CODE;
import static com.google.www.utils.Constantes.ENDPOINT;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

public class GoogleSearchStepDefinition implements En {

    public GoogleSearchStepDefinition() {

        Before(() -> OnStage.setTheStage(new OnlineCast()));

        Given("{string} enter the endpoint of the rest service", (String actor) ->
                theActorCalled(actor).whoCan(CallAnApi.at(ENDPOINT)));

        When("enter the dates", (DataTable data) ->
                theActorInTheSpotlight().attemptsTo(RealiceSearch
                        .inGoogle(resolveEnvironmentVariables(data.transpose().asMap(String.class, String.class)))));

        Then("he user will verify the response message and code {int}", (Integer code) ->
                theActorInTheSpotlight().should(seeThatResponse(response -> response.statusCode(code))
                        .orComplainWith(ManejoDeExceptions.class, RESPONSE_CODE)));

        Then("should see the result of the search the title {string} and {string}", (String title, String searchTerms) ->
                theActorInTheSpotlight().should(seeThat(TheResponseDate.isCorrect(title, searchTerms), equalTo(true))
                        .orComplainWith(ManejoDeExceptions.class, DATA)));
    }

    private Map<String, String> resolveEnvironmentVariables(Map<String, String> parameters) {
        return parameters.entrySet().stream().collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().replace("${GOOGLE_API_KEY}", requiredEnvironmentVariable("GOOGLE_API_KEY"))));
    }

    private String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
