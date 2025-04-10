pipeline {
    agent any
    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=.m2/repository"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/katsiaryne/jenkins'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'

            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                bat 'mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/TEST-*.xml'
                    bat 'mvn allure:report'
                    archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true

                    allure([
                            includeProperties: false,
                            jdk: '',
                            results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed!'
        }
    }
}
