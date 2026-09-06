import { Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class CheckoutPage extends BasePage {
  private firstNameInput = '[data-test="firstName"]';
  private lastNameInput = '[data-test="lastName"]';
  private postalCodeInput = '[data-test="postalCode"]';
  private continueButton = '[data-test="continue"]';
  private finishButton = '[data-test="finish"]';
  private cancelButton = '[data-test="cancel"]';
  private completeHeader = '[data-test="complete-header"]';
  private backHomeButton = '[data-test="back-to-products"]';

  constructor(page: Page) {
    super(page);
  }

  async enterFirstName(firstName: string): Promise<void> {
    await this.fill(this.firstNameInput, firstName);
  }

  async enterLastName(lastName: string): Promise<void> {
    await this.fill(this.lastNameInput, lastName);
  }

  async enterPostalCode(postalCode: string): Promise<void> {
    await this.fill(this.postalCodeInput, postalCode);
  }

  async fillCheckoutInfo(firstName: string, lastName: string, postalCode: string): Promise<void> {
    await this.enterFirstName(firstName);
    await this.enterLastName(lastName);
    await this.enterPostalCode(postalCode);
  }

  async continueToOverview(): Promise<void> {
    await this.click(this.continueButton);
  }

  async finishCheckout(): Promise<void> {
    await this.click(this.finishButton);
    await this.page.waitForURL("**/checkout-complete.html");
  }

  async cancelCheckout(): Promise<void> {
    await this.click(this.cancelButton);
    await this.page.waitForURL("**/cart.html");
  }

  async getCompleteMessage(): Promise<string> {
    return (await this.page.locator(this.completeHeader).textContent()) ?? "";
  }

  async backToProducts(): Promise<void> {
    await this.click(this.backHomeButton);
    await this.page.waitForURL("**/inventory.html");
  }
}
