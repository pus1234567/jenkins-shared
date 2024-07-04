def call(String agentLabel,body) {
    
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    pipeline {
        agent any
        stages {
            stage("Hi") {
                steps {
                    sh "echo Hi"                   
                }
            }
    }
}
}