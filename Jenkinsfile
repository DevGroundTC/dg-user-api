pipeline {
    agent any

    stages {

        stage('Test') {
            steps {
                sh 'mvn test'
                //еще раз проходят тесты???
                sh 'mvn clean package'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package -Dmaven.test.skip'
            }
        }

        stage('Docker build') {
            steps {
//                 script {
//                 docker.build("dg-user-api:latest")
//                 }
//             }
                sh 'docker build -t dg-user-api:latest .'
        }

        stage('Deploy') {
            steps {
            // как остановится, если сначала не запускали
           //     sh 'docker stop dg-user-api:latest'
                sh 'docker run -p 1000:1000 --name user-api dg-user-api:latest'
            }
        }
    }
}                            
