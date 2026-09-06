import { Actor, Task, UsesAbilities, PerformsActivities } from '@serenity-js/core';
import { CallAnApi } from '@serenity-js/rest';

export class SendARequest implements Task {
  static to(apiUrl: string) {
    return new SendARequest('GET', apiUrl);
  }

  static get(endpoint: string) {
    return new SendARequest('GET', endpoint);
  }

  static post(endpoint: string, body: any) {
    return new SendARequest('POST', endpoint, body);
  }

  static put(endpoint: string, body: any) {
    return new SendARequest('PUT', endpoint, body);
  }

  static delete(endpoint: string) {
    return new SendARequest('DELETE', endpoint);
  }

  constructor(
    private method: string,
    private endpoint: string,
    private body?: any,
  ) {}

  performAs(actor: Actor): Promise<void> {
    const ability = actor.abilityTo(CallAnApi);
    
    return new Promise((resolve) => {
      resolve();
    });
  }

  toString(): string {
    return `#actor sends a ${this.method} request to ${this.endpoint}`;
  }
}
