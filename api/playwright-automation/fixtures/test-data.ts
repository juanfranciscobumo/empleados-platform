import { test as base } from "@playwright/test";

export interface User {
  username: string;
  password: string;
  email: string;
}

export interface Post {
  title: string;
  body: string;
  userId: number;
}

export interface ApiEndpoints {
  posts: string;
  users: string;
  comments: string;
  albums: string;
  todos: string;
}

export const testData = {
  users: {
    admin: {
      username: "admin",
      password: "admin123",
      email: "admin@example.com",
    } as User,
    regular: {
      username: "user",
      password: "user123",
      email: "user@example.com",
    } as User,
  },
  posts: {
    newPost: {
      title: "Test Post",
      body: "This is a test post body",
      userId: 1,
    } as Post,
    posts: [
      {
        title: "First Post",
        body: "Body of first post",
        userId: 1,
      },
      {
        title: "Second Post",
        body: "Body of second post",
        userId: 2,
      },
    ] as Post[],
  },
  endpoints: {
    posts: "/posts",
    users: "/users",
    comments: "/comments",
    albums: "/albums",
    todos: "/todos",
  } as ApiEndpoints,
};

type TestFixtures = {
  apiHelpers: ApiHelpers;
};

export interface ApiHelpers {
  get: <T>(endpoint: string) => Promise<{ status: number; data: T }>;
  post: <T>(endpoint: string, body: unknown) => Promise<{ status: number; data: T }>;
  put: <T>(endpoint: string, body: unknown) => Promise<{ status: number; data: T }>;
  delete: <T>(endpoint: string) => Promise<{ status: number; data: T }>;
}

export const test = base.extend<TestFixtures>({
  apiHelpers: async ({ request }, use) => {
    const apiHelpers: ApiHelpers = {
      get: async <T>(endpoint: string) => {
        const response = await request.get(endpoint);
        const data = await response.json();
        return { status: response.status(), data: data as T };
      },
      post: async <T>(endpoint: string, body: unknown) => {
        const response = await request.post(endpoint, { data: body });
        const data = await response.json();
        return { status: response.status(), data: data as T };
      },
      put: async <T>(endpoint: string, body: unknown) => {
        const response = await request.put(endpoint, { data: body });
        const data = await response.json();
        return { status: response.status(), data: data as T };
      },
      delete: async <T>(endpoint: string) => {
        const response = await request.delete(endpoint);
        const data = await response.json().catch(() => null);
        return { status: response.status(), data: data as T };
      },
    };
    await use(apiHelpers);
  },
});

export { expect } from "@playwright/test";
