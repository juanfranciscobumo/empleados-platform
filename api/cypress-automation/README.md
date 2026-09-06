# Cypress Automation

[![Cypress](https://img.shields.io/badge/Cypress-13.0.0-17202C?style=flat-square&logo=cypress)](https://www.cypress.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.3.0-3178C6?style=flat-square&logo=typescript)](https://www.typescriptlang.org/)
[![Node.js](https://img.shields.io/badge/Node.js-18%2B-339933?style=flat-square&logo=node.js)](https://nodejs.org/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=flat-square&logo=github-actions)](https://github.com/features/actions)
[![Code Style](https://img.shields.io/badge/Code%20Style-ESLint%20%2B%20Prettier-4B32C3?style=flat-square&logo=eslint)](https://eslint.org/)
[![Report](https://img.shields.io/badge/Report-Mochawesome-FF6B6B?style=flat-square)](https://github.com/adamgruber/mochawesome)
[![Pages](https://img.shields.io/badge/Pages-GitHub%20Pages-222222?style=flat-square&logo=github)](https://pages.github.com/)

Framework de automatización de pruebas API con Cypress y TypeScript para [JSONPlaceholder](https://jsonplaceholder.typicode.com).

## Reporte de Pruebas

Ver reporte en GitHub Pages: https://juanfranciscobumo.github.io/cypress-automation/

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    Cypress Automation                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Test      │    │  Custom     │    │  Fixtures   │     │
│  │   Specs     │───▶│  Commands   │───▶│  (Data)     │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│         │                  │                  │             │
│         ▼                  ▼                  ▼             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              API (JSONPlaceholder)                   │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ▼                                  │
│  ┌─────────────┐    ┌─────────────┐                        │
│  │  Mochawesome│    │  GitHub     │                        │
│  │  Reporter   │    │  Pages      │                        │
│  └─────────────┘    └─────────────┘                        │
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
| `npm run cypress:run:api` | Ejecuta solo tests de API |
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
│   │   └── api/              # Tests de API
│   │       ├── posts.cy.ts
│   │       ├── users.cy.ts
│   │       └── comments.cy.ts
│   ├── fixtures/             # Datos de prueba
│   │   ├── users.json
│   │   ├── posts.json
│   │   └── apiEndpoints.json
│   ├── support/
│   │   ├── commands.ts       # Custom commands
│   │   └── e2e.ts
│   └── types/
│       └── index.d.ts        # Definiciones de tipos
├── .eslintrc.json            # Configuración ESLint
├── .prettierrc               # Configuración Prettier
├── cypress.config.ts         # Configuración principal
└── package.json
```

## Custom Commands

### API
- `cy.apiGet<T>(endpoint)` - Petición GET
- `cy.apiPost<T>(endpoint, body)` - Petición POST
- `cy.apiPut<T>(endpoint, body)` - Petición PUT
- `cy.apiDelete<T>(endpoint)` - Petición DELETE

### Autenticación
- `cy.login(username, password)` - Login con sesión

### Fixtures
- `cy.getFixture<T>(fixtureName)` - Obtener datos de fixtures tipados

## Tests incluidos (11)

### Posts (6)
- GET all posts
- GET post by ID
- POST create post
- PUT update post
- DELETE post
- GET posts by userId

### Users (3)
- GET all users
- GET user by ID
- GET user posts

### Comments (2)
- GET all comments
- GET comments by postId

## Ejemplo de uso

```typescript
// Test de API
cy.apiGet<Post[]>("/posts").then((response) => {
  expect(response.status).to.eq(200);
  expect(response.body).to.be.an("array");
});

// Crear post
cy.apiPost<Post>("/posts", {
  title: "Nuevo Post",
  body: "Contenido",
  userId: 1
});
```

## API Under Test

Se utiliza [JSONPlaceholder](https://jsonplaceholder.typicode.com) como API de prueba.

### Base URL
```
https://jsonplaceholder.typicode.com
```

### Endpoints testeados

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/posts` | Obtener todos los posts |
| GET | `/posts/:id` | Obtener un post por ID |
| GET | `/posts?userId=:id` | Obtener posts por usuario |
| POST | `/posts` | Crear un nuevo post |
| PUT | `/posts/:id` | Actualizar un post |
| DELETE | `/posts/:id` | Eliminar un post |
| GET | `/users` | Obtener todos los usuarios |
| GET | `/users/:id` | Obtener un usuario por ID |
| GET | `/users/:id/posts` | Obtener posts de un usuario |
| GET | `/comments` | Obtener todos los comentarios |
| GET | `/comments?postId=:id` | Obtener comentarios por post |

## Configuración

La configuración principal se encuentra en `cypress.config.ts`:

- **baseUrl**: https://jsonplaceholder.typicode.com
- **viewport**: 1280x720
- **Spec pattern**: `cypress/e2e/**/*.cy.ts`

## Tecnologías

- Cypress 13
- TypeScript 5
- Node.js 18+
- GitHub Actions (CI/CD)
- ESLint + Prettier (Code Quality)
- Mochawesome (HTML Reports)
- GitHub Pages (Report Deployment)
