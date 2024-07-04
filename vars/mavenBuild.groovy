def call() {
    sh  '''
            chmod +x mvnw
            ./mvnw package
        '''
}