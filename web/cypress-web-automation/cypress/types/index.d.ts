/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable {
    /**
     * Login con credenciales en SauceDemo
     */
    login(username: string, password: string): Chainable<void>;

    /**
     * Login como usuario estándar
     */
    loginAsStandardUser(): Chainable<void>;

    /**
     * Login como usuario problemático
     */
    loginAsProblemUser(): Chainable<void>;

    /**
     * Login como usuario bloqueado
     */
    loginAsLockedOutUser(): Chainable<void>;

    /**
     * Agregar producto al carrito por índice
     */
    addToCart(index: number): Chainable<void>;

    /**
     * Ir al carrito de compras
     */
    goToCart(): Chainable<void>;

    /**
     * Obtener elemento por data-test
     */
    getByDataTest(selector: string): Chainable<JQuery<HTMLElement>>;
  }

  interface Fixtures {
    users: {
      standard: { username: string; password: string };
      problem: { username: string; password: string };
      lockedOut: { username: string; password: string };
    };
    products: {
      names: string[];
      prices: number[];
    };
    urls: {
      inventory: string;
      cart: string;
      checkout: string;
    };
  }
}
