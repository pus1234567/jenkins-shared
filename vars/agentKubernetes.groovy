def call() {
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