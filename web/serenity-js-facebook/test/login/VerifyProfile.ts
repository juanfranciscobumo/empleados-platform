import { Task } from '@serenity-js/core';
import { Ensure, includes } from '@serenity-js/assertions';
import { By, PageElement, Text } from '@serenity-js/web';

import { LoginPage } from './LoginForm';

export const VerifyMessage = {
  isVisible: (expectedText: string) =>
    Task.where(`#actor verifies message contains "${expectedText}"`,
      Ensure.that(Text.of(LoginPage.flashMessage()), includes(expectedText)),
    ),
};
