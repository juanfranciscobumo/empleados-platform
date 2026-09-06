# Applitools Visual AI Testing

[![CI](https://github.com/juanfranciscobumo/applitools-visual-ai-testing/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/applitools-visual-ai-testing/actions/workflows/ci.yml)
[![Cypress](https://img.shields.io/badge/Cypress-13.6-green?style=flat-square&logo=cypress)](https://www.cypress.io)
[![Applitools](https://img.shields.io/badge/Applitools-Eyes-blue?style=flat-square)](https://applitools.com)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.3-3178C6?style=flat-square&logo=typescript&logoColor=white)](https://www.typescriptlang.org)
[![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-282A2E?style=flat-square&logo=githubactions&logoColor=white)](https://github.com/features/actions)

Visual AI regression testing using **Applitools Eyes** and **Cypress** against SauceDemo, with automated CI/CD and cross-browser visual validation.

## What This Project Does

Uses Applitools Visual AI to:
- **Detect visual regressions** automatically across UI changes
- **Cross-browser visual validation** (Chrome, Firefox, Edge)
- **Responsive layout testing** (Desktop, Tablet, Mobile)
- **Layout shift detection** with intelligent comparison
- **Baseline management** through Applitools dashboard

## Test Suites

| Suite | Tests | Description |
|-------|-------|-------------|
| Login | 3 | Login page states, error handling, locked user |
| Products | 4 | Product listing, detail view, sorting, mobile layout |
| Cart | 3 | Empty cart, cart with items, checkout flow |
| Cross-Browser | 2 | Viewport consistency, layout shift detection |

**Total: 12 visual tests**

## Key Features

- **Visual AI Comparison** — Intelligent pixel-by-pixel analysis with Applitools
- **Page Object Model** — Structured test organization
- **Custom Commands** — Reusable `cy.login()`, `cy.loginToDemo()` helpers
- **Batch Grouping** — Tests organized by feature area
- **Responsive Testing** — Multiple viewport sizes validated

## Prerequisites

- Node.js 18+
- Applitools API key (free for open source: https://applitools.com/community)

## Installation

```bash
npm install
npx cypress open
```

## Scripts

| Command | Description |
|---------|-------------|
| `npm run cy:open` | Open Cypress UI |
| `npm run cy:run` | Run tests headless |
| `npm run cy:run:chrome` | Run in Chrome headless |
| `npm run test` | Run all tests |
| `npm run lint` | Lint code |

## Project Structure

```
applitools-visual-ai-testing/
├── .github/
│   └── workflows/
│       └── ci.yml                  # CI/CD pipeline
├── cypress/
│   ├── e2e/
│   │   ├── login.cy.ts            # Login page visual tests
│   │   ├── products.cy.ts         # Products page visual tests
│   │   ├── cart.cy.ts             # Cart flow visual tests
│   │   └── cross-browser.cy.ts    # Cross-browser consistency
│   ├── fixtures/
│   └── support/
│       ├── commands.ts             # Custom Cypress commands
│       └── e2e.ts                  # Support file with Applitools
├── cypress.config.ts
├── package.json
├── tsconfig.json
└── README.md
```

## API Under Test

**SauceDemo** — `https://www.saucedemo.com`

| Page | Elements Tested |
|------|-----------------|
| Login | Username, password, login button, error messages |
| Products | Product grid, sort dropdown, item details |
| Cart | Cart items, checkout button, remove items |

## Sample Applitools Output

```
  Visual Tests
    Login Page - Visual Tests
      ✓ should capture login page baseline (2s)
      ✓ should capture login error state (1s)
      ✓ should capture locked out user error (1s)
    Products Page - Visual Tests
      ✓ should capture products page baseline (2s)
      ✓ should capture product detail view (2s)
      ✓ should capture product sort options (3s)
      ✓ should capture responsive layout - mobile (1s)
    Cart Flow - Visual Tests
      ✓ should capture empty cart state (1s)
      ✓ should capture cart with items (2s)
      ✓ should capture checkout step one (1s)
    Cross-Browser Visual Consistency
      ✓ should maintain visual consistency across viewports (3s)
      ✓ should detect layout shifts on inventory page (2s)

  12 passing
```

## Technologies

- **Cypress** — Modern E2E testing framework
- **Applitools Eyes** — Visual AI regression testing
- **TypeScript** — Type-safe test code
- **GitHub Actions** — CI/CD pipeline
- **GitHub Pages** — Report hosting

## License

MIT
