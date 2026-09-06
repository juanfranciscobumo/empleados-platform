import { Task } from '@serenity-js/core';
import { Enter, Press } from '@serenity-js/web';

import { LoginPage } from './LoginForm';

export const Authenticate = {
  using: (username: string, password: string) =>
    Task.where(`#actor logs in as ${username}`,
      Enter.theValue(username).into(LoginPage.usernameField()),
      Enter.theValue(password).into(LoginPage.passwordField()),
      Press.the('Enter').in(LoginPage.passwordField()),
    ),
};
