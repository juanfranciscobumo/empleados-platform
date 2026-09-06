import { test, expect } from "@playwright/test";
import AxeBuilder from "@axe-core/playwright";

test.describe("Accessibility Tests", () => {
  test.beforeEach(async ({ page }) => {
    await page.goto("/");
  });

  test("Login page should have no critical accessibility violations", async ({ page }) => {
    const accessibilityScanResults = await new AxeBuilder({ page })
      .exclude(".footer")
      .withTags(["critical", "serious"])
      .analyze();
    expect(accessibilityScanResults.violations).toEqual([]);
  });

  test("Login form should be keyboard navigable", async ({ page }) => {
    await page.locator('[data-test="username"]').click();
    await expect(page.locator('[data-test="username"]')).toBeFocused();

    await page.keyboard.press("Tab");
    await expect(page.locator('[data-test="password"]')).toBeFocused();

    await page.keyboard.press("Tab");
    await expect(page.locator('[data-test="login-button"]')).toBeFocused();
  });

  test("Error messages should be visible after failed login", async ({ page }) => {
    await page.locator('[data-test="login-button"]').click();
    await expect(page.locator('[data-test="error"]')).toBeVisible();
    await expect(page.locator('[data-test="error"]')).toContainText(
      "Username is required"
    );
  });

  test("Page should have proper heading hierarchy", async ({ page }) => {
    const h1Count = await page.locator("h1").count();
    expect(h1Count).toBeLessThanOrEqual(1);
  });

  test("Page should have lang attribute", async ({ page }) => {
    const html = page.locator("html");
    await expect(html).toHaveAttribute("lang", /.+/);
  });

  test("Login inputs should have accessible names", async ({ page }) => {
    await expect(page.locator('[data-test="username"]')).toHaveAttribute(
      "placeholder",
      /.+/
    );
    await expect(page.locator('[data-test="password"]')).toHaveAttribute(
      "placeholder",
      /.+/
    );
  });
});
