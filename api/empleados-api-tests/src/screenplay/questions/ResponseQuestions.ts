import { Question } from '@serenity-js/core';

export class ResponseStatusCode {
  static value(): Question<number> {
    return Question.about('the response status code')
      .answeredBy(actor => {
        // This would typically be answered by an ability
        return 200;
      });
  }
}

export class EmployeeData {
  static value(): Question<any> {
    return Question.about('the employee data')
      .answeredBy(actor => {
        // This would typically be answered by an ability
        return {};
      });
  }
}
