                  # language: es
                  Característica: Autenticación en The Internet

                    Esquema del escenario: Iniciar sesión con credenciales
                    Dado que James accede a la página de login
                    Cuando ingresa sus credenciales
                      | usuario | <usuario> |
                      | clave   | <clave>   |
                    Entonces verá el mensaje '<mensaje>'

                    Ejemplos:
                      | usuario    | clave             | mensaje                     |
                      | tomsmith   | SuperSecretPassword! | You logged into a secure area! |
                      | foobar     | barfoo               | Your username is invalid!       |
