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

        stage('Publish Jars & Amps') {
            when {
                anyOf {
                    branch "master*"
                    branch "release*"
                }
            }
            environment {
                SONNATYPE_CREDENTIALS = credentials('sonatype')
                GPGPASSPHRASE = credentials('gpgpassphrase')
            }
            steps {
                script {
                    sh "./gradlew publish -Pde_publish_username=${SONNATYPE_CREDENTIALS_USR} -Pde_publish_password=${SONNATYPE_CREDENTIALS_PSW} -PkeyId=DF8285F0 -Ppassword=${GPGPASSPHRASE} -PsecretKeyRingFile=/var/jenkins_home/secring.gpg"
                }
            }
        }

        stage('Close Sonatype Repo') {
            when {
                anyOf {
                    branch "release*"
                }
            }
            environment {
                SONNATYPE_CREDENTIALS = credentials('sonatype')
            }
            steps {
                script {
                    sh "./gradlew closeAndReleaseRepository -Pde_publish_username=${SONNATYPE_CREDENTIALS_USR} -Pde_publish_password=${SONNATYPE_CREDENTIALS_PSW}"
                }
            }
        }
    }


    post {
        always {
            junit '**/build/test-results/**/*.xml'
        }
    }
}


