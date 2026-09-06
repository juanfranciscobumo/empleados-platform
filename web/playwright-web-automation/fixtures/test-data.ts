import { test as base, Page } from "@playwright/test";

export interface UserData {
  username: string;
  password: string;
}

export const testData = {
  users: {
    standard: {
      username: "standard_user",
      password: "secret_sauce",
    } as UserData,
    problem: {
      username: "problem_user",
      password: "secret_sauce",
    } as UserData,
    lockedOut: {
      username: "locked_out_user",
      password: "secret_sauce",
    } as UserData,
    invalid: {
      username: "invalid_user",
      password: "wrong_password",
    } as UserData,
  },
  products: {
    names: [
      "Sauce Labs Backpack",
      "Sauce Labs Bike Light",
      "Sauce Labs Bolt T-Shirt",
      "Sauce Labs Fleece Jacket",
      "Sauce Labs Onesie",
      "Test.allTheThings() T-Shirt (Red)",
    ],
    prices: [29.99, 9.99, 15.99, 49.99, 7.99, 15.99],
  },
  urls: {
    inventory: "/inventory.html",
    cart: "/cart.html",
    checkout: "/checkout-step-one.html",
    checkoutComplete: "/checkout-complete.html",
  },
  checkout: {
    firstName: "Juan",
    lastName: "Perez",
    postalCode: "12345",
  },
};

type TestFixtures = {
  loginPage: LoginPageFixture;
  inventoryPage: InventoryPageFixture;
  cartPage: CartPageFixture;
  checkoutPage: CheckoutPageFixture;
};

export interface LoginPageFixture {
  visit: () => Promise<void>;
  login: (username: string, password: string) => Promise<void>;
  loginExpectingError: (username: string, password: string) => Promise<void>;
  enterUsername: (username: string) => Promise<void>;
  enterPassword: (password: string) => Promise<void>;
  clickLogin: () => Promise<void>;
  getErrorMessage: () => Promise<string>;
}

export interface InventoryPageFixture {
  isInventoryDisplayed: () => Promise<boolean>;
  getItemCount: () => Promise<number>;
  getItemNames: () => Promise<string[]>;
  getItemPrices: () => Promise<number[]>;
  addToCart: (index: number) => Promise<void>;
  removeFromCart: (index: number) => Promise<void>;
  goToCart: () => Promise<void>;
  getCartBadgeCount: () => Promise<number>;
  sortBy: (option: string) => Promise<void>;
}

export interface CartPageFixture {
  isCartDisplayed: () => Promise<boolean>;
  getItemCount: () => Promise<number>;
  getItemNames: () => Promise<string[]>;
  removeItem: (index: number) => Promise<void>;
  checkout: () => Promise<void>;
  continueShopping: () => Promise<void>;
}

export interface CheckoutPageFixture {
  fillCheckoutInfo: (firstName: string, lastName: string, postalCode: string) => Promise<void>;
  continueToOverview: () => Promise<void>;
  finishCheckout: () => Promise<void>;
  cancelCheckout: () => Promise<void>;
  getCompleteMessage: () => Promise<string>;
  getErrorMessage: () => Promise<string>;
  backToProducts: () => Promise<void>;
}

