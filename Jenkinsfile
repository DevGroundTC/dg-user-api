pipeline {
    agent any

    stages {

        stage('Test') {
            steps {
                sh 'mvn test'
                //еще раз проходят тесты???
                //sh 'mvn clean package'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package -Dmaven.test.skip'
            }
        }

        stage('Docker build') {
            steps {
                sh 'docker build -t dg-user-api:latest .'
        }
        }

        stage('Deploy') {
             steps {
            // // как остановится, если сначала не запускали
            //     sh 'docker stop dg-user-api:latest'
            //     sh 'docker run -p 1000:1000 --name user-api dg-user-api:latest'
            // }
                      script {
                    def previousContainerId = sh(returnStdout: true, script: "docker ps -q --filter ancestor=dg-user-api:latest").trim()
                    if (previousContainerId) {
                        sh "docker stop $previousContainerId"
                    } else {
                        echo "Предыдущий контейнер отсутствует"
                    }
                }
            sh 'docker run -p 1000:1000 --name user-api dg-user-api:latest'
                 echo "Контейнер запущен"
        }
    }
}
}
