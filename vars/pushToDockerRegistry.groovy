def call(String credentialsId, String registry, String imageName, String imageTag) {
    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
        sh "echo \$DOCKER_PASSWORD | docker login ${registry} -u \$DOCKER_USERNAME --password-stdin"
        sh "docker push ${registry}/${imageName}:${imageTag}"
        sh "docker push ${registry}/${imageName}:latest"
    }
}


