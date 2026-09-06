import { Given, When, Then } from '@cucumber/cucumber';
import { actorInTheSpotlight } from '@serenity-js/core';
import { SendARequest, VerifyApiResponse } from '../screenplay/tasks';
import { ResponseStatusCode, EmployeeData } from '../screenplay/questions';

Given('the API is available at {string}', async function (apiUrl: string) {
  // Store the API URL in the actor's notes
  await actorInTheSpotlight().attemptsTo(
    SendARequest.to(apiUrl)
  );
});

When('I send a GET request to {string}', async function (endpoint: string) {
  await actorInTheSpotlight().attemptsTo(
    SendARequest.get(endpoint)
  );
});

When('I send a POST request to {string} with body:', async function (endpoint: string, body: string) {
  await actorInTheSpotlight().attemptsTo(
    SendARequest.post(endpoint, JSON.parse(body))
  );
});

When('I send a PUT request to {string} with body:', async function (endpoint: string, body: string) {
  await actorInTheSpotlight().attemptsTo(
    SendARequest.put(endpoint, JSON.parse(body))
  );
});

When('I send a DELETE request to {string}', async function (endpoint: string) {
  await actorInTheSpotlight().attemptsTo(
    SendARequest.delete(endpoint)
  );
});

Given('I have an employee with ID {int}', async function (id: number) {
  // This step just sets up the context
  await actorInTheSpotlight().notes.set('employeeId', id);
});

Given('I create an employee with name {string} and role {string}', async function (name: string, role: string) {
  await actorInTheSpotlight().attemptsTo(
    SendARequest.post('/employees', { name, role })
  );
  const response = await ResponseStatusCode.value();
  if (response === 200) {
    const data = await EmployeeData.value();
    await actorInTheSpotlight().notes.set('employeeId', data.id);
  }
});

Then('the response status code should be {int}', async function (expectedStatus: number) {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.statusCode(expectedStatus)
  );
});

Then('the response should be a list of employees', async function () {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.isArray()
  );
});

Then('the response should contain employee data', async function () {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.hasProperty('id')
  );
});

Then('the employee name should be {string}', async function (expectedName: string) {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.hasPropertyValue('name', expectedName)
  );
});

Then('the employee role should be {string}', async function (expectedRole: string) {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.hasPropertyValue('role', expectedRole)
  );
});

Then('the employee ID should be {int}', async function (expectedId: number) {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.hasPropertyValue('id', expectedId)
  );
});

Then('the employee should be deleted', async function () {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.isEmpty()
  );
});

Then('the response should contain error message {string}', async function (expectedMessage: string) {
  await actorInTheSpotlight().attemptsTo(
    VerifyApiResponse.hasErrorMessage(expectedMessage)
  );
});
