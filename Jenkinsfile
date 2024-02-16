pipeline {
    agent any

    stages {

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package -Dmaven.test.skip'
            }
        }

        stage('Docker build') {
            steps {
                sh 'docker build -t dg-user-api:v1 .'
        }
        }

        stage('Deploy') {
             steps {
                      script {
                    def previousContainerId = sh(returnStdout: true, script: "docker ps -q --filter ancestor=dg-user-api:v1").trim()
                    if (previousContainerId) {
                        sh "docker stop $previousContainerId"
                        sh 'docker container rm dg-user-api'
                    } else {
                        echo "Предыдущий контейнер отсутствует"
                    }
                }
            sh 'docker run --network dg-user-network -d -p 8888:8888 --name dg-user-api dg-user-api:v1'
                 echo "Контейнер запущен"
        }
    }
}
}
