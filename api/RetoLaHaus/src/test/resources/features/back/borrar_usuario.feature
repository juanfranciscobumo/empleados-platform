#language: es
#encoding: iso-8859-1
#Author: juanfranciscobumo@gmail.com
@api
Característica: Borrar usuario
  Yo como usuario de reqres
  Quiero borrar un usuario
  Para comprobar que funciona el borrado de usuarios

  Antecedentes: Endpoint
    Dado que se conoce el endpoint del servicio

  Esquema del escenario: Borrar usuario
    Cuando se ingrese el código del usuario a borrar '<codigo>'
    Entonces no se debe visualizar el usuario '<respuesta>'
    Ejemplos: Datos de entrada
      | codigo | respuesta  |
      | 2      | OK_BORRADO |
