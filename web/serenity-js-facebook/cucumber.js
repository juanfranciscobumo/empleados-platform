module.exports = {
  default: [
    `--require-module ts-node/register`,
    `--format @serenity-js/cucumber`,
    `--format-options {"specDirectory":"features"}`,
    `--require features/step-definitions/parameter.steps.ts`,
    `--require features/step-definitions/login.steps.ts`,
    `--require features/support/serenity.config.ts`,
  ].join(' ')
};
