# YouTube Video Search - Web UI Testing

[![CI](https://github.com/juanfranciscobumo/YoutubeAutomation/actions/workflows/ci.yml/badge.svg)](https://github.com/juanfranciscobumo/YoutubeAutomation/actions/workflows/ci.yml)
[![Serenity BDD](https://img.shields.io/badge/Serenity--BDD-4.2.7-informational)](https://serenity-bdd.github.io/)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-green)](https://gradle.org/)

Web UI test automation for **YouTube** video search and navigation, built with **Serenity BDD**, **Screenplay pattern**, **Cucumber**, **Selenium WebDriver** and **Gradle**.

## What it demonstrates

- Web UI testing with Selenium WebDriver via Serenity Screenplay
- Video search, playback and duration validation
- Menu navigation testing
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
| Gradle | 8.5 |

## Test scenarios

- Search for a video on YouTube and validate it plays correctly
- Verify video duration matches expected value
- Navigate YouTube menu sections

## Run locally

Requires a running Selenoid instance or local ChromeDriver:

```bash
./gradlew clean test aggregate
```

On Windows:

```powershell
.\gradlew.bat clean test aggregate
```

The Serenity report is generated under `build/site/serenity/`.

## CI/CD

GitHub Actions runs the test suite on every push to `master` using **Selenoid** for browser automation, and deploys the Serenity report to **GitHub Pages**.

Report: https://juanfranciscobumo.github.io/YoutubeAutomation/

## Project structure

```
src/
├── main/java/          # Step definitions, tasks, questions, UI targets
└── test/resources/
    └── features/       # Cucumber .feature files
```

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
