import { defineConfig } from 'cypress';
import { EyesCypress } from '@applitools/eyes-cypress';

const eyes = EyesCypress.configure();

export default defineConfig({
  e2e: {
    baseUrl: 'https://www.saucedemo.com',
    viewportWidth: 1280,
    viewportHeight: 720,
    video: false,
    screenshotOnRunFailure: true,
    supportFile: 'cypress/support/e2e.ts',
    specPattern: 'cypress/e2e/**/*.cy.ts',
    env: {
      APPLITOOLS_API_KEY: process.env.APPLITOOLS_API_KEY || '',
      APPLITOOLS_SERVER_URL: 'https://api.applitools.com',
    },
  },
});
