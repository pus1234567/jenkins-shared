def call(Map config = [:]) {
    sh """
        docker build -f src/main/docker/Dockerfile.jvm -t ${config.name} .
        docker login ${config.address} -u=admin -p=${config.pass}
        docker tag ${config.name}:latest ${config.repo}/${config.name}
        docker push ${config.repo}/${config.name}
    """
}