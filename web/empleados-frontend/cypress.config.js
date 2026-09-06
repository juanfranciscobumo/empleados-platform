{
  "e2e": {
    "baseUrl": "http://localhost:80",
    "supportFile": "cypress/support/e2e.js",
    "specPattern": "cypress/e2e/**/*.cy.{js,jsx,ts,tsx}",
    "viewportWidth": 1280,
    "viewportHeight": 720,
    "video": false,
    "screenshotOnRunFailure": true,
    "retries": {
      "runMode": 2,
      "openMode": 0
    },
    "env": {
      "API_URL": "http://localhost:8081"
    }
  }
}
