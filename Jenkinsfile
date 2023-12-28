pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
                script {
                docker.build("dg-user-api:latest")
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker run -d -p 1000:1000 dg-user-api:latest'
            }
        }
    }
}
