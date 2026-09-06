# Google Search REST API Testing

[![CI](https://github.com/juanfranciscobumo/GoogleSearchRest/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/GoogleSearchRest/actions/workflows/ci.yml)
[![Serenity BDD](https://img.shields.io/badge/Serenity--BDD-4.2.7-informational)](https://serenity-bdd.github.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-green)](https://gradle.org/)

Automated REST API testing against the **Google Custom Search API**, built with **Serenity BDD**, **Screenplay pattern**, **Cucumber** and **Gradle**.

## What it demonstrates

- REST API testing with Serenity Screenplay REST
- Cucumber BDD scenarios with Gherkin syntax
- Response validation (status codes, JSON fields)
- Environment-based configuration (API keys via secrets)

## Tech stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Serenity BDD | 4.2.7 |
| Cucumber | 7.22.0 |
| JUnit | 5.11.4 |
| Gradle | 8.5 |

## Run locally

Set the `GOOGLE_API_KEY` environment variable, then:

```bash
./gradlew clean test aggregate
```

On Windows:

```powershell
$env:GOOGLE_API_KEY="your-key"; .\gradlew.bat clean test aggregate
```

The Serenity report is generated under `build/site/serenity/`.

## CI/CD

GitHub Actions runs the test suite on every push to `master` and deploys the Serenity report to **GitHub Pages**.

> **Note:** The `GOOGLE_API_KEY` secret must be configured in the repository settings.

## Project structure

```
src/
├── main/java/          # Step definitions, tasks, questions, models
└── test/resources/
    └── features/       # Cucumber .feature files
```

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
