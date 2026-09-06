# Portfolio cleanup and publishing plan

This document is the source of truth for the local portfolio review completed on 2026-08-17. It deliberately does not delete repositories or rewrite Git history.

## Feature repositories

| Repository | Status | Next work |
| --- | --- | --- |
| `CV` | Feature | Keep its project cards aligned with the repositories below. |
| `RetoLaHaus` | Feature | Refresh dependencies, add a current GitHub Actions workflow, and document the test environment. |
| `Empleados` + `AutoApiEmpleados` | Feature | Present as one system-under-test plus API quality suite. |
| `Galibu` | Candidate | Feature only after its Android test coverage and README describe ownership and architecture. |
| `GoogleSearchRest` | Learning example | Run only with `GOOGLE_API_KEY` injected as an environment variable or CI secret. |
| `appium_whatsapp` | Legacy mobile example | Replace the WhatsApp dependency with a controllable demo app before featuring it. |

## Archive candidates

Archive on GitHub instead of deleting so links and history remain available: `CapacitacionQvision`, `HotelPOM`, `RetoUtestSophos`, `Selenium`, `cucumberJunit`, `serenity-js-facebook`, `Serenity-js-rest`, and `YoutubeWithMaven`.

Delete or make private: `pruebasDocker` and the `serenity-js` upstream fork. Keep `YoutubeAutomation` only as a legacy comparison until it is replaced by a current, stable test target.

## Security remediation required in GitHub and provider consoles

1. Revoke and replace the EC2 private key that was committed under `Empleados`. The file has been removed from the current working tree, but is still present in Git history and may be present on the remote.
2. Revoke and replace the Google Custom Search key formerly committed in `GoogleSearchRest`; configure the replacement as `GOOGLE_API_KEY` in GitHub Actions secrets.
3. Revoke and replace the Netlify token formerly committed in the workflows of `GoogleSearchRest`, `HotelPOM`, and `Serenity-js-rest`; set replacement values as `NETLIFY_AUTH_TOKEN` and `NETLIFY_SITE_ID` repository secrets only where deployment remains required.
4. Rewrite the affected repositories' Git history with an approved credential-removal process, force-push the cleaned history, and notify any collaborators to re-clone. Removing files in a new commit does not erase prior exposure.
5. Review Firebase API-key restrictions for `Galibu` in Google Cloud/Firebase. `google-services.json` is normally public configuration, but its key must be restricted to the Android app's package and signing certificate.

## Recommended new repositories

- `qa-platform-e2e`: Playwright + TypeScript, UI/API coverage, trace/video artifacts, parallel execution, and GitHub Actions.
- `mobile-automation-lab`: Appium 2 against a controllable Android demo application, with CI execution instructions.
- `quality-engineering-toolkit`: reusable test-data, API client, polling, configuration, and reporting utilities.
- `performance-testing-lab`: k6 scenarios and threshold-based performance reporting.
