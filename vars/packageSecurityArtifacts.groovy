def call(Map config) {
    // Create build metadata file
    sh """
    cat << EOF > build-metadata.json
    {
        "buildId": "${env.BUILD_NUMBER}",
        "imageId": "${config.dockerRegistry}/${config.imageName}:${config.imageTag}",
        "gitCommit": "\$(git rev-parse HEAD)",
        "gitBranch": "\$(git rev-parse --abbrev-ref HEAD)",
        "buildDate": "\$(date -u +"%Y-%m-%dT%H:%M:%SZ")",
        "artifacts": [
            "sbom.json",
            "sbom-spdx.json",
            "sbom-cyclonedx.json",
            "dependency-check-report.xml",
            "dependency-check-report.html",
            "trivy-report.json",
            "snyk-report.json",
            "docker-bench-results.txt",
            "sonarqube-issues.json"
        ]
    }
    EOF
    """
    
    // Package all security artifacts
    sh 'mkdir -p security-artifacts'
    sh 'cp sbom*.json dependency-check-report.* trivy-report.json snyk-report.json docker-bench-results.txt sonarqube-issues.json build-metadata.json security-artifacts/ 2>/dev/null || true'
    sh "tar -czf ${config.imageName}-${config.imageTag}-security-artifacts.tar.gz security-artifacts"
}


