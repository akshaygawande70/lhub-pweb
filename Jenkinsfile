@Library('common-pipeline@devops') _

pipeline {
    agent {
        label 'terraform'
    }

    stages {
        stage('Deployment') {
            steps {
                script {
                    container('terraform') {
                        pweb.pipeline()
                    }
                }
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}