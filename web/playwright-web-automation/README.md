# Playwright Web Automation

[![Playwright](https://img.shields.io/badge/Playwright-1.40%2B-2EAD33?style=flat-square&logo=playwright)](https://playwright.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.3.0-3178C6?style=flat-square&logo=typescript)](https://www.typescriptlang.org/)
[![Node.js](https://img.shields.io/badge/Node.js-20%2B-339933?style=flat-square&logo=node.js)](https://nodejs.org/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=flat-square&logo=github-actions)](https://github.com/features/actions)
[![Code Style](https://img.shields.io/badge/Code%20Style-ESLint%20%2B%20Prettier-4B32C3?style=flat-square&logo=eslint)](https://eslint.org/)
[![Accessibility](https://img.shields.io/badge/Accessibility-axe--core-FA3C00?style=flat-square&logo=axe)](https://www.deque.com/axe/)
[![Report](https://img.shields.io/badge/Report-Allure%20%2B%20HTML-FF6B6B?style=flat-square)](https://docs.qameta.io/allure/)
[![Pages](https://img.shields.io/badge/Pages-GitHub%20Pages-222222?style=flat-square&logo=github)](https://pages.github.com/)

Framework de automatización web con Playwright y TypeScript para [SauceDemo](https://www.saucedemo.com). Implementa Page Object Model, custom fixtures, tests de accesibilidad con axe-core y reportes con Allure + HTML.

## Reporte de Pruebas

Ver reporte en GitHub Pages: https://juanfranciscobumo.github.io/playwright-web-automation/

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│              Playwright Web Automation                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Test      │    │  Page       │    │  Custom     │     │
│  │   Specs     │───▶│  Objects    │───▶│  Fixtures   │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│         │                  │                  │             │
│         ▼                  ▼                  ▼             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              SauceDemo (Web App)                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ▼                                  │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │  HTML       │    │   axe-core  │    │  GitHub     │     │
│  │  Reporter   │    │  (A11y)     │    │  Pages      │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Requisitos previos

- Node.js >= 20
- npm o yarn

## Instalación

```bash
npm install
npx playwright install
```

## Scripts disponibles

| Comando | Descripción |
|---------|-------------|
| `npm test` | Ejecuta todos los tests |
| `npm run test:headed` | Ejecuta tests con navegador visible |
| `npm run test:ui` | Abre la UI de Playwright |
| `npm run test:debug` | Ejecuta tests en modo debug |
| `npm run test:login` | Ejecuta tests de login |
| `npm run test:inventory` | Ejecuta tests de inventario |
| `npm run test:cart` | Ejecuta tests de carrito |
| `npm run test:checkout` | Ejecuta tests de checkout |
| `npm run test:accessibility` | Ejecuta tests de accesibilidad |
| `npm run test:dev` | Ejecuta tests en ambiente dev |
| `npm run test:staging` | Ejecuta tests en ambiente staging |
| `npm run test:prod` | Ejecuta tests en ambiente production |
| `npm run report` | Abre el reporte HTML |
| `npm run allure:generate` | Genera reporte Allure |
| `npm run allure:open` | Abre reporte Allure |
| `npm run lint` | Verifica código con ESLint |
| `npm run lint:fix` | Corrige problemas de ESLint |
| `npm run format` | Formatea código con Prettier |
| `npm run format:check` | Verifica formateo con Prettier |

## Estructura del proyecto

```
├── config/
│   └── environments/        # Configuración por ambiente
│       ├── dev.json
│       ├── staging.json
│       └── prod.json
├── fixtures/
│   └── test-data.ts           # Datos de prueba y fixtures
├── pages/                     # Page Objects
│   ├── BasePage.ts
│   ├── LoginPage.ts
│   ├── InventoryPage.ts
│   ├── CartPage.ts
│   └── CheckoutPage.ts
├── tests/
│   └── tests/                 # Tests
│       ├── login.spec.ts
│       ├── inventory.spec.ts
│       ├── cart.spec.ts
│       ├── checkout.spec.ts
│       └── accessibility.spec.ts
├── .eslintrc.json             # Configuración ESLint
├── .prettierrc                # Configuración Prettier
└── playwright.config.ts       # Configuración principal
```

## Page Objects

### BasePage
- `navigate(path)` - Navegar a una ruta
- `getTitle()` - Obtener título de la página
- `waitForSelector(selector, timeout)` - Esperar por elemento
- `isVisible(selector)` - Verificar si un elemento es visible
- `click(selector)` - Hacer clic en un elemento
- `fill(selector, value)` - Llenar un campo

### LoginPage
- `visit()` - Navegar a la página de login
- `login(username, password)` - Login completo
- `getErrorMessage()` - Obtener mensaje de error

### InventoryPage
- `isInventoryDisplayed()` - Verificar inventario
- `getItemCount()` - Cantidad de productos
- `getItemNames()` - Nombres de productos
- `getItemPrices()` - Precios de productos
- `addToCart(index)` - Agregar al carrito
- `removeFromCart(index)` - Eliminar del carrito
- `goToCart()` - Ir al carrito
- `getCartBadgeCount()` - Contador del carrito
- `sortBy(option)` - Ordenar productos

### CartPage
- `isCartDisplayed()` - Verificar carrito
- `getItemCount()` - Cantidad de items
- `getItemNames()` - Nombres de items
- `removeItem(index)` - Eliminar item
- `checkout()` - Ir al checkout
- `continueShopping()` - Volver a comprar

### CheckoutPage
- `fillCheckoutInfo(firstName, lastName, postalCode)` - Llenar información
- `continueToOverview()` - Continuar al resumen
- `finishCheckout()` - Finalizar compra
- `cancelCheckout()` - Cancelar compra
- `getCompleteMessage()` - Obtener mensaje de confirmación
- `backToProducts()` - Volver a productos

## Custom Fixtures

### loginPage
```typescript
test("ejemplo", async ({ loginPage }) => {
  await loginPage.login("user", "pass");
});
```

### inventoryPage
```typescript
test("ejemplo", async ({ inventoryPage }) => {
  await inventoryPage.addToCart(0);
  const count = await inventoryPage.getCartBadgeCount();
});
```

### cartPage
```typescript
test("ejemplo", async ({ cartPage }) => {
  await cartPage.checkout();
});
```

### checkoutPage
```typescript
test("ejemplo", async ({ checkoutPage }) => {
  await checkoutPage.fillCheckoutInfo("Juan", "Perez", "12345");
  await checkoutPage.finishCheckout();
});
```

## Tests incluidos (30)

### Login (5)
- Login exitoso
- Error con usuario bloqueado
- Error con credenciales inválidas
- Error con contraseña vacía
- Error con usuario vacío

### Inventario (8)
- Mostrar todos los productos
- Mostrar nombres de productos
- Mostrar precios de productos
- Agregar producto al carrito
- Agregar múltiples productos
- Ordenar precio bajo-alto
- Ordenar precio alto-bajo
- Ordenar nombre A-Z

### Carrito (5)
- Agregar producto y ver en carrito
- Agregar múltiples productos
- Eliminar producto del carrito
- Continuar comprando
- Mostrar nombre del producto en carrito

### Checkout (6)
- Completar compra exitosamente
- Cancelar compra
- Error nombre faltante
- Error apellido faltante
- Error código postal faltante
- Volver a productos después de compra

### Accesibilidad - axe-core (6)
- Violaciones de accesibilidad (critical/serious)
- Navegación por teclado
- Visibilidad de mensajes de error
- Jerarquía de encabezados
- Atributo lang en HTML
- Nombres accesibles en botones y campos

## Website Under Test

[SauceDemo](https://www.saucedemo.com) - Tienda de demostración para testing automatizado.

## Tecnologías

- Playwright 1.40+
- TypeScript 5
- Node.js 20+
- Page Object Model
- GitHub Actions (CI/CD)
- ESLint + Prettier (Code Quality)
- axe-core / @axe-core/playwright (Accessibility Testing)
- Allure (Advanced Reports)
- GitHub Pages (Report Deployment)
