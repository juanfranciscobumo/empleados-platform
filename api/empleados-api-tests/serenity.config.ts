import { serenityBDDReporter, playwright, Cast, ProjectConfig } from '@serenity-js/core';
import { ConsoleReporter } from '@serenity-js/console-reporter';
import { PlaywrightReport } from '@serenity-js/playwright';
import * as path from 'path';

export const config: ProjectConfig = {
  runner: {
    cwd: path.resolve(__dirname),
  },
  actors: Cast.where(actorCan, (actor) => actor.whoCan(
    // Add abilities here
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
