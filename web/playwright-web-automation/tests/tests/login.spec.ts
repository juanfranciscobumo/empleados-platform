import { test, expect, testData } from "../../fixtures/test-data";

test.describe("Login Tests", () => {
  test.beforeEach(async ({ loginPage }) => {
    await loginPage.visit();
  });

  test("debería hacer login exitoso con usuario estándar", async ({ loginPage }) => {
    await loginPage.login(
      testData.users.standard.username,
      testData.users.standard.password
    );
    expect(testData.urls.inventory).toContain("inventory");
  });

  test("debería mostrar error con usuario bloqueado", async ({ loginPage }) => {
    await loginPage.loginExpectingError(
      testData.users.lockedOut.username,
      testData.users.lockedOut.password
    );
    const error = await loginPage.getErrorMessage();
    expect(error).toContain("Sorry, this user has been locked out");
  });

  test("debería mostrar error con credenciales inválidas", async ({ loginPage }) => {
    await loginPage.loginExpectingError(
      testData.users.invalid.username,
      testData.users.invalid.password
    );
    const error = await loginPage.getErrorMessage();
    expect(error).toContain("Username and password do not match");
  });

  test("debería mostrar error con contraseña vacía", async ({ loginPage }) => {
    await loginPage.enterUsername("standard_user");
    await loginPage.clickLogin();
    const error = await loginPage.getErrorMessage();
    expect(error).toContain("Password is required");
  });

  test("debería mostrar error con usuario vacío", async ({ loginPage }) => {
    await loginPage.enterPassword("secret_sauce");
    await loginPage.clickLogin();
    const error = await loginPage.getErrorMessage();
    expect(error).toContain("Username is required");
  });
});
