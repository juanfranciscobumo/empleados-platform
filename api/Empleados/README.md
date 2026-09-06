# Employee API

Sample Spring Boot REST API used as the system under test for the portfolio's API-quality suite.

## Run locally

Requires JDK 22.

```powershell
.\gradlew.bat test
.\gradlew.bat bootRun
```

The API uses an in-memory H2 database for local development.

## Security

No credentials, private keys, or environment files belong in this repository. Configure deployment credentials through the CI platform's secret store. If this repository was ever public, rotate any credential that was previously committed before deploying it again.

## Related project

The automated API tests live in [AutoApiEmpleados](https://github.com/juanfranciscobumo/AutoApiEmpleados).
