package com.employees.www.stepdefinitions;

import com.employees.www.models.EmployeeModel;
import com.employees.www.tasks.LookEmployee;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java8.En;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import java.util.List;
import java.util.Map;

import static com.employees.www.utils.Constants.ENDPOINT;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class EmployeesStepDefinitions implements En {

    @DataTableType
    public EmployeeModel employeeEntry(Map<String, String> entry) {
        return new EmployeeModel(
                Integer.parseInt(entry.get("id")),
                entry.get("name"),
                entry.get("role")
        );
    }

    public EmployeesStepDefinitions() {
        Before(() -> OnStage.setTheStage(new OnlineCast()));

        Given("the user wants to find an employee", () -> {
            theActorCalled("Juan").whoCan(CallAnApi.at(ENDPOINT));
        });

        When("the user enters the id {int} of the employee", (Integer id) -> {
            theActorInTheSpotlight().attemptsTo(LookEmployee.byId(id));
        });

        Then("the user should see the employee", (DataTable dataTable) -> {
            List<EmployeeModel> employees = dataTable.asList(EmployeeModel.class);

            theActorInTheSpotlight().should(
                    seeThatResponse("all the expected users should be returned",
                            response -> response.statusCode(200)
                                    .body("name", equalTo(employees.get(0).getName()))
                                    .body("id", equalTo(employees.get(0).getId()))
                                    .body("role", equalTo(employees.get(0).getRole())))
            );
        });
    }
}
