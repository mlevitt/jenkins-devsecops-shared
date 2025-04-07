def call(String tokenId, String registry, String imageName, String imageTag) {
    withCredentials([string(credentialsId: tokenId, variable: 'SNYK_TOKEN')]) {
        sh "snyk container test ${registry}/${imageName}:${imageTag} --severity-threshold=high --json > snyk-report.json || true"
    }
    recordIssues tools: [snyk(pattern: 'snyk-report.json')]
}


