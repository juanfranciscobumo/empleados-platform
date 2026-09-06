# Playwright Automation

[![Playwright](https://img.shields.io/badge/Playwright-1.40%2B-2EAD33?style=flat-square&logo=playwright)](https://playwright.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.3.0-3178C6?style=flat-square&logo=typescript)](https://www.typescriptlang.org/)
[![Node.js](https://img.shields.io/badge/Node.js-20%2B-339933?style=flat-square&logo=node.js)](https://nodejs.org/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=flat-square&logo=github-actions)](https://github.com/features/actions)
[![Code Style](https://img.shields.io/badge/Code%20Style-ESLint%20%2B%20Prettier-4B32C3?style=flat-square&logo=eslint)](https://eslint.org/)
[![Report](https://img.shields.io/badge/Report-Allure%20%2B%20HTML-FF6B6B?style=flat-square)](https://docs.qameta.io/allure/)
[![Pages](https://img.shields.io/badge/Pages-GitHub%20Pages-222222?style=flat-square&logo=github)](https://pages.github.com/)

Framework de automatización de pruebas API con Playwright y TypeScript para [JSONPlaceholder](https://jsonplaceholder.typicode.com). Incluye custom fixtures, cliente API tipado y reportes con Allure + HTML.

## Reporte de Pruebas

Ver reporte en GitHub Pages: https://juanfranciscobumo.github.io/playwright-automation/

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                Playwright Automation                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Test      │    │  Custom     │    │  Fixtures   │     │
│  │   Specs     │───▶│  Fixtures   │───▶│  (Data)     │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│         │                  │                  │             │
│         ▼                  ▼                  ▼             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              API (JSONPlaceholder)                   │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ▼                                  │
│  ┌─────────────┐    ┌─────────────┐                        │
│  │  Allure     │    │  HTML       │                        │
│  │  Reporter   │    │  Reporter   │                        │
│  └─────────────┘    └─────────────┘                        │
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
| `npm run test:api` | Ejecuta solo tests de API |
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
│   └── test-data.ts       # Datos de prueba y custom fixtures
├── pages/
│   └── BasePage.ts        # Page Object base
├── utils/
│   └── api-client.ts      # Cliente API utility
├── tests/
│   └── api/               # Tests de API
│       ├── posts.spec.ts
│       ├── users.spec.ts
│       └── comments.spec.ts
├── .eslintrc.json         # Configuración ESLint
├── .prettierrc            # Configuración Prettier
└── playwright.config.ts   # Configuración principal
```

## Custom Fixtures y Helpers

### apiHelpers
Fixture personalizado para peticiones API:

```typescript
test("GET posts", async ({ apiHelpers }) => {
  const { status, data } = await apiHelpers.get<Post[]>("/posts");
  expect(status).toBe(200);
});
```

### Métodos disponibles
- `apiHelpers.get<T>(endpoint)` - Petición GET
- `apiHelpers.post<T>(endpoint, body)` - Petición POST
- `apiHelpers.put<T>(endpoint, body)` - Petición PUT
- `apiHelpers.delete<T>(endpoint)` - Petición DELETE

### testData
Datos de prueba centralizados:

```typescript
import { testData } from "../fixtures/test-data";

test("ejemplo", async ({ apiHelpers }) => {
  const { data } = await apiHelpers.get(testData.endpoints.posts);
});
```

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
import { test, expect, testData } from "../fixtures/test-data";

interface Post {
  id: number;
  title: string;
  body: string;
  userId: number;
}

test("GET - Debería obtener posts", async ({ apiHelpers }) => {
  const { status, data } = await apiHelpers.get<Post[]>(testData.endpoints.posts);
  
  expect(status).toBe(200);
  expect(data).toBeInstanceOf(Array);
});

test("POST - Debería crear post", async ({ apiHelpers }) => {
  const { status, data } = await apiHelpers.post<Post>(
    testData.endpoints.posts,
    testData.posts.newPost
  );
  
  expect(status).toBe(201);
  expect(data).toHaveProperty("id");
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

La configuración principal se encuentra en `playwright.config.ts`:

- **baseUrl**: https://jsonplaceholder.typicode.com
- **Browsers**: Chromium, Firefox, WebKit, Mobile Chrome (Pixel 5), Mobile Safari (iPhone 13)
- **Reporters**: HTML, List, Allure
- **Retries**: 2 en CI, 0 local
- **Trace**: on-first-retry
- **Screenshot**: only-on-failure

## Tecnologías

- Playwright 1.40+
- TypeScript 5
- Node.js 20+
- GitHub Actions (CI/CD)
- ESLint + Prettier (Code Quality)
- Allure (Advanced Reports)
- GitHub Pages (Report Deployment)
