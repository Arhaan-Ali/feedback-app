pipeline {
    agent any
    tools { maven 'Maven' }
    stages {
        stage('Checkout') { steps { checkout scm } }
        stage('Maven Build') { steps { sh 'mvn -B clean package' } }
    }
    post {
        success { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true }
        always { echo 'Feedback app pipeline finished.' }
    }
}
