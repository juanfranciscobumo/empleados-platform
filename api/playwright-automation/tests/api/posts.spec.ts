import { test, expect, testData } from "../../fixtures/test-data";

interface Post {
  id: number;
  title: string;
  body: string;
  userId: number;
}

test.describe("Posts API Tests", () => {
  test("GET - Debería obtener todos los posts", async ({ apiHelpers }) => {
    const { status, data } = await apiHelpers.get<Post[]>(testData.endpoints.posts);

    expect(status).toBe(200);
    expect(data).toBeInstanceOf(Array);
    expect(data.length).toBeGreaterThan(0);
  });

  test("GET - Debería obtener un post por ID", async ({ apiHelpers }) => {
    const { status, data } = await apiHelpers.get<Post>(`${testData.endpoints.posts}/1`);

    expect(status).toBe(200);
    expect(data).toHaveProperty("id", 1);
    expect(data).toHaveProperty("title");
    expect(data).toHaveProperty("body");
    expect(data).toHaveProperty("userId");
  });

  test("POST - Debería crear un nuevo post", async ({ apiHelpers }) => {
    const newPost = testData.posts.newPost;

    const { status, data } = await apiHelpers.post<Post>(testData.endpoints.posts, newPost);

    expect(status).toBe(201);
    expect(data).toHaveProperty("id");
    expect(data.title).toBe(newPost.title);
    expect(data.body).toBe(newPost.body);
    expect(data.userId).toBe(newPost.userId);
  });

  test("PUT - Debería actualizar un post existente", async ({ apiHelpers }) => {
    const updatedPost = {
      title: "Post Actualizado",
      body: "Contenido actualizado",
      userId: 1,
    };

    const { status, data } = await apiHelpers.put<Post>(
      `${testData.endpoints.posts}/1`,
      updatedPost
    );

    expect(status).toBe(200);
    expect(data.title).toBe(updatedPost.title);
    expect(data.body).toBe(updatedPost.body);
  });

  test("DELETE - Debería eliminar un post", async ({ apiHelpers }) => {
    const { status } = await apiHelpers.delete(`${testData.endpoints.posts}/1`);

    expect(status).toBe(200);
  });

  test("GET - Debería filtrar posts por userId", async ({ apiHelpers }) => {
    const { status, data } = await apiHelpers.get<Post[]>(
      `${testData.endpoints.posts}?userId=1`
    );

    expect(status).toBe(200);
    expect(data).toBeInstanceOf(Array);
    data.forEach((post) => {
      expect(post.userId).toBe(1);
    });
  });
});
