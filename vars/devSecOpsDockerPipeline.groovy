
def call(Map config = [:]) {
    // Default configuration with overridable parameters
    def defaults = [
        dockerRegistry: 'your-registry.example.com',
        imageName: 'your-app',
        imageTag: env.BUILD_NUMBER,
        dockerCredentialsId: 'docker-credentials',
        sonarqubeCredentialsId: 'sonarqube-credentials',
        snykTokenId: 'snyk-token',
        artifactRegistry: 'your-artifact-registry.example.com',
        artifactRepo: 'security-artifacts',
        artifactCredentialsId: 'artifact-repo-credentials',
        securityNotificationEmail: 'security-team@example.com',
        enableGitVerification: true,
        enableSnyk: true,
        enableTrivy: true,
        enableSonarQube: true,
        enableDependencyCheck: true,
        enableDockerBench: true,
        enableArtifactStorage: true,
        enableCosignSigning: true,
        securityDashboardUrl: 'https://your-security-dashboard.example.com/api/update-status',
        vulnerabilityTrackerUrl: 'https://your-vulnerability-tracker.example.com/api/register-build'
    ]
    
    // Merge provided config with defaults
    config = defaults + config
    
    pipeline {
        agent any
        
        environment {
            DOCKER_REGISTRY = "${config.dockerRegistry}"
            IMAGE_NAME = "${config.imageName}"
            IMAGE_TAG = "${config.imageTag}"
            DOCKER_CREDENTIALS_ID = "${config.dockerCredentialsId}"
            SONARQUBE_CREDENTIALS_ID = "${config.sonarqubeCredentialsId}"
            SNYK_TOKEN_ID = "${config.snykTokenId}"
            ARTIFACT_REGISTRY = "${config.artifactRegistry}"
            ARTIFACT_REPO = "${config.artifactRepo}"
            ARTIFACT_CREDENTIALS_ID = "${config.artifactCredentialsId}"
        }
        
        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                    script {
                        if (config.enableGitVerification) {
                            sh 'git verify-commit HEAD || echo "Commit signature verification failed"'
                        }
                    }
                }
            }
            
            stage('Security Scans') {
                parallel {
                    stage('Dependency Check') {
                        when { expression { return config.enableDependencyCheck } }
                        steps {
                            dependencyCheckWrapper()
                        }
                    }
                    
                    stage('Static Analysis') {
                        when { expression { return config.enableSonarQube } }
                        steps {
                            sonarQubeAnalysis(config.sonarqubeCredentialsId, config.imageName)
                        }
                    }
                }
            }
            
            stage('Build Docker Image') {
                steps {
                    buildDockerImage(config.dockerRegistry, config.imageName, config.imageTag)
                }
            }
            
            stage('Container Security') {
                parallel {
                    stage('Trivy Scan') {
                        when { expression { return config.enableTrivy } }
                        steps {
                            trivyScan(config.dockerRegistry, config.imageName, config.imageTag)
                        }
                    }
                    
                    stage('Snyk Container Scan') {
                        when { expression { return config.enableSnyk } }
                        steps {
                            snykContainerScan(config.snykTokenId, config.dockerRegistry, config.imageName, config.imageTag)
                        }
                    }
                    
                    stage('Docker Bench Security') {
                        when { expression { return config.enableDockerBench } }
                        steps {
                            dockerBenchSecurity()
                        }
                    }
                }
            }
            
            stage('Generate SBOM') {
                steps {
                    generateSBOM(config.dockerRegistry, config.imageName, config.imageTag)
                }
            }
            
            stage('Security Artifacts') {
                when { expression { return config.enableArtifactStorage } }
                steps {
                    packageSecurityArtifacts(config)
                    storeSecurityArtifacts(config)
                }
            }
            
            stage('Sign & Attest') {
                when { expression { return config.enableCosignSigning } }
                steps {
                    signAndAttestImage(config.dockerRegistry, config.imageName, config.imageTag)
                }
            }
            
            stage('Push to Registry') {
                steps {
                    pushToDockerRegistry(config.dockerCredentialsId, config.dockerRegistry, config.imageName, config.imageTag)
                }
            }
        }
        
        post {
            always {
                script {
                    cleanupResources(config)
                    archiveBuiltArtifacts()
                    if (config.enableArtifactStorage) {
                        registerWithTracker(config.vulnerabilityTrackerUrl)
                    }
                }
            }
            
            success {
                echo 'Pipeline successful!'
                script {
                    if (config.enableArtifactStorage && config.securityDashboardUrl) {
                        updateSecurityDashboard(config)
                    }
                }
            }
            
            failure {
                echo 'Pipeline failed - sending notification'
                mail to: config.securityNotificationEmail,
                     subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                     body: "Something is wrong with ${env.BUILD_URL}"
            }
        }
    }
}


