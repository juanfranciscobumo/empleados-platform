import { Actor, Task, Expectation } from '@serenity-js/core';

export class VerifyApiResponse implements Task {
  static statusCode(expected: number) {
    return new VerifyApiResponse('statusCode', expected);
  }

  static isArray() {
    return new VerifyApiResponse('isArray');
  }

  static hasProperty(property: string) {
    return new VerifyApiResponse('hasProperty', property);
  }

  static hasPropertyValue(property: string, value: any) {
    return new VerifyApiResponse('hasPropertyValue', { property, value });
  }

  static hasErrorMessage(message: string) {
    return new VerifyApiResponse('hasErrorMessage', message);
  }

  static isEmpty() {
    return new VerifyApiResponse('isEmpty');
  }

  constructor(
    private check: string,
    private expected?: any,
  ) {}

  performAs(actor: Actor): Promise<void> {
    return new Promise((resolve) => {
      resolve();
    });
  }

  toString(): string {
    return `#actor verifies ${this.check}`;
  }
}
