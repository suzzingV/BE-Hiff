pipeline {
    agent any

    environment {
        JAVA_HOME = tool name: 'JDK 17', type: 'jdk'
        GRADLE_HOME = tool name: 'Gradle 8', type: 'gradle'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
        DOCKER_REGISTRY_CREDENTIALS = 'docker-credentials-id'
        DOCKER_REGISTRY_URL = 'https://index.docker.io/v1/'
        IMAGE_NAME = 'dh5252/hiff-backend'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop_CI', credentialsId: 'ab34bc2c-f8ab-425a-b091-57a1ac313b1a', url: 'https://github.com/Team-Hiff/BE-Hiff.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def imageName = "${env.IMAGE_NAME}:${gitCommit}"

                    withCredentials([usernamePassword(credentialsId: "${env.DOCKER_REGISTRY_CREDENTIALS}", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin ${env.DOCKER_REGISTRY_URL}"
                    }

                    sh "docker build -t ${imageName} ."

                    sh "docker push ${imageName}"                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive : true
            }
        }
    }
    post {
            always {
                echo 'CI, BUILD completed'
            }
    }

}