import { Actor, Cast, TakeNotes } from '@serenity-js/core';
import { BrowseTheWebWithPlaywright, ExtraBrowserContextOptions } from '@serenity-js/playwright';
import * as playwright from 'playwright';

export class Actors implements Cast {
  constructor(
    private readonly browser: playwright.Browser,
    private readonly browserContextOptions: playwright.BrowserContextOptions,
    private readonly extraBrowserContextOptions: ExtraBrowserContextOptions,
  ) {
  }

  prepare(actor: Actor): Actor {
    return actor.whoCan(
      BrowseTheWebWithPlaywright.using(this.browser, this.browserContextOptions, this.extraBrowserContextOptions),
      TakeNotes.usingAnEmptyNotepad(),
    );
  }
}
