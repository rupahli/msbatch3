pipeline {
  agent any
    
	environment {
    registry = "rupahli/inventory-management"
    registryCredential = 'rupahlidocker'
    dockerImage = ''
  }
   
   stages {
    stage('Build') {
      steps {
		    bat 'mvn -version'
			bat 'mvn clean install -DskipTests'
			
      }
    }
	
	stage('Building Docker image') {
      steps{
        script {
          dockerImage = docker.build registry + ":$BUILD_NUMBER"
        }
      }
    }
	
	 stage('Deploy Image') {
      steps{
        script {
          docker.withRegistry( '', registryCredential ) {
            dockerImage.push()
          }
        }
      }
    }
	
	
	}
	
    post {
		always {
			echo 'build success'
            /*cleanWs()*/
        }
  }
}
  