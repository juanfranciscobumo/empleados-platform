import { LoginPage } from "../pages/LoginPage";
import { InventoryPage } from "../pages/InventoryPage";

describe("Login Tests", () => {
  const loginPage = new LoginPage();
  const inventoryPage = new InventoryPage();

  beforeEach(() => {
    loginPage.visit();
  });

  it("debería hacer login exitoso con usuario estándar", () => {
    loginPage.login("standard_user", "secret_sauce");
    inventoryPage.isInventoryDisplayed().should("be.true");
  });

  it("debería mostrar error con usuario bloqueado", () => {
    loginPage.login("locked_out_user", "secret_sauce");
    loginPage.getErrorMessage().should("contain", "Sorry, this user has been locked out");
  });

  it("debería mostrar error con credenciales inválidas", () => {
    loginPage.login("invalid_user", "wrong_password");
    loginPage.getErrorMessage().should("contain", "Username and password do not match");
  });

  it("debería mostrar error con contraseña vacía", () => {
    loginPage.enterUsername("standard_user");
    loginPage.clickLogin();
    loginPage.getErrorMessage().should("contain", "Password is required");
  });

  it("debería mostrar error con usuario vacío", () => {
    loginPage.enterPassword("secret_sauce");
    loginPage.clickLogin();
    loginPage.getErrorMessage().should("contain", "Username is required");
  });
});
