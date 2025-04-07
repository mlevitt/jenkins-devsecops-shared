def call(String registry, String imageName, String imageTag) {
    sh "trivy image --format json --output trivy-report.json ${registry}/${imageName}:${imageTag}"
    recordIssues tools: [trivy(pattern: 'trivy-report.json')]
}


