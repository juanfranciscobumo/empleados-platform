import { serenityBDDReporter, playwright, Cast, ProjectConfig } from '@serenity-js/core';
import { ConsoleReporter } from '@serenity-js/console-reporter';
import { BrowseTheWeb } from '@serenity-js/playwright';
import * as path from 'path';

export const config: ProjectConfig = {
  runner: {
    cwd: path.resolve(__dirname),
  },
  actors: Cast.where(actorCan, (actor) => actor.whoCan(
    BrowseTheWeb.using(playwright.browser('chromium'))
  )),
  crew: [
    serenityBDDReporter(),
    ConsoleReporter.forStandardOutput(),
  ],
  prostgresReporter: {
    enabled: false,
  },
};

function actorCan(actor) {
  return actor;
}
