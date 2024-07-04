def call(body) {
    
    def pipelineParams= [:]
    body.delegate = pipelineParams
    body.resolveStrategy = Closure.DELEGATE_FIRST
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