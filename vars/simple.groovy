def call(body) {
    
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    pipeline {
        environment {
        NEXUS_D = credentials('nexus-d')
    }
  agent {
    kubernetes {
      yaml """
kind: Pod
spec:
  containers:
  - name: dind
    image: docker:dind
    securityContext:
      privileged: true
    args:
      - "--insecure-registry=192.168.2.5:8082"
"""
    }
  }
  stages {
      stage('Build Maven'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: pipelineParams.git_url]]])
                mavenBuild()
            }
    }
      stage('Docker build') {
      steps {
        container(name: 'dind') {
            sh """
                    docker build -f src/main/docker/Dockerfile.jvm -t ${pipelineParams.name} .
                    docker login ${pipelineParams.address} -u=${pipelineParams.username} -p=${NEXUS_D}
                    docker tag ${pipelineParams.name}:${pipelineParams.tag} ${pipelineParams.repo}/${pipelineParams.name}
                    docker push ${pipelineParams.repo}/${pipelineParams.name}
                """
        }
      }
    }
  }
}
}
