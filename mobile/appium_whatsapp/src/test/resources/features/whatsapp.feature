#encoding: iso-8859-1
#Author: juanfranciscobumo@gmail.com
Feature: Send a message by whatsapp
  How a user from androind
  I want send a message by whatsapp
  To check that message was sent

  Scenario Outline: Send a message
    Given that Juan is on whatsapp
    When look the personal contact <contact> and send the message <message>
    Then juan will check that message was send at contact <contact> with message <message>

    Examples:
      | contact         | message                                |
      | Karen Estupinan | Disculpe, esto es un mensaje de prueba |
