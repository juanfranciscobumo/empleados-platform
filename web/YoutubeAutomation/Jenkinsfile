pipeline {
        agent none
    stages {
        stage('Descargar codigo') {
            agent any
            steps {
                echo 'Descargando código'
                git url: 'https://github.com/juanfranciscobumo/YoutubeAutomation.git', branch: 'master'
            }
        }
                stage('Compilar proyecto') {
            agent any
            steps {
                echo 'Ejecutando prueba'
                bat 'gradle clean test aggregate'
            }
                }
        stage('Escanear con sonar') {
            agent any
            steps {
                echo 'Escaneando con sonar'
                bat 'gradle sonarqube'
            }
        }
        stage('Generar reporte') {
            agent any
            steps {
                echo 'Generando reporte'
                publishHTML(target: [
        reportName : 'Serenity',
        reportDir:   'target/site/serenity',
        reportFiles: 'index.html',
        keepAll:     true,
        alwaysLinkToLastBuild: true,
        allowMissing: false
    ])
            }
        }
    }
}
