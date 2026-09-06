import { test, expect, testData } from "../../fixtures/test-data";

interface User {
  id: number;
  name: string;
  username: string;
  email: string;
  phone: string;
}

test.describe("Users API Tests", () => {
  test("GET - Debería obtener todos los usuarios", async ({ apiHelpers }) => {
    const { status, data } = await apiHelpers.get<User[]>(testData.endpoints.users);

    expect(status).toBe(200);
    expect(data).toBeInstanceOf(Array);
    expect(data.length).toBe(10);
  });

  test("GET - Debería obtener un usuario por ID", async ({ apiHelpers }) => {
    const { status, data } = await apiHelpers.get<User>(`${testData.endpoints.users}/1`);

    expect(status).toBe(200);
    expect(data).toHaveProperty("id", 1);
    expect(data).toHaveProperty("name");
    expect(data).toHaveProperty("email");
  });

  test("GET - Debería obtener posts de un usuario", async ({ request }) => {
    const response = await request.get(`${testData.endpoints.users}/1/posts`);
    const data = await response.json();

    expect(response.status()).toBe(200);
    expect(data).toBeInstanceOf(Array);
  });
});
