# Reto La Haus - Full Stack Test Automation

[![CI](https://github.com/juanfranciscobumo/RetoLaHaus/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/RetoLaHaus/actions/workflows/ci.yml)
[![Serenity BDD](https://img.shields.io/badge/Serenity--BDD-4.2.7-informational)](https://serenity-bdd.github.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.12-green)](https://gradle.org/)

Full-stack test automation challenge for the **La Haus** real estate platform, built with **Serenity BDD**, **Screenplay pattern**, **Cucumber** and **Gradle**.

## What it demonstrates

- **Front-end**: Web UI testing for property search (Selenium WebDriver + Serenity Screenplay)
- **Back-end**: REST API CRUD operations (create, read, update, delete users)
- Cucumber BDD scenarios with Gherkin syntax
- Selenoid integration for browser automation
- Serenity HTML reports

## Tech stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Serenity BDD | 4.2.7 |
| Cucumber | 7.22.0 |
| JUnit | 5.11.4 |
| Selenium | managed by Serenity |
| Gradle | 8.12 |

## Test scenarios

### Front-end
- Search properties in Medellin (apartments, neighborhood, rooms, bathrooms, area)

### Back-end (REST API)
- Create user
- Search user
- Update user (PUT)
- Update user (PATCH)
- Delete user

## Run locally

```bash
docker-compose up -d
./gradlew clean test aggregate
```

On Windows:

```powershell
.\gradlew.bat clean test aggregate
```

The Serenity report is generated under `build/site/serenity/`.

## CI/CD

GitHub Actions runs the test suite on every push to `master` using **Selenoid** for browser automation, and deploys the Serenity report to **GitHub Pages**.

## Project structure

```
src/
├── main/java/          # Step definitions, tasks, questions, models, UI targets
└── test/resources/
    └── features/
        ├── front/      # Web UI feature files
        └── back/       # REST API feature files
```

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
