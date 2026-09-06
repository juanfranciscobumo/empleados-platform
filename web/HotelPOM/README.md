# Hotel Search - Web UI Testing

[![CI](https://github.com/juanfranciscobumo/HotelPOM/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/HotelPOM/actions/workflows/ci.yml)
[![Serenity BDD](https://img.shields.io/badge/Serenity--BDD-4.2.7-informational)](https://serenity-bdd.github.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.6-green)](https://gradle.org/)

Web UI test automation for a travel agency website, built with **Serenity BDD**, **Screenplay pattern**, **Cucumber**, **Selenium WebDriver** and **Gradle**.

## What it demonstrates

- Web UI testing with Selenium WebDriver via Serenity Screenplay
- Cucumber BDD scenarios with data-driven tables
- Page Object Model with Target elements
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
| Gradle | 8.6 |

## Run locally

Requires a running Selenoid instance or local ChromeDriver:

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
    └── features/       # Cucumber .feature files
```

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
