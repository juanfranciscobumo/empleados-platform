# Cypress Web Automation

[![Cypress](https://img.shields.io/badge/Cypress-13.0.0-17202C?style=flat-square&logo=cypress)](https://www.cypress.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.3.0-3178C6?style=flat-square&logo=typescript)](https://www.typescriptlang.org/)
[![Node.js](https://img.shields.io/badge/Node.js-18%2B-339933?style=flat-square&logo=node.js)](https://nodejs.org/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=flat-square&logo=github-actions)](https://github.com/features/actions)
[![Code Style](https://img.shields.io/badge/Code%20Style-ESLint%20%2B%20Prettier-4B32C3?style=flat-square&logo=eslint)](https://eslint.org/)
[![Accessibility](https://img.shields.io/badge/Accessibility-axe--core-FA3C00?style=flat-square&logo=axe)](https://www.deque.com/axe/)
[![Report](https://img.shields.io/badge/Report-Mochawesome-FF6B6B?style=flat-square)](https://github.com/adamgruber/mochawesome)
[![Pages](https://img.shields.io/badge/Pages-GitHub%20Pages-222222?style=flat-square&logo=github)](https://pages.github.com/)

Framework de automatización web con Cypress y TypeScript para [SauceDemo](https://www.saucedemo.com). Implementa Page Object Model, custom commands, tests de accesibilidad con axe-core y reportes HTML con Mochawesome.

## Reporte de Pruebas

Ver reporte en GitHub Pages: https://juanfranciscobumo.github.io/cypress-web-automation/

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                Cypress Web Automation                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Test      │    │  Page       │    │  Custom     │     │
│  │   Specs     │───▶│  Objects    │───▶│  Commands   │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│         │                  │                  │             │
│         ▼                  ▼                  ▼             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              SauceDemo (Web App)                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ▼                                  │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │  Mochawesome│    │   axe-core  │    │  GitHub     │     │
│  │  Reporter   │    │  (A11y)     │    │  Pages      │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Requisitos previos

- Node.js >= 18
- npm o yarn

## Instalación

```bash
npm install
```

## Scripts disponibles

| Comando | Descripción |
|---------|-------------|
| `npm run cypress:open` | Abre la UI de Cypress |
| `npm run cypress:run` | Ejecuta tests en modo headless |
| `npm run cypress:run:headless` | Ejecuta tests en Chrome headless |
| `npm run test:accessibility` | Ejecuta tests de accesibilidad |
| `npm run test:dev` | Ejecuta tests en ambiente dev |
| `npm run test:staging` | Ejecuta tests en ambiente staging |
| `npm run test:prod` | Ejecuta tests en ambiente production |
| `npm run lint` | Verifica código con ESLint |
| `npm run lint:fix` | Corrige problemas de ESLint |
| `npm run format` | Formatea código con Prettier |
| `npm run format:check` | Verifica formateo con Prettier |
| `npm run report:generate` | Genera reporte HTML completo |

## Estructura del proyecto

```
├── config/
│   └── environments/        # Configuración por ambiente
│       ├── dev.json
│       ├── staging.json
│       └── prod.json
├── cypress/
│   ├── e2e/
│   │   ├── pages/                    # Page Objects
│   │   │   ├── LoginPage.ts
│   │   │   ├── InventoryPage.ts
│   │   │   ├── CartPage.ts
│   │   │   └── CheckoutPage.ts
│   │   └── tests/                    # Tests
│   │       ├── login.cy.ts
│   │       ├── inventory.cy.ts
│   │       ├── cart.cy.ts
│   │       ├── checkout.cy.ts
│   │       └── accessibility.cy.ts
│   ├── fixtures/                     # Datos de prueba
│   │   ├── users.json
│   │   ├── products.json
│   │   └── urls.json
│   ├── support/
│   │   ├── commands.ts               # Custom commands
│   │   └── e2e.ts
│   └── types/
│       └── index.d.ts                # Definiciones de tipos
├── .eslintrc.json                    # Configuración ESLint
├── .prettierrc                       # Configuración Prettier
├── cypress.config.ts                 # Configuración principal
└── package.json
```

## Page Objects

### LoginPage
- `visit()` - Navegar a la página de login
- `enterUsername(username)` - Ingresar usuario
- `enterPassword(password)` - Ingresar contraseña
- `clickLogin()` - Hacer clic en login
- `login(username, password)` - Login completo
- `getErrorMessage()` - Obtener mensaje de error
- `isErrorDisplayed()` - Verificar si se muestra error

### InventoryPage
- `isInventoryDisplayed()` - Verificar si se muestra el inventario
- `getItemCount()` - Obtener cantidad de productos
- `getItemNames()` - Obtener nombres de productos
- `getItemPrices()` - Obtener precios de productos
- `addToCart(index)` - Agregar producto al carrito
- `removeFromCart(index)` - Eliminar producto del carrito
- `goToCart()` - Ir al carrito
- `getCartBadgeCount()` - Obtener contador del carrito
- `sortBy(option)` - Ordenar productos

### CartPage
- `isCartDisplayed()` - Verificar si se muestra el carrito
- `getItemCount()` - Obtener cantidad de items
- `getItemNames()` - Obtener nombres de items
- `removeItem(index)` - Eliminar item del carrito
- `checkout()` - Ir al checkout
- `continueShopping()` - Volver a comprar

### CheckoutPage
- `fillCheckoutInfo(firstName, lastName, postalCode)` - Llenar información
- `continueToOverview()` - Continuar al resumen
- `finishCheckout()` - Finalizar compra
- `cancelCheckout()` - Cancelar compra
- `getCompleteMessage()` - Obtener mensaje de confirmación
- `backToProducts()` - Volver a productos

## Custom Commands

- `cy.login(username, password)` - Login en SauceDemo
- `cy.loginAsStandardUser()` - Login como usuario estándar
- `cy.loginAsProblemUser()` - Login como usuario problemático
- `cy.loginAsLockedOutUser()` - Login como usuario bloqueado
- `cy.addToCart(index)` - Agregar producto al carrito
- `cy.goToCart()` - Ir al carrito
- `cy.getByDataTest(selector)` - Seleccionar por data-test attribute

## Tests incluidos (29)

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

### Accesibilidad - axe-core (5)
- Violaciones de accesibilidad (critical/serious)
- Elementos de formulario enfocables
- Visibilidad de mensajes de error
- Jerarquía de encabezados
- Nombres accesibles en botones y campos

## Website Under Test

[SauceDemo](https://www.saucedemo.com) - Tienda de demostración para testing automatizado.

## Tecnologías

- Cypress 13
- TypeScript 5
- Node.js 18+
- Page Object Model
- GitHub Actions (CI/CD)
- ESLint + Prettier (Code Quality)
- axe-core / cypress-axe (Accessibility Testing)
- Mochawesome (HTML Reports)
- GitHub Pages (Report Deployment)
