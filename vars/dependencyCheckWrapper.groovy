def call() {
    // Check for vulnerable dependencies
    dependencyCheck additionalArguments: '--format XML --out dependency-check-report.xml', odcInstallation: 'OWASP-Dependency-Check'
    dependencyCheck additionalArguments: '--format HTML --out dependency-check-report.html', odcInstallation: 'OWASP-Dependency-Check'
    // Publish report
    dependencyCheckPublisher pattern: 'dependency-check-report.xml'
}


