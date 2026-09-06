pipeline {
	agent any
    tools {
		jdk 'java'  // Especifica tu JDK configurado en Jenkins
    }

    environment {
		DOCKER_IMAGE = 'juanfranciscobumo/empleados:latest'
        DOCKER_PLATFORMS = 'linux/amd64,linux/arm64'
        SSH_KEY = 'clave-ec2.pem'  // Define la clave SSH como una variable
        EC2_HOST = 'admin@ec2-3-83-67-172.compute-1.amazonaws.com' // Define el host EC2 como variable
        TEST_REPO = 'https://github.com/juanfranciscobumo/AutoApiEmpleados.git' // Define el repositorio de pruebas
        APP_REPO = 'https://github.com/juanfranciscobumo/Empleados.git' // Define el repositorio de la aplicación
    }

    stages {
		stage('Login to Docker Hub') {
			steps {
				echo 'Autenticando en Docker Hub...'
                withCredentials([usernamePassword(credentialsId: 'docker_password', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
					sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                    '''
                }
            }
        }

        stage('Clonar repositorio de aplicación') {
			steps {
				echo 'Clonando el repositorio principal...'
                git url: APP_REPO
            }
        }

        stage('Construir aplicación') {
			steps {
				echo 'Construyendo la aplicación...'
                sh "./gradlew clean build"
            }
        }

        stage('Construir y empujar imagen Docker') {
			steps {
				echo 'Construyendo y empujando la imagen Docker...'
                script {
					sh "docker buildx build --platform ${DOCKER_PLATFORMS} -t ${DOCKER_IMAGE} --push ."
                }
            }
        }

        stage('Desplegar API en EC2') {
			steps {
				echo 'Desplegando API en EC2...'
                script {
					sh """
                        ssh -o StrictHostKeyChecking=no -i ${SSH_KEY} ${EC2_HOST} \
                        'docker pull ${DOCKER_IMAGE} && \
                        docker ps -q -f name=empleados | xargs -r docker stop && \
                        docker ps -a -q -f name=empleados | xargs -r docker rm && \
                        docker run -d -p 8081:8081 --name empleados ${DOCKER_IMAGE}'
                    """
                }
            }
        }

        stage('Clonar repositorio de pruebas') {
			steps {
				echo 'Clonando repositorio de pruebas...'
                git url: TEST_REPO, branch: 'main'
            }
        }

        stage('Ejecutar pruebas') {
			steps {
				echo 'Ejecutando pruebas automatizadas...'
                sh """
                    chmod +x ./gradlew
                    ./gradlew clean test aggregate
                """
            }
        }

        stage('Generar reporte de pruebas') {
			steps {
				echo 'Generando reporte de pruebas...'
                publishHTML(target: [
                    reportName: 'Serenity Report',
                    reportDir: 'target/site/serenity',
                    reportFiles: 'index.html',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
            }
        }
    }

    post {
		always {
			echo 'Pipeline finalizado.'
        }
        success {
			echo 'Pipeline ejecutado con éxito.'
        }
        failure {
			echo 'El pipeline ha fallado.'
        }
        cleanup {
			echo 'Limpiando el workspace...'
            cleanWs()
        }
    }
}
