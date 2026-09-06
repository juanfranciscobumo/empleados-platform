#Author: pacho0323@hotmail.com

@tag
Feature: Agencia de viajes
  Yo como usuario
  quiero buscar la tarifa economica en diferentes destinos

  @tag1
  Scenario Outline: Buscar tarifa mas economica
    Given que el usuario se encuentre en la pagina web
    When realice la busqueda con los datos solicitados
      | location   | mesInicio   | anoInicio   | diaInicio   | mesFin   | anoFin   | diaFin   | rooms   | adults   | children   |
      | <location> | <mesInicio> | <anoInicio> | <diaInicio> | <mesFin> | <anoFin> | <diaFin> | <rooms> | <adults> | <children> |
    And buscara el precio mas economico
      | diaInicio   | mesInicio   | anoInicio   | diaFin   | mesFin   | anoFin   |
      | <diaInicio> | <mesInicio> | <anoInicio> | <diaFin> | <mesFin> | <anoFin> |
    Then valide el precio Total

    Examples:
      | location      | mesInicio | anoInicio | diaInicio | mesFin  | anoFin | diaFin | rooms | adults | children |
      | San Francisco | SEPTEMBER | 2025      | 25        | OCTOBER | 2025   | 9      | 1     | 1      | 0        |
