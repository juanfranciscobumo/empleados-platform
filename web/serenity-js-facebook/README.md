# Serenity/JS The Internet

[![Serenity/JS](https://img.shields.io/badge/Serenity%2FJS-3.x-FF6B6B?style=flat-square)](https://serenity-js.org/)
[![Playwright](https://img.shields.io/badge/Playwright-1.40%2B-2EAD33?style=flat-square&logo=playwright)](https://playwright.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-13-23D96B?style=flat-square&logo=cucumber)](https://cucumber.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.9-3178C6?style=flat-square&logo=typescript)](https://www.typescriptlang.org/)
[![Node.js](https://img.shields.io/badge/Node.js-20%2B-339933?style=flat-square&logo=node.js)](https://nodejs.org/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=flat-square&logo=github-actions)](https://github.com/features/actions)

Automated BDD tests for [The Internet](https://the-internet.herokuapp.com/login) using **Serenity/JS**, **Cucumber**, and **Playwright** with the [Screenplay Pattern](https://serenity-js.org/handbook/design/screenplay-pattern/).

## Reporte de Pruebas

Ver reporte en GitHub Pages: https://juanfranciscobumo.github.io/serenity-js-facebook/

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│           Serenity/JS + Cucumber + Playwright               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Feature   │    │    Step     │    │    Task     │     │
│  │   (Gherkin) │───▶│ Definitions │───▶│  (Screenplay)│    │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│         │                  │                  │             │
│         ▼                  ▼                  ▼             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         The Internet (Web App)                       │   │
│  └─────────────────────────────────────────────────────┘   │
│                          │                                  │
│                          ▼                                  │
│  ┌─────────────┐    ┌─────────────┐                        │
│  │  Serenity   │    │  HTML       │                        │
│  │  BDD Report │    │  Reporter   │                        │
│  └─────────────┘    └─────────────┘                        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Requisitos previos

- Node.js >= 20
- Java JRE 17+ (para reportes Serenity BDD)

## Instalación

```bash
npm install
npx playwright install chromium
```

## Scripts disponibles

| Comando | Descripción |
|---------|-------------|
| `npm test` | Ejecuta todos los tests y genera reporte |
| `HEADLESS=false npm test` | Ejecuta con navegador visible |
| `npm run test:execute` | Ejecuta tests sin generar reporte |
| `npm run test:report` | Genera reporte Serenity BDD |
| `npm run start` | Abre reporte en http://localhost:8080 |
| `npm run lint` | Verifica código con ESLint |
| `npm run lint:fix` | Corrige problemas de ESLint |

## Estructura del proyecto

```
├── features/
│   ├── login.feature                    # Escenarios Gherkin
│   ├── step-definitions/                # Definiciones de pasos
│   │   ├── parameter.steps.ts           # Tipos de parámetros custom
│   │   └── login.steps.ts              # Implementación de pasos
│   └── support/
│       └── serenity.config.ts           # Configuración Serenity + Playwright
├── test/
│   ├── Actors.ts                        # Configuración de actores
│   ├── index.ts                         # Barrel exports
│   └── login/
│       ├── LoginForm.ts                 # Page Object del formulario
│       ├── Authenticate.ts              # Task: ingresar credenciales
│       └── VerifyProfile.ts             # Question: verificar mensaje
├── cucumber.js                          # Configuración Cucumber
├── tsconfig.json                        # Configuración TypeScript
└── package.json
```

## Screenplay Pattern

### Actors
Los actores representan usuarios que interactúan con la aplicación. Cada actor tiene habilidades como `BrowseTheWebWithPlaywright`.

### Tasks
Las tareas componen secuencias de interacciones con significado de dominio:
```typescript
export const Authenticate = {
  using: (username: string, password: string) =>
    Task.where(`#actor logs in as ${username}`,
      Enter.theValue(username).into(LoginPage.usernameField()),
      Enter.theValue(password).into(LoginPage.passwordField()),
      Press.the('Enter').in(LoginPage.passwordField()),
    ),
};
```

### Questions
Las preguntas consultan el sistema para obtener información:
```typescript
export const VerifyMessage = {
  isVisible: (expectedText: string) =>
    Task.where(`#actor verifies message contains "${expectedText}"`,
      Ensure.that(Text.of(LoginPage.flashMessage()), includes(expectedText)),
    ),
};
```

## Tests incluidos (2 scenarios)

### Login exitoso
- Usuario: `tomsmith`, Password: `SuperSecretPassword!`
- Verifica: "You logged into a secure area!"

### Login fallido
- Usuario: `foobar`, Password: `barfoo`
- Verifica: "Your username is invalid!"

## Tecnologías

- Serenity/JS 3.x (Screenplay Pattern + BDD Reports)
- Playwright 1.40+ (Browser Automation)
- Cucumber.js 13 (BDD Test Runner)
- TypeScript 5.9 (Type Safety)
- GitHub Actions (CI/CD)

## Autor

Juan Francisco Builes Montoya - juanfranciscobumo@gmail.com
