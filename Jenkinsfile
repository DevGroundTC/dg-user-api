pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Шаги сборки приложения
                // Например, сборка кода или сборка Docker образа
                // Это может включать команды типа 'docker build'
                sh 'mvn clean package'
                script {
                docker.build("dg-user-api:latest")
                }
            }
        }

        stage('Deploy') {
            steps {
                // Шаг развертывания на Docker
                // Например, команда 'docker run' для запуска контейнера
                sh 'docker run -d -p 1000:1000 dg-user-api:latest'
            }
        }
    }
}
