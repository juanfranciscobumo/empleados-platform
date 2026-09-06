import { Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class CartPage extends BasePage {
  private cartList = ".cart_list";
  private cartItem = ".cart_item";
  private itemName = ".inventory_item_name";
  private itemPrice = ".inventory_item_price";
  private removeButton = ".cart_button";
  private checkoutButton = '[data-test="checkout"]';
  private continueShoppingButton = '[data-test="continue-shopping"]';

  constructor(page: Page) {
    super(page);
  }

  async isCartDisplayed(): Promise<boolean> {
    return this.isVisible(this.cartList);
  }

  async getItemCount(): Promise<number> {
    return this.page.locator(this.cartItem).count();
  }

  async getItemNames(): Promise<string[]> {
    return this.page.locator(this.itemName).allTextContents();
  }

  async removeItem(index: number): Promise<void> {
    await this.page.locator(this.cartItem).nth(index).locator(this.removeButton).click();
  }

  async checkout(): Promise<void> {
    await this.click(this.checkoutButton);
    await this.page.waitForURL("**/checkout-step-one.html");
  }

  async continueShopping(): Promise<void> {
    await this.click(this.continueShoppingButton);
    await this.page.waitForURL("**/inventory.html");
  }
}