export const test = base.extend<TestFixtures>({
  loginPage: async ({ page }, use) => {
    const loginPage: LoginPageFixture = {
      visit: async () => {
        await page.goto("/");
      },
      login: async (username: string, password: string) => {
        await page.goto("/");
        await page.locator('[data-test="username"]').fill(username);
        await page.locator('[data-test="password"]').fill(password);
        await page.locator('[data-test="login-button"]').click();
        await page.waitForURL("**/inventory.html");
      },
      loginExpectingError: async (username: string, password: string) => {
        await page.goto("/");
        await page.locator('[data-test="username"]').fill(username);
        await page.locator('[data-test="password"]').fill(password);
        await page.locator('[data-test="login-button"]').click();
        await page.locator('[data-test="error"]').waitFor({ state: "visible" });
      },
      enterUsername: async (username: string) => {
        await page.locator('[data-test="username"]').fill(username);
      },
      enterPassword: async (password: string) => {
        await page.locator('[data-test="password"]').fill(password);
      },
      clickLogin: async () => {
        await page.locator('[data-test="login-button"]').click();
      },
      getErrorMessage: async () => {
        return (await page.locator('[data-test="error"]').textContent()) ?? "";
      },
    };
    await use(loginPage);
  },

  inventoryPage: async ({ page }, use) => {
    const inventoryPage: InventoryPageFixture = {
      isInventoryDisplayed: async () => {
        return page.locator(".inventory_list").isVisible();
      },
      getItemCount: async () => {
        return page.locator(".inventory_item").count();
      },
      getItemNames: async () => {
        return page.locator(".inventory_item_name").allTextContents();
      },
      getItemPrices: async () => {
        const texts = await page.locator(".inventory_item_price").allTextContents();
        return texts.map((t) => parseFloat(t.replace("$", "")));
      },
      addToCart: async (index: number) => {
        await page.locator(".inventory_item").nth(index).locator("button").click();
      },
      removeFromCart: async (index: number) => {
        await page.locator(".inventory_item").nth(index).locator("button").click();
      },
      goToCart: async () => {
        await page.locator('[data-test="shopping-cart-link"]').click();
        await page.waitForURL("**/cart.html");
      },
      getCartBadgeCount: async () => {
        const text = await page.locator(".shopping_cart_badge").textContent();
        return text ? parseInt(text) : 0;
      },
      sortBy: async (option: string) => {
        await page.locator(".product_sort_container").selectOption(option);
      },
    };
    await use(inventoryPage);
  },

  cartPage: async ({ page }, use) => {
    const cartPage: CartPageFixture = {
      isCartDisplayed: async () => {
        return page.locator(".cart_list").isVisible();
      },
      getItemCount: async () => {
        return page.locator(".cart_item").count();
      },
      getItemNames: async () => {
        return page.locator(".inventory_item_name").allTextContents();
      },
      removeItem: async (index: number) => {
        await page.locator(".cart_item").nth(index).locator(".cart_button").click();
      },
      checkout: async () => {
        await page.locator('[data-test="checkout"]').click();
        await page.waitForURL("**/checkout-step-one.html");
      },
      continueShopping: async () => {
        await page.locator('[data-test="continue-shopping"]').click();
        await page.waitForURL("**/inventory.html");
      },
    };
    await use(cartPage);
  },

  checkoutPage: async ({ page }, use) => {
    const checkoutPage: CheckoutPageFixture = {
      fillCheckoutInfo: async (firstName: string, lastName: string, postalCode: string) => {
        await page.locator('[data-test="firstName"]').fill(firstName);
        await page.locator('[data-test="lastName"]').fill(lastName);
        await page.locator('[data-test="postalCode"]').fill(postalCode);
      },
      continueToOverview: async () => {
        await page.locator('[data-test="continue"]').click();
      },
      finishCheckout: async () => {
        await page.locator('[data-test="finish"]').click();
        await page.waitForURL("**/checkout-complete.html");
      },
      cancelCheckout: async () => {
        await page.locator('[data-test="cancel"]').click();
        await page.waitForURL("**/cart.html");
      },
      getCompleteMessage: async () => {
        return (await page.locator('[data-test="complete-header"]').textContent()) ?? "";
      },
      getErrorMessage: async () => {
        return (await page.locator('[data-test="error"]').textContent()) ?? "";
      },
      backToProducts: async () => {
        await page.locator('[data-test="back-to-products"]').click();
        await page.waitForURL("**/inventory.html");
      },
    };
    await use(checkoutPage);
  },
});

export { expect } from "@playwright/test";
