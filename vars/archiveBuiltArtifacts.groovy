def call() {
    archiveArtifacts artifacts: '*-security-artifacts.tar.gz, security-artifacts/*, build-metadata.json', allowEmptyArchive: true
}


