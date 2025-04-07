@Library('devsecops-library') _

// Simple Jenkinsfile that leverages the shared library
devSecOpsDockerPipeline(
    // Override defaults as needed
    dockerRegistry: 'registry.example.com',
    imageName: 'my-application',
    imageTag: "${env.BUILD_NUMBER}-${env.GIT_COMMIT.substring(0,7)}",
    
    // Security scan configuration
    enableGitVerification: true,
    enableSnyk: true,
    enableTrivy: true,
    enableSonarQube: true,
    enableDependencyCheck: true,
    enableDockerBench: true,
    
    // Artifact storage configuration
    enableArtifactStorage: true,
    artifactRegistry: 'artifacts.example.com',
    artifactRepo: 'security-artifacts',
    
    // Credentials
    dockerCredentialsId: 'docker-registry-creds',
    sonarqubeCredentialsId: 'sonar-creds',
    snykTokenId: 'snyk-api-token',
    artifactCredentialsId: 'artifactory-creds',
    
    // Notification settings
    securityNotificationEmail: 'security@example.com',
    securityDashboardUrl: 'https://security-dashboard.example.com/api/builds',
    vulnerabilityTrackerUrl: 'https://vuln-tracker.example.com/api/builds'
)
