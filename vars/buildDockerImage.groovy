def call(String registry, String imageName, String imageTag) {
    sh "DOCKER_BUILDKIT=1 docker build --no-cache --pull -t ${registry}/${imageName}:${imageTag} -t ${registry}/${imageName}:latest ."
}


