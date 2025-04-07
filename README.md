# DevSecOps Docker Pipeline Shared Library

This Jenkins shared library implements DevSecOps best practices for Docker image building, scanning, and security artifact management.

## Setup Instructions

1. Configure this repository as a Jenkins Shared Library:
   - In Jenkins, go to Manage Jenkins > Configure System
   - Under "Global Pipeline Libraries" section, add a new library:
     - Name: `devsecops-library`
     - Default version: `main` (or your preferred branch)
     - Retrieval method: Modern SCM > Git
     - Project repository: URL to this repository
     - Credentials: As needed for repository access

2. Install required Jenkins plugins:
   - Pipeline
   - Docker Pipeline
   - OWASP Dependency-Check
   - SonarQube Scanner
   - Warnings Next Generation

3. Configure credentials in Jenkins:
   - Docker registry credentials
   - SonarQube credentials
   - Snyk API token
   - Artifact repository credentials
   - Cosign signing key

## Usage

Import the library and call the `devSecOpsDockerPipeline` function in your Jenkinsfile:

```groovy
@Library('devsecops-library') _

devSecOpsDockerPipeline(
    dockerRegistry: 'registry.example.com',
    imageName: 'my-application'
    // Add other configuration as needed
)
```

## Configuration Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `dockerRegistry` | Docker registry URL | 'your-registry.example.com' |
| `imageName` | Name of the Docker image | 'your-app' |
| `imageTag` | Tag for the Docker image | env.BUILD_NUMBER |
| `dockerCredentialsId` | Jenkins credentials ID for Docker registry | 'docker-credentials' |
| `sonarqubeCredentialsId` | Jenkins credentials ID for SonarQube | 'sonarqube-credentials' |
| `snykTokenId` | Jenkins credentials ID for Snyk API token | 'snyk-token' |
| `artifactRegistry` | Artifact repository URL | 'your-artifact-registry.example.com' |
| `artifactRepo` | Repository name in artifact registry | 'security-artifacts' |
| `artifactCredentialsId` | Jenkins credentials ID for artifact repository | 'artifact-repo-credentials' |
| `securityNotificationEmail` | Email address for security notifications | 'security-team@example.com' |
| `enableGitVerification` | Enable Git commit signature verification | true |
| `enableSnyk` | Enable Snyk container scanning | true |
| `enableTrivy` | Enable Trivy container scanning | true |
| `enableSonarQube` | Enable SonarQube static analysis | true |
| `enableDependencyCheck` | Enable OWASP Dependency Check | true |
| `enableDockerBench` | Enable Docker Bench Security | true |
| `enableArtifactStorage` | Enable security artifact storage | true |
| `enableCosignSigning` | Enable container signing and attestation | true |
| `securityDashboardUrl` | URL for security dashboard updates | 'https://your-security-dashboard.example.com/api/update-status' |
| `vulnerabilityTrackerUrl` | URL for vulnerability tracker | 'https://your-vulnerability-tracker.example.com/api/register-build' |

## Pipeline Stages

1. **Checkout**: Clone repository and verify Git commits
2. **Security Scans**: Run dependency checks and static analysis in parallel
3. **Build Docker Image**: Build the container image using BuildKit
4. **Container Security**: Run container security scans in parallel
5. **Generate SBOM**: Create Software Bill of Materials in multiple formats
6. **Security Artifacts**: Package and store security artifacts
7. **Sign & Attest**: Sign the container image and attach attestations
8. **Push to Registry**: Push the container image to the registry

## Extending

To add new functionality, create additional `.groovy` files in the `vars` directory.
