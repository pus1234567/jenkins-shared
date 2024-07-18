def call(Map config = [:]) {
    pipeline {
        agent {
    kubernetes {
      yaml """
kind: Pod
spec:
  containers:
  - name: dind1
    image: docker:dind
    securityContext:
      privileged: true
"""
    }
  }
        stages {
            stage('Build and Push Agent Images') {
                steps {
                    container('dind') {
                        for(p in config) {
                            sh 'mkdir ${p.value}'
                            dir(p.value)
                        }
                    }
                }
            }
        }
    }
}
