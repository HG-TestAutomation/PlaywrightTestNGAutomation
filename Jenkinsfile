pipeline {
    agent any

    tools {
        maven 'Maven-3.9.9' // Replace with your Maven version in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/HG-TestAutomation/PlaywrightTestNGAutomation.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Allure Report') {
            steps {
                sh 'mvn allure:report'
            }
        }

        stage('Archive Results') {
            steps {
                archiveArtifacts artifacts: '**/target/surefire-reports/*.xml', fingerprint: true
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
