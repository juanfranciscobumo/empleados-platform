#juanfranciscobumo@gmail.com
Feature: Register user
  I like user of utest
  Want register my data
  To enter to utest

  Scenario Outline: Register user
    Given the user is the page utest
    When enter his datas
      | firstName   | lastName   | emailAddress   | month   | year   | day   | languages   | device   | model   | operatingSystem   | password   |
      | <firstName> | <lastName> | <emailAddress> | <month> | <year> | <day> | <languages> | <device> | <model> | <operatingSystem> | <password> |
    Then the user will registered and he will find the button 'Complete Setup'
    Examples: Data
      | firstName | lastName | emailAddress            | month  | year | day | languages | device | model     | operatingSystem | password       |
      | Juan      | Builes   | francisco@francisco.com | August | 1989 | 10  | Spanish   | Apple  | iPad 1 3G | iOS 4.3.1       | Noviembre2019* |


