pipeline {
    agent any

    stages {
        stage("Clean") {
            steps {
                sh "./gradlew clean"
            }
        }

        stage("Assemble") {
            steps {
                sh "./gradlew assemble"
            }
        }

        stage("Test") {
            steps {
                sh "./gradlew test"
            }
        }
    }


    post {
        always {
            junit '**/build/test-results/**/*.xml'
        }
    }
}


