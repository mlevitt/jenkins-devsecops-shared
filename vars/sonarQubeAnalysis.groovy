def call(String credentialsId, String projectKey) {
    withSonarQubeEnv(credentialsId: credentialsId) {
        sh "sonar-scanner -Dsonar.projectKey=${projectKey} -Dsonar.sources=."
    }
    timeout(time: 5, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: true
    }
    sh "curl -u \$(cat ${credentialsId}) \${SONAR_HOST_URL}/api/issues/search?componentKeys=${projectKey} -o sonarqube-issues.json"
}


