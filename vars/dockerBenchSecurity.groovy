def call() {
    sh 'docker run --rm -v /var:/var -v /usr/bin/docker:/usr/bin/docker -v /var/run/docker.sock:/var/run/docker.sock aquasec/docker-bench-security > docker-bench-results.txt || true'
    archiveArtifacts artifacts: 'docker-bench-results.txt', allowEmptyArchive: true
}


