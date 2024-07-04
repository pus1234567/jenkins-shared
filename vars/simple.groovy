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
            dockerBuildPush(name:"ex1", tag:"latest" address:"192.168.2.5:8082", repo:"192.168.2.5:8082/repository/docker-images", username:"admin", pass:NEXUS_D)
        }
      }
    }
  }
}
}
