def call(String registry, String imageName, String imageTag) {
    sh "syft ${registry}/${imageName}:${imageTag} -o json > sbom.json"
    sh "syft ${registry}/${imageName}:${imageTag} -o spdx-json > sbom-spdx.json"
    sh "syft ${registry}/${imageName}:${imageTag} -o cyclonedx-json > sbom-cyclonedx.json"
}


