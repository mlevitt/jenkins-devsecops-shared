def call(String trackerUrl) {
    if (fileExists('build-metadata.json')) {
        sh """
        curl -X POST -H "Content-Type: application/json" -d @build-metadata.json \
             ${trackerUrl}
        """
    }
}


