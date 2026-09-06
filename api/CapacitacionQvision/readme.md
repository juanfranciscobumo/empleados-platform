# REST API Training - Capacitacion Qvision

[![CI](https://github.com/juanfranciscobumo/CapacitacionQvision/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/CapacitacionQvision/actions/workflows/ci.yml)
[![Serenity BDD](https://img.shields.io/badge/Serenity--BDD-4.2.7-informational)](https://serenity-bdd.github.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.6-green)](https://gradle.org/)

REST API test automation training project, built with **Serenity BDD**, **Screenplay pattern**, **Cucumber** and **Gradle**. Tests the [reqres.in](https://reqres.in) API.

## What it demonstrates

- REST API testing with Serenity Screenplay REST
- Cucumber BDD scenarios with Spanish Gherkin syntax
- Data-driven testing with Cucumber Examples tables
- Environment-based test data (tags: `@laboratorio`, `@desarrollo`)
- Serenity HTML reports

## Tech stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Serenity BDD | 4.2.7 |
| Cucumber | 7.22.0 |
| JUnit | 5.11.4 |
| Gradle | 8.6 |

## Test scenarios

- **Create user**: Register a user with name and job, validate response
- **Login**: Attempt login with invalid data, validate error message

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

GitHub Actions runs the test suite on every push to `master` and deploys the Serenity report to **GitHub Pages**.

## Project structure

```
src/
├── main/java/          # Step definitions, tasks, questions, models
└── test/resources/
    └── features/       # Cucumber .feature files (Spanish)
```

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
