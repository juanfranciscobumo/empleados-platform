# YouTube Video Search - Maven Edition

[![CI](https://github.com/juanfranciscobumo/YoutubeWithMaven/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/YoutubeWithMaven/actions/workflows/ci.yml)
[![Serenity BDD](https://img.shields.io/badge/Serenity--BDD-4.2.7-informational)](https://serenity-bdd.github.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9.6-blue)](https://maven.apache.org/)

Web UI test automation for **YouTube** video search, built with **Serenity BDD**, **Screenplay pattern**, **Cucumber**, **Selenium WebDriver** and **Maven**.

This is the Maven counterpart of [YoutubeAutomation](https://github.com/juanfranciscobumo/YoutubeAutomation).

## What it demonstrates

- Web UI testing with Selenium WebDriver via Serenity Screenplay
- Video search, playback and duration validation
- Menu navigation testing
- Maven build lifecycle with Serenity Maven plugin
- Serenity HTML reports

## Tech stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Serenity BDD | 4.2.7 |
| Cucumber | 7.22.0 |
| JUnit | 5.11.4 |
| Selenium | managed by Serenity |
| Maven | 3.9.6 |

## Test scenarios

- Search for a video on YouTube and validate it plays correctly
- Verify video duration matches expected value
- Navigate YouTube menu sections

## Run locally

```bash
mvn clean verify
```

The Serenity report is generated under `target/site/serenity/`.

## CI/CD

GitHub Actions runs the test suite on every push to `master` using **Selenoid** for browser automation, and deploys the Serenity report to **GitHub Pages**.

Report: https://juanfranciscobumo.github.io/YoutubeWithMaven/

## Project structure

```
src/
├── main/java/          # Step definitions, tasks, questions, UI targets
└── test/resources/
    └── features/       # Cucumber .feature files
```

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
