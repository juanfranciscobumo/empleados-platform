import { Given, Then, When } from '@cucumber/cucumber';
import { actorCalled, actorInTheSpotlight } from '@serenity-js/core';
import { Navigate } from '@serenity-js/web';

import { Authenticate } from '../../test/login/Authenticate';
import { VerifyMessage } from '../../test/login/VerifyProfile';

Given('que {actor} accede a la página de login', (actorName: string) =>
  actorCalled(actorName).attemptsTo(
    Navigate.to('https://the-internet.herokuapp.com/login'),
  )
);

When('ingresa sus credenciales', (table: { rowsHash: () => Record<string, string> }) => {
  const rows = table.rowsHash();
  return actorInTheSpotlight().attemptsTo(
    Authenticate.using(rows['usuario'], rows['clave']),
  );
});

Then('verá el mensaje {string}', (expectedMessage: string) =>
  actorInTheSpotlight().attemptsTo(
    VerifyMessage.isVisible(expectedMessage),
  )
);
