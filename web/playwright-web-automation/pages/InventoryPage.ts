import { Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class InventoryPage extends BasePage {
  private inventoryContainer = ".inventory_list";
  private inventoryItem = ".inventory_item";
  private itemName = ".inventory_item_name";
  private itemPrice = ".inventory_item_price";
  private cartLink = '[data-test="shopping-cart-link"]';
  private cartBadge = ".shopping_cart_badge";

  constructor(page: Page) {
    super(page);
  }

  async isInventoryDisplayed(): Promise<boolean> {
    return this.isVisible(this.inventoryContainer);
  }

  async getItemCount(): Promise<number> {
    return this.page.locator(this.inventoryItem).count();
  }

  async getItemNames(): Promise<string[]> {
    return this.page.locator(this.itemName).allTextContents();
  }

  async getItemPrices(): Promise<number[]> {
    const texts = await this.page.locator(this.itemPrice).allTextContents();
    return texts.map((t) => parseFloat(t.replace("$", "")));
  }

  async addToCart(index: number): Promise<void> {
    await this.page.locator(this.inventoryItem).nth(index).locator("button").click();
  }

  async removeFromCart(index: number): Promise<void> {
    await this.page.locator(this.inventoryItem).nth(index).locator("button").click();
  }

  async goToCart(): Promise<void> {
    await this.click(this.cartLink);
    await this.page.waitForURL("**/cart.html");
  }

  async getCartBadgeCount(): Promise<number> {
    const text = await this.page.locator(this.cartBadge).textContent();
    return text ? parseInt(text) : 0;
  }

  async sortBy(option: string): Promise<void> {
    await this.page.locator(".product_sort_container").selectOption(option);
  }
}
