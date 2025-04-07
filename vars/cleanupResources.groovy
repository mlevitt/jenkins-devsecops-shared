def call(Map config) {
    sh "docker rmi ${config.dockerRegistry}/${config.imageName}:${config.imageTag} || true"
    sh "docker rmi ${config.dockerRegistry}/${config.imageName}:latest || true"
    sh "docker rmi ${config.dockerRegistry}/${config.imageName}-security:${config.imageTag} || true"
}


