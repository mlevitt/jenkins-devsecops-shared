def call(Map config) {
    // Option 1: Push to Artifact Repository
    withCredentials([usernamePassword(credentialsId: config.artifactCredentialsId, passwordVariable: 'ARTIFACT_PASSWORD', usernameVariable: 'ARTIFACT_USERNAME')]) {
        sh """
        curl -u ${ARTIFACT_USERNAME}:${ARTIFACT_PASSWORD} -X POST "${config.artifactRegistry}/repository/${config.artifactRepo}/${config.imageName}/${config.imageTag}/security-artifacts.tar.gz" \
            --upload-file ${config.imageName}-${config.imageTag}-security-artifacts.tar.gz
        """
    }
    
    // Option 2: Push to Docker Registry as an artifact container
    sh """
    mkdir -p container-build
    cp ${config.imageName}-${config.imageTag}-security-artifacts.tar.gz container-build/
    
    cat << EOF > container-build/Dockerfile
    FROM scratch
    ADD ${config.imageName}-${config.imageTag}-security-artifacts.tar.gz /
    CMD [""]
    EOF
    
    cd container-build
    docker build -t ${config.dockerRegistry}/${config.imageName}-security:${config.imageTag} .
    """
    
    withCredentials([usernamePassword(credentialsId: config.dockerCredentialsId, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
        sh "echo \$DOCKER_PASSWORD | docker login ${config.dockerRegistry} -u \$DOCKER_USERNAME --password-stdin"
        sh "docker push ${config.dockerRegistry}/${config.imageName}-security:${config.imageTag}"
    }
}


