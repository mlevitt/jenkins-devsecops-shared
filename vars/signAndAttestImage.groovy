def call(String registry, String imageName, String imageTag) {
    withCredentials([file(credentialsId: 'cosign-key', variable: 'COSIGN_KEY')]) {
        sh "cosign sign --key \${COSIGN_KEY} ${registry}/${imageName}:${imageTag}"
        sh "cosign attest --key \${COSIGN_KEY} --type cyclonedx --predicate sbom-cyclonedx.json ${registry}/${imageName}:${imageTag}"
        sh "cosign attest --key \${COSIGN_KEY} --type vuln --predicate trivy-report.json ${registry}/${imageName}:${imageTag}"
    }
}


