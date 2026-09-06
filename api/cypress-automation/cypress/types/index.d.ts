/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable {
    /**
     * Realiza una petición GET a la API
     */
    apiGet<T>(endpoint: string): Chainable<Cypress.Response<T>>;

    /**
     * Realiza una petición POST a la API
     */
    apiPost<T>(endpoint: string, body: unknown): Chainable<Cypress.Response<T>>;

    /**
     * Realiza una petición PUT a la API
     */
    apiPut<T>(endpoint: string, body: unknown): Chainable<Cypress.Response<T>>;

    /**
     * Realiza una petición DELETE a la API
     */
    apiDelete<T>(endpoint: string): Chainable<Cypress.Response<T>>;

    /**
     * Login con credenciales
     */
    login(username: string, password: string): Chainable<void>;

    /**
     * Obtiene datos de fixtures
     */
    getFixture<T extends keyof Cypress.Fixtures>(fixture: T): Chainable<Cypress.Fixtures[T]>;
  }

  interface Fixtures {
    users: {
      admin: { username: string; password: string };
      regular: { username: string; password: string };
    };
    posts: { title: string; body: string; userId: number }[];
    apiEndpoints: {
      posts: string;
      users: string;
      comments: string;
    };
  }
}
