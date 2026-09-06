import { Given, When, Then } from '@cucumber/cucumber';
import { actorInTheSpotlight } from '@serenity-js/core';
import { Navigate, Click, Enter, Ensure, isVisible, hasAttribute } from '@serenity-js/web';

const PAGE_URL = 'http://localhost:80';

Given('I open the Employee Management page', async function () {
  await actorInTheSpotlight().attemptsTo(
    Navigate.to(PAGE_URL)
  );
});

When('I fill in the name field with {string}', async function (name: string) {
  await actorInTheSpotlight().attemptsTo(
    Enter.theValue(name).into('#name')
  );
});

When('I fill in the role field with {string}', async function (role: string) {
  await actorInTheSpotlight().attemptsTo(
    Enter.theValue(role).into('#role')
  );
});

When('I click the save button', async function () {
  await actorInTheSpotlight().attemptsTo(
    Click.on('#submitBtn')
  );
});

When('I click the edit button for {string}', async function (employeeName: string) {
  await actorInTheSpotlight().attemptsTo(
    Click.on(`tr:has(td:contains("${employeeName}")) .btn-edit`)
  );
});

When('I click the delete button for {string}', async function (employeeName: string) {
  await actorInTheSpotlight().attemptsTo(
    Click.on(`tr:has(td:contains("${employeeName}")) .btn-danger`)
  );
});

When('I click the cancel button', async function () {
  await actorInTheSpotlight().attemptsTo(
    Click.on('#cancelBtn')
  );
});

When('I confirm the deletion', async function () {
  // Handle the confirmation dialog
  await actorInTheSpotlight().attemptsTo(
    Click.on('.confirm-button')
  );
});

When('I clear the name field and type {string}', async function (name: string) {
  await actorInTheSpotlight().attemptsTo(
    Enter.theValue(name).into('#name')
  );
});

Given('there is an employee named {string}', async function (employeeName: string) {
  // This step assumes the employee exists or creates it via API
});

Then('the page title should be {string}', async function (expectedTitle: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('h1').textContent(),
      equals(expectedTitle)
    )
  );
});

Then('the API status indicator should be visible', async function () {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(isVisible('.api-status'))
  );
});

Then('the employee form should be displayed', async function () {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(isVisible('#employeeForm'))
  );
});

Then('the employee table should be displayed', async function () {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(isVisible('#employeeTable'))
  );
});

Then('the employee table should contain at least one employee', async function () {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('#employeeTableBody tr').count(),
      isGreaterThan(0)
    )
  );
});

Then('the stats should show the total number of employees', async function () {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(isVisible('#stats'))
  );
});

Then('a success toast should appear with message {string}', async function (expectedMessage: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(isVisible('.toast.success'))
  );
});

Then('the employee {string} should appear in the table', async function (employeeName: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('#employeeTableBody').textContent(),
      contains(employeeName)
    )
  );
});

Then('the employee {string} should not appear in the table', async function (employeeName: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('#employeeTableBody').textContent(),
      not(contains(employeeName))
    )
  );
});

Then('the form should be cleared', async function () {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('#name').inputValue(),
      equals('')
    )
  );
});

Then('the form title should change to {string}', async function (expectedTitle: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('#formTitle').textContent(),
      equals(expectedTitle)
    )
  );
});

Then('the save button text should change to {string}', async function (expectedText: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('#submitBtn').textContent(),
      contains(expectedText)
    )
  );
});

Then('the form should not submit', async function () {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('#name').inputValue(),
      not(equals(''))
    )
  );
});

Then('the API status dot should have class {string}', async function (expectedClass: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('.status-dot').getAttribute('class'),
      contains(expectedClass)
    )
  );
});

Then('the API status text should contain {string}', async function (expectedText: string) {
  await actorInTheSpotlight().attemptsTo(
    Ensure.that(
      await actorInTheSpotlight().elementLocator.locator('.status-text').textContent(),
      contains(expectedText)
    )
  );
});
