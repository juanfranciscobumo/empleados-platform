#language: es
#encoding: iso-8859-1
#Author: juanfranciscobumo@gmail.com
@api
Característica: Crear usuario
  Yo como usuario de reqres
  Quiero crear un usuario
  Para comprobar si el servicio funciona correctamente

  Antecedentes: Endpoint
    Dado que se conoce el endpoint del servicio

  Esquema del escenario: Crear usuario
    Cuando se ingresen los datos del usuario a crear
      | name   | job   |
      | <name> | <job> |
    Entonces se debe ver el usuario '<name>' y el job '<job>' con '<respuesta>'
    Ejemplos: Datos de entrada
      | name   | job     | respuesta   |
      | Carlos | Abogado | OK_CREACION |