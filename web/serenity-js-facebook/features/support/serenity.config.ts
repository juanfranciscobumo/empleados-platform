import { AfterAll, BeforeAll, setDefaultTimeout } from '@cucumber/cucumber';
import { configure, Duration } from '@serenity-js/core';
import * as path from 'path';
import * as playwright from 'playwright';

import { Actors } from '../../test';

const timeouts = {
  cucumber: {
    step: Duration.ofSeconds(30),
  },
  playwright: {
    defaultNavigationTimeout: Duration.ofSeconds(10),
    defaultTimeout: Duration.ofSeconds(5),
  },
  serenity: {
    cueTimeout: Duration.ofSeconds(5),
  }
};

let browser: playwright.Browser;

setDefaultTimeout(timeouts.cucumber.step.inMilliseconds());

BeforeAll(async () => {
  browser = await playwright.chromium.launch({
    headless: process.env.HEADLESS !== 'false',
  });

  configure({
    actors: new Actors(
      browser,
      {
        baseURL: 'https://the-internet.herokuapp.com/',
      },
      {
        defaultNavigationTimeout: timeouts.playwright.defaultNavigationTimeout.inMilliseconds(),
        defaultTimeout: timeouts.playwright.defaultTimeout.inMilliseconds(),
      }
    ),
    crew: [
      ['@serenity-js/console-reporter', { theme: 'auto' }],
      ['@serenity-js/web:Photographer', {
        strategy: 'TakePhotosOfFailures',
      }],
      ['@serenity-js/core:ArtifactArchiver', { outputDirectory: path.resolve(__dirname, '../../target/site/serenity') }],
      ['@serenity-js/serenity-bdd', { specDirectory: path.resolve(__dirname, '../../features') }],
    ],
    cueTimeout: timeouts.serenity.cueTimeout,
  });
});

AfterAll(async () => {
  if (browser) {
    await browser.close();
  }
});
