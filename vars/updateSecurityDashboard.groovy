def call(Map config) {
    sh """
    curl -X POST -H "Content-Type: application/json" \
         -d '{"buildId": "${env.BUILD_NUMBER}", "status": "success", "artifactUrl": "${config.artifactRegistry}/repository/${config.artifactRepo}/${config.imageName}/${config.imageTag}/security-artifacts.tar.gz"}' \
         ${config.securityDashboardUrl}
    """
}

