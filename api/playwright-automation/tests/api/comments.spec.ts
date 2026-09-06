import { test, expect, testData } from "../../fixtures/test-data";

interface Comment {
  id: number;
  postId: number;
  name: string;
  email: string;
  body: string;
}

test.describe("Comments API Tests", () => {
  test("GET - Debería obtener todos los comentarios", async ({ apiHelpers }) => {
    const { status, data } = await apiHelpers.get<Comment[]>(testData.endpoints.comments);

    expect(status).toBe(200);
    expect(data).toBeInstanceOf(Array);
    expect(data.length).toBeGreaterThan(0);
  });

  test("GET - Debería filtrar comentarios por postId", async ({ apiHelpers }) => {
    const { status, data } = await apiHelpers.get<Comment[]>(
      `${testData.endpoints.comments}?postId=1`
    );

    expect(status).toBe(200);
    expect(data).toBeInstanceOf(Array);
    data.forEach((comment) => {
      expect(comment.postId).toBe(1);
    });
  });
});
