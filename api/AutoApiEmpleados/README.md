# Employee API Quality

[![CI](https://github.com/juanfranciscobumo/AutoApiEmpleados/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/AutoApiEmpleados/actions/workflows/ci.yml)
[![Serenity BDD](https://img.shields.io/badge/Serenity--BDD-4.2.7-informational)](https://serenity-bdd.github.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.12-green)](https://gradle.org/)

API test suite for an Employee REST API, built with **Serenity BDD**, **Screenplay pattern**, **Cucumber** and **Gradle**.

## What it demonstrates

- API-level BDD scenarios with Cucumber Gherkin syntax
- Screenplay pattern: tasks, questions, and interactions
- REST API testing with Serenity REST
- JUnit 5 Platform execution
- Serenity HTML reports deployed to GitHub Pages

## Tech stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Serenity BDD | 4.2.7 |
| Cucumber | 7.22.0 |
| JUnit | 5.11.4 |
| Gradle | 8.12 |

## Run locally

```bash
./gradlew clean test aggregate
```

On Windows:

```powershell
.\gradlew.bat clean test aggregate
```

The Serenity report is generated under `build/site/serenity/`.

## CI/CD

GitHub Actions runs the test suite on every push to `main` and deploys the Serenity report to **GitHub Pages**.

## Project structure

```
src/
├── main/java/          # Step definitions, tasks, questions, models
└── test/resources/
    └── features/       # Cucumber .feature files
```

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
